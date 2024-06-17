defmodule Betunfair.Actions.Market do
  import Betunfair.Actions.User

  @type market_id() :: String.t()
  @type text() :: String.t()
  @type bet_id() :: String.t()


  @doc """
  Creates a new market with the given name and description.

  ## Parameters
    - `name`: The name of the market.
    - `description`: The description of the market.

  ## Examples
      iex> {:ok, market_id} = Betunfair.Actions.Market.market_create("Market1", "Description of Market1")
  """
  @spec market_create(name :: text(), description :: text()) :: {:ok, market_id()} | {:error, text()}
  def market_create(name, description) do
    if :dets.member(:markets, name) do
      {:error, "This markets name already exists"}
    else
      :dets.insert(:markets, {name, description, :active})
      :dets.insert(:market_bets, {name, []})
      {:ok, name}
    end
  end

  @doc """
  Retrieves a list of all markets.

  ## Examples
      iex> {:ok, markets} = Betunfair.Actions.Market.market_list()
  """
  @spec market_list() :: {:ok, [market_id()]}
  def market_list() do
    list = :dets.foldl(fn ({name, _, _}, acc) -> acc ++ [name] end, [], :markets)
    {:ok, list}
  end

  @doc """
  Retrieves a list of all active markets.

  ## Examples
      iex> {:ok, active_markets} = Betunfair.Actions.Market.market_list_active()
  """
  @spec market_list_active() :: {:ok, [market_id()]}
  def market_list_active() do
    list = :dets.foldl(fn ({name, _, status}, acc) -> if status == :active do acc ++ [name] end end, [], :markets)
    {:ok, list}
  end

  # Function to retrieve a list of bets associated with a market
  @doc """
  Retrieves a list of bets associated with a market.

  ## Parameters
    - `id`: The unique identifier for the market.

  ## Examples
      iex> {:ok, bets} = Betunfair.Actions.Market.market_bets("Market1")
  """
  @spec market_bets(id :: market_id()) :: {:ok, Enumerable.t(bet_id())}
  def market_bets(id) do
    if :dets.member(:market_bets, id) do
      [{_, list}] = :dets.lookup(:market_bets, id)
      {:ok, list}
    else
      {:error, "The market with id #{id} does not exist"}
    end
  end


  # Function to cancel a market
  @doc """
  Cancels the market and refunds bets associated with it.

  ## Parameters
    - `id`: The unique identifier for the market.

  ## Examples
      iex> {:ok, bets} = Betunfair.Actions.Market.market_cancel("Market1")
  """
  @spec market_cancel(id :: market_id()):: {:ok, Enumerable.t(integer())} | {:error, text()}
  def market_cancel(id) do
    if :dets.member(:market_bets, id) do
      change_status(id, :cancelled)
      {_, bet_list} = market_bets(id)
      refund_cancel(bet_list)
      {:ok, bet_list}
    else
      {:error, "The market with id #{id} does not exist"}
    end
  end

  defp refund_cancel([]) do
    []
  end
  defp refund_cancel([head | tail]) do
    if :dets.member(:back_bets, head) do
      [{_, user_id, _, _, stake, _, _, _, _}] = :dets.lookup(:back_bets, head)
      [{_, username, balance}] = :dets.lookup(:users, user_id)
      :dets.insert(:users, {user_id, username, balance + stake})
    else
      [{_, user_id, _, _, stake, _, _, _, _}] = :dets.lookup(:lay_bets, head)
      [{_, username, balance}] = :dets.lookup(:users, user_id)
      :dets.insert(:users, {user_id, username, balance + stake})
    end
    refund_cancel(tail)
  end

  defp return_stakes([], acc) do acc end
  defp return_stakes([head | tail], acc) do
    if :dets.member(:back_bets, head) do
      [{_, _, _, _, stake, _, _, _, _}] = :dets.lookup(:back_bets, head)
      return_stakes(tail, acc ++ [stake])
    else
      if :dets.member(:lay_bets, head) do
        [{_, _, _, _, stake, _, _, _, _}] = :dets.lookup(:lay_bets, head)
        return_stakes(tail, acc ++ [stake])
      end
    end
  end

  defp change_status(id, status) do
    [{name, description, st}] = :dets.lookup(:markets, id)
    if st != status do
      :dets.insert(:markets, {name, description, status})
    end
  end

  # Function return all back bets in the market sorted in ascending order by ods
  @doc """
  Returns all back bets in the market sorted in ascending order by ods

  ## Parameters
    - `id`: The unique identifier for the market.

  ## Examples
      iex> {:ok, bets} = Betunfair.Actions.market_pending_backs("Market1")
  """

  @spec market_pending_backs(id :: market_id()) :: {:ok, Enumerable.t({integer(), bet_id()})}
  def market_pending_backs(id) do
    list = :dets.foldl(fn ({id_bet, _, _, market_id, _, _, odds, _, _}, acc) -> if market_id == id do [{odds, id_bet} | acc] else acc end end, [], :pending_back_bets)
    sorted_list = Enum.sort_by(list, fn {odds, _} -> odds end)
    {:ok, sorted_list}
  end


  # Function return all back bets in the market sorted in ascending order by ods
  @doc """
  Returns all lays bets in the market sorted in ascending order by ods.

  ## Parameters
    - `id`: The unique identifier for the market.

  ## Examples
      iex> {:ok, bets} = Betunfair.Actions.market_pending_lays("Market1")
  """

  @spec market_pending_lays(id :: market_id()) :: {:ok, Enumerable.t({integer(), bet_id()})}
  def market_pending_lays(id) do
    list = :dets.foldl(fn ({id_bet, _, _, market_id, _, _, odds, _, _}, acc) -> if market_id == id do [{odds, id_bet} | acc] else acc end end, [], :pending_lay_bets)
    sorted_list_reverse = Enum.sort_by(list, fn {odds, _} -> odds end)
    sorted_list = Enum.reverse(sorted_list_reverse)
    {:ok, sorted_list}
  end


  @doc """
  Freezes the specified market, preventing any further bets.

  ## Parameters
    - `id`: The unique identifier for the market.

  ## Examples
      iex> {:ok, bet_list} = Betunfair.Actions.Market.market_freeze("market1")
  """
  @spec market_freeze(id :: market_id()) :: :ok
  def market_freeze(id) do
    if :dets.member(:market_bets, id) do
      change_status(id, :frozen)
      {_, market_list} = market_bets(id)
      refund_freeze(market_list)
      {:ok, "The market is frozen"}
    else
      {:error, "The market doesn't exist"}
    end
  end

  defp refund_freeze([]) do
    {:ok, "The market is frozen"}
  end
  defp refund_freeze([head | tail]) do
      if :dets.member(:pending_back_bets, head) do
        [{_, user_id, _, _, _, remaining_back_stake, _, _, _}] = :dets.lookup(:pending_back_bets, head)
        [{_, username, balance}] = :dets.lookup(:users, user_id)
        :dets.insert(:users, {user_id, username, balance + remaining_back_stake})
        [{_, user_id, _, market_id, stake, remaining_stake, odds, match_list, status}] = :dets.lookup(:pending_back_bets, head)
        {:ok, "The market has been frozen"}
      else
        if :dets.member(:pending_lay_bets, head) do
          [{_, user_id, _, _, _, remaining_lay_stake, _, _, _}] = :dets.lookup(:pending_lay_bets, head)
          [{_, username, balance}] = :dets.lookup(:users, user_id)
          :dets.insert(:users, {user_id, username, balance + remaining_lay_stake})
          [{_, user_id, _, market_id, stake, remaining_stake, odds, match_list, status}] = :dets.lookup(:pending_lay_bets, head)
          {:ok, "The market has been frozen"}
        else
          if :dets.member(:back_bets, head) || :dets.member(:lay_bets, head) do
            {:ok, "This bet has already been matched"}
          else
            {:error, "The bet does not exist"}
          end
        end
      end
      refund_freeze(tail)
  end


  # Function to settle a market
  @doc """
  Settles the market based on the result.

  ## Parameters
    - `id`: The unique identifier for the market.
    - `result`: The result of the market (true if back wins, false if lay wins).

  ## Examples
      iex> {:ok, message} = Betunfair.Actions.Market.market_settle("Market1", true)
  """
  def market_settle(id, result) do
    change_status(id, {:settle, result})
    if result do #Gana el back
      #back = match_back + match_lay
      list = :dets.foldl(fn ({_, market_id, lay, back, _, _, match_lay, match_back}, acc) -> if market_id == id && :dets.member(:back_bets, back) do [{lay, back, match_lay, match_back} | acc] else acc end end, [], :matched_bets)
      back_wins(list)
    else #Gana el lay
      #lay = match_lay + match_back
      list = :dets.foldl(fn ({_, market_id, lay, back, _, _, match_lay, match_back}, acc) -> if market_id == id && :dets.member(:lay_bets, lay) do [{lay, back, match_lay, match_back} | acc] else acc end end, [], :matched_bets)
      lay_wins(list)
    end
  end

  defp back_wins([]) do
    {:ok, "The money was deposited into your account"}
  end
  defp back_wins([head | tail]) do
    #back_wins
    {lay, back, match_lay, match_back} = head

    [{_, user_id, _, _, back_stake, remaining_back_state, _, _, _}] = :dets.lookup(:back_bets, back)
    [{_, username, balance}] = :dets.lookup(:users, user_id)
    #balance = balance_b + match_lay + match_back
    {_, list} = user_bets(user_id)
    number = length(list)
    balance = remaining_back_state*number + round(match_lay)
    :dets.insert(:users, {user_id, username, balance})

    #lay_loses
    [{_, user_id, _, _, lay_stake, remaining_lay_state, _, _, _}] = :dets.lookup(:lay_bets, lay)
    [{_, username, balance}] = :dets.lookup(:users, user_id)
    balance = remaining_back_state*number - round(match_lay)
    :dets.insert(:users, {user_id, username, balance})

    back_wins(tail)
  end

  defp lay_wins([]) do
    {:ok, "The money was deposited into your account"}
  end
  defp lay_wins([head | tail]) do
    #lay_wins
    {lay, back, match_lay, match_back} = head
    IO.inspect(match_back)
    IO.inspect(match_lay)
    [{_, user_id_l, _, _, lay_stake, remaining_lay_stake, _, _, _}] = :dets.lookup(:lay_bets, lay)
    [{_, user_id_b, _, _, back_stake, remaining_back_stake, _, _, _}] = :dets.lookup(:back_bets, back)
    IO.inspect(remaining_back_stake)
    [{_, username, balance_b}] = :dets.lookup(:users, user_id_l)

    {_, list} = user_bets(user_id_l)
    number = length(list)
    IO.inspect(number)

    {_, list} = user_bets(user_id_l)
    number = length(list)
    balance = remaining_back_stake*number + round(match_back)
    IO.inspect(balance)
    :dets.insert(:users, {user_id_l, username, balance})

    #back_loses
    [{_, username, balance}] = :dets.lookup(:users, user_id_b)
    balance = remaining_back_stake*number - round(match_lay) - remaining_lay_stake
    :dets.insert(:users, {user_id_b, username, balance})
    lay_wins(tail)
  end

  # Function to retrieve information about a market
  @doc """
  Retrieves information about a market.

  ## Parameters
    - `id`: The unique identifier for the market.

  ## Examples
      iex> {:ok, market_info} = Betunfair.Actions.Market.market_get("Market1")
  """
  @spec market_get(id :: market_id()) :: {:ok, %{name: text(), description: text(), status: :active | :frozen | :cancelled | {:settled, result::bool()}}}
  def market_get(id) do
    if :dets.member(:markets, id) do
      [{_, description, status}] = :dets.lookup(:markets, id)
      {:ok, %{:name => id, :description => description, :status => status}}
    else
      {:error, "The market with id #{id} does not exist"}
    end
  end

  # Function to match bets in a market
  @doc """
  Matches bets in the market.

  ## Parameters
    - `id`: The unique identifier for the market.

  ## Examples
      iex> {:ok, message} = Betunfair.Actions.Market.market_match("Market1")
  """

  @spec market_match(id :: market_id()) :: :ok
  def market_match(id) do
    # Guardamos las listas de back y lay
    {_, back_list} = market_pending_backs(id)
    {_, lay_list} = market_pending_lays(id)

    # Recorremos las listas
    go_through(back_list, lay_list)
    {:ok, "The bets were matched"}
  end

  defp go_through([], _) do
    {:ok, "The bets were matched"}
  end
  defp go_through(_, []) do
    {:ok, "The bets were matched"}
  end
  defp go_through([head_b | tail_b], [head_l | tail_l]) do
    {_, back_list} = head_b
    {_, lay_list} = head_l

    {remaining_back_stake, remaining_lay_stake} = check(back_list, lay_list)

    cond do
      remaining_back_stake == 0 && remaining_lay_stake == 0 ->
        go_through(tail_b, tail_l)
      remaining_back_stake != 0 && remaining_lay_stake == 0 ->
        go_through([head_b | tail_b], tail_l)
      remaining_back_stake == 0 && remaining_lay_stake != 0 ->
        go_through(tail_b, [head_l | tail_l])
      true -> :error
    end
  end

  defp check(back, lay) do
    # Pillamos el stake y las odds de la apuesta correspondiente
    [{_, back_user_id, _, back_market_id, back_stake, _, back_odds, back_matched_list, back_status}] = :dets.lookup(:pending_back_bets, back)
    [{_, lay_user_id, _, lay_market_id, lay_stake, _, lay_odds, lay_matched_list, lay_status}] = :dets.lookup(:pending_lay_bets, lay)

    if back_odds <= lay_odds do
      if back_stake * (back_odds/100) - back_stake > lay_stake do # El lay se queda a 0
        match_back = lay_stake / ((lay_odds/100) - 1) #Lo que apuesta el back o beneficios lay
        match_lay = lay_stake #Beneficios del back o lo que apuesta el lay
        remaining_back_stake = back_stake - match_back

        list = :dets.foldl(fn ({match_id, _, _, _, _, _, _, _}, acc) -> acc ++ [match_id] end, [], :matched_bets)
        len = length(list) + 1
        match_id = "matched" <> Integer.to_string(len)

        :dets.insert(:pending_back_bets, {back, back_user_id, :back, back_market_id, back_stake, remaining_back_stake, back_odds, back_matched_list ++ [lay], back_status})
        :dets.delete(:pending_lay_bets, lay)
        :dets.insert(:matched_bets, {match_id, back_market_id, lay, back, lay_odds, back_odds, match_lay, match_back})
        {remaining_back_stake, 0}
      else
        if back_stake * (back_odds/100) - back_stake == lay_stake do # El lay y el back son 0
          match_back = back_stake
          match_lay = lay_stake

          list = :dets.foldl(fn ({match_id, _, _, _, _, _, _}, acc) -> acc ++ [match_id] end, [], :matched_bets)
          len = length(list) + 1
          match_id = "matched" <> Integer.to_string(len)

          :dets.delete(:pending_back_bets, back)
          :dets.delete(:pending_lay_bets, lay)
          :dets.insert(:matched_bets, {match_id, back_market_id, lay, back, lay_odds, back_odds, match_lay, match_back})
          {0, 0}
        else
          if back_stake * (back_odds/100) - back_stake < lay_stake do
            match_back = back_stake
            match_lay = back_stake * (back_odds/100) - back_stake
            remaining_lay_stake = lay_stake - match_lay

            list = :dets.foldl(fn ({match_id, _, _, _, _, _, _, _}, acc) -> acc ++ [match_id] end, [], :matched_bets)
            len = length(list) + 1
            match_id = "matched" <> Integer.to_string(len)


            :dets.insert(:pending_lay_bets, {lay, lay_user_id, :lay, lay_market_id, lay_stake, remaining_lay_stake, lay_odds, lay_matched_list ++ [back], lay_status})
            :dets.delete(:pending_back_bets, back)
            :dets.insert(:matched_bets, {match_id, back_market_id, lay, back, lay_odds, back_odds, match_lay, match_back})
            {0, remaining_lay_stake}
          else
            {back_stake, lay_stake}
          end
        end
      end
    else
      go_through([], lay_stake)
    end
  end


end

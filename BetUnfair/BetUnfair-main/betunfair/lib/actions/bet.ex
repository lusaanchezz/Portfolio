defmodule Betunfair.Actions.Bet do
  @type bet_id() :: String.t()
  @type user_id() :: String.t()
  @type market_id() :: String.t()
  @type text() :: String.t()

    @doc """
  Places a back bet on the specified market for the specified user.

  ## Parameters
    - `user_id`: The unique identifier for the user placing the bet.
    - `market_id`: The unique identifier for the market where the bet is placed.
    - `stake`: The amount of money to bet.
    - `odds`: The odds for the bet.

  ## Examples
      iex> {:ok, bet_id} = Betunfair.Actions.Bet.bet_back("user1", "market1", 100, 3)
  """
  @spec bet_back(user_id :: user_id(), market_id :: market_id(), stake :: integer(), odds :: integer()) :: {:ok, bet_id()} | {:error, text()}
  def bet_back(user_id, market_id, stake, odds) do
    if :dets.member(:user_bets, user_id) do
      if :dets.member(:market_bets, market_id) do
        [{_, _, status}] = :dets.lookup(:markets, market_id)
        if status != :frozen do
          #Modificamos el balance del usuario cuando hace una apuesta
          [{_, username, balance}] = :dets.lookup(:users, user_id)
          balance = balance - stake
          :dets.insert(:users, {user_id, username, balance})
          list = :dets.foldl(fn ({id_bet, _, _, _, _, _, _, _, _}, acc) -> acc ++ [id_bet] end, [], :back_bets)
          remaining_stake = stake
          len = length(list) + 1
          id_bet = "bb" <> Integer.to_string(len)

          :dets.insert(:back_bets, {id_bet, user_id, :back, market_id, stake, remaining_stake, odds, [], :active})
          :dets.insert(:pending_back_bets, {id_bet, user_id, :back, market_id, stake, remaining_stake, odds, [], :active})
          [{_, list_user}] = :dets.lookup(:user_bets, user_id)
          :dets.insert(:user_bets, {user_id, list_user ++ [id_bet]})
          [{_, list_market}] = :dets.lookup(:market_bets, market_id)
          :dets.insert(:market_bets, {market_id, list_market ++ [id_bet]})
          {:ok, id_bet}
        else
          {:error, "The status of the market ain't active"}
        end
      else
        {:error, "This market does not exist"}
      end
    else
      {:error, "This user does not exist"}
    end
  end

  @doc """
  Places a lay bet on the specified market for the specified user.

  ## Parameters
    - `user_id`: The unique identifier for the user placing the bet.
    - `market_id`: The unique identifier for the market where the bet is placed.
    - `stake`: The amount of money to bet.
    - `odds`: The odds for the bet.

  ## Examples
      iex> {:ok, bet_id} = Betunfair.Actions.Bet.bet_lay("user1", "market1", 100, 3)
  """

  @spec bet_lay(user_id :: user_id(), market_id :: market_id(), stake :: integer(), odds :: integer()) :: {:ok, bet_id()} | {:error, text()}
  def bet_lay(user_id, market_id, stake, odds) do
    if :dets.member(:user_bets, user_id) do
      if :dets.member(:market_bets, market_id) do
        [{_, _, status}] = :dets.lookup(:markets, market_id)
        if status != :frozen do

          #Modificamos el balance del usuario cuando hace una apuesta
          [{_, username, balance}] = :dets.lookup(:users, user_id)
          balance = balance - stake
          :dets.insert(:users, {user_id, username, balance})

          list = :dets.foldl(fn ({id_bet, _, _, _, _, _, _, _, _}, acc) -> acc ++ [id_bet] end, [], :lay_bets)
          remaining_stake = stake
          len = length(list) + 1
          id_bet = "bl" <> Integer.to_string(len)

          :dets.insert(:lay_bets, {id_bet, user_id, :lay, market_id, stake, remaining_stake, odds, [], :active})
          :dets.insert(:pending_lay_bets, {id_bet, user_id, :lay, market_id, stake, remaining_stake, odds, [], :active})
          [{_, list_user}] = :dets.lookup(:user_bets, user_id)
          :dets.insert(:user_bets, {user_id, list_user ++ [id_bet]})
          [{_, list_market}] = :dets.lookup(:market_bets, market_id)
          :dets.insert(:market_bets, {market_id, list_market ++ [id_bet]})
          {:ok, id_bet}
        else
          {:error, "The status of the market ain't active"}
        end
      else
        {:error, "This market does not exist"}
      end
    else
      {:error, "This user does not exist"}
    end
  end

  @doc """
  Retrieves information about a bet.

  ## Parameters
    - `id`: The unique identifier for the bet.

  ## Examples
      iex> {:ok, bet_info} = Betunfair.Actions.Bet.bet_get("bet1")
  """
  @spec bet_get(id :: bet_id()) :: {:ok, %{bet_type: :back | :lay, market_id: market_id(), user_id: user_id(), odds: integer(), original_stake: integer(),  remaining_stake: integer(), matched_bets: Enumerable.t(bet_id()),  status: :active | :cancelled | :market_cancelled | {:market_settled, boolean()}}}
  def bet_get(id) do
    if :dets.member(:pending_back_bets, id) do
      [{_, user_id, bet_type, market_id, original_stake, remaining_stake, odds, matched_bets, status}] = :dets.lookup(:pending_back_bets, id)
      #{:ok, %{:stake => round(remaining_stake)}}
      {:ok, %{:user_id => user_id, :bet_type => bet_type, :market_id => market_id, :original_stake => original_stake,  :stake => round(remaining_stake), :odds => odds, :matched_bets => matched_bets, :status => status}}
    else
      if :dets.member(:pending_lay_bets, id) do
        [{_, user_id, bet_type, market_id, original_stake, remaining_stake, odds, matched_bets, status}] = :dets.lookup(:pending_lay_bets, id)
        #{:ok, %{:stake =>  round(remaining_stake)}}
        {:ok, %{:user_id => user_id, :bet_type => bet_type, :market_id => market_id, :original_stake => original_stake,  :stake => round(remaining_stake), :odds => odds, :matched_bets => matched_bets, :status => status}}
      else
        if :dets.member(:back_bets, id) || :dets.member(:lay_bets, id) do
          {:ok, %{:stake => 0}}
        else
          {:error, "The bet with id #{id} does not exist"}
        end
      end
    end
  end

  @doc """
  Cancels a bet.

  ## Parameters
    - `id`: The unique identifier for the bet to cancel.

  ## Examples
      iex> Betunfair.Actions.Bet.bet_cancel("bet1")
  """
  @spec bet_cancel(id :: bet_id()) :: {:ok, String.t()} | {:error, String.t()}
  def bet_cancel(id_bet) do
    if :dets.member(:pending_back_bets, id_bet) do
      [{_, user_id, _, _, _, remaining_back_stake, _, _, _}] = :dets.lookup(:pending_back_bets, id_bet)
      [{_, username, balance}] = :dets.lookup(:users, user_id)
      :dets.insert(:users, {user_id, username, balance + remaining_back_stake})
      [{_, user_id, _, market_id, stake, remaining_stake, odds, match_list, status}] = :dets.lookup(:pending_back_bets, id_bet)
      if status != :cancelled do
        :dets.insert(:pending_back_bets, {id_bet, user_id, :back, market_id, stake, remaining_stake, odds, match_list, :cancelled})
      end
      {:ok, "The bet has been cancelled"}
    else
      if :dets.member(:pending_lay_bets, id_bet) do
        [{_, user_id, _, _, _, remaining_lay_stake, _, _, _}] = :dets.lookup(:pending_lay_bets, id_bet)
        [{_, username, balance}] = :dets.lookup(:users, user_id)
        :dets.insert(:users, {user_id, username, balance + remaining_lay_stake})
        [{_, user_id, _, market_id, stake, remaining_stake, odds, match_list, status}] = :dets.lookup(:pending_lay_bets, id_bet)
        if status != :cancelled do
          :dets.insert(:pending_lay_bets, {id_bet, user_id, :lay, market_id, stake, remaining_stake, odds, match_list, :cancelled})
        end
        {:ok, "The bet has been cancelled"}
      else
        if :dets.member(:back_bets, id_bet) || :dets.member(:lay_bets, id_bet) do
          {:ok, "This bet has already been matched"}
        else
          {:error, "The bet does not exist"}
        end
      end
    end
  end


end

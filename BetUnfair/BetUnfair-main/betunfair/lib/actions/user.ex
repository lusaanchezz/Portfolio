defmodule Betunfair.Actions.User do
  @type user_id() :: String.t()
  @type text() :: String.t()
  @type bet_id() :: String.t()

 @doc """
  Creates a new user with the given parameters.

  ## Parameters
    - `id`: The unique identifier for the user.
    - `username`: The username for the user.

  ## Examples
      iex> {:ok, user_id} = Betunfair.Actions.User.user_create("user1", "Manuel Martinez")
  """
  @spec user_create(id :: user_id(), username :: user_id()) :: {:ok, user_id()} | {:error, text()}
  def user_create(id, username) do
    if :dets.member(:users, id) do
      {:error, "This user id already exists"}
    else
      :dets.insert(:users, {id, username, 0})
      :dets.insert(:user_bets, {id, []})
      {:ok, id}
    end
  end

  @doc """
  Deposits funds into the user's account.

  ## Parameters
    - `id`: The unique identifier for the user.
    - `amount`: The amount of money to deposit.

  ## Examples
      iex> {:ok, message} = Betunfair.Actions.User.user_deposit("user1", 100)
  """
  @spec user_deposit(id :: user_id(), amount :: integer()) :: {:ok, text()} | {:error, text()}
  def user_deposit(id, amount) do
    #IO.inspect(:dets.foldl(fn ({_, "paco", "hola"}, acc) ->
                         # Map.put(acc, id, "username", "balance") end, %{}, :users))

    if amount > 0 do
      if :dets.member(:users, id) do
        [{_, username, _}] = :dets.lookup(:users, id)
        :dets.insert(:users, {id, username, amount})
        {:ok, "User #{username} has deposited the amount of #{amount}"}
      else
        {:error, "The user with id #{id} does not exist"}
      end
    else
      {:error, "The amount of money deposited must be higher than 0"}
    end
  end

  @doc """
  Withdraws funds from the user's account.

  ## Parameters
    - `id`: The unique identifier for the user.
    - `amount`: The amount of money to withdraw.

  ## Examples
      iex> {:ok, message} = Betunfair.Actions.User.user_withdraw("user1", 50)
  """

  @spec user_withdraw(id :: user_id(), amount :: integer()) :: {:ok, text()} | {:error, text()}
  def user_withdraw(id, amount) do
    if amount > 0 do
      if :dets.member(:users, id) do
        [{_, username, balance}] = :dets.lookup(:users, id)
        if balance >= amount do
          sol = balance - amount
          :dets.insert(:users, {id, username, sol})
          {:ok, "User #{username} has withdrawn the amount of #{amount}"}
        else
          {:error, "The balance of your account is insufficient"}
        end
      else
        {:error, "The user with id #{id} does not exist"}
      end
    else
      {:error, "The amount of money withdrawn must be higher than 0"}
    end
  end

  @doc """
  Retrieves information about a user.

  ## Parameters
    - `id`: The unique identifier for the user.

  ## Examples
      iex> {:ok, user_info} = Betunfair.Actions.User.user_get("user1")
  """

  @spec user_get(id :: user_id()) :: {:ok, %{name: text(), id: user_id(), balance: integer()}}
  def user_get(id) do
    if :dets.member(:users, id) do
      [{_, username, balance}] = :dets.lookup(:users, id)
      {:ok, %{:name => username, :id => id, :balance => balance}}
    else
      {:error, "The user with id #{id} does not exist"}
    end
  end

  @doc """
  Retrieves all bets placed by the user.

  ## Parameters
    - `id`: The unique identifier for the user.

  ## Examples
      iex> {:ok, bets} = Betunfair.Actions.User.user_bets("user1")
  """

  @spec user_bets(id :: user_id()):: {:ok, Enumerable.t(bet_id())}
  def user_bets(id) do
    if :dets.member(:user_bets, id) do
      [{_, list}] = :dets.lookup(:user_bets, id)
      {:ok, list}
    else
      {:error, "The user with id #{id} does not exist"}
    end
  end
end

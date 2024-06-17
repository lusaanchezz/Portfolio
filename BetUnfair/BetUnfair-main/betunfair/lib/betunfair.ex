defmodule Betunfair do
  @moduledoc """
  Documentation for `Betunfair`.
  """
  use GenServer, restart: :permanent

  @doc """
  start_link

  ## Examples

      iex> Betunfair.hello()
      :world

  """
  def start_link(name) do
    :dets.open_file(:users, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:user_bets, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:markets, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:market_bets, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:back_bets, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:lay_bets, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:pending_back_bets, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:pending_lay_bets, [{:type, :set}, {:keypos, 1}])
    :dets.open_file(:matched_bets, [{:type, :set}, {:keypos, 1}])
    GenServer.start_link(__MODULE__, %{}, name: Betunfair)
  end

  def stop() do
    :dets.close(:users)
    :dets.close(:user_bets)
    :dets.close(:markets)
    :dets.close(:market_bets)
    :dets.close(:back_bets)
    :dets.close(:lay_bets)
    :dets.close(:matched_bets)
  end

  # User
  def user_create(id, username) do
    GenServer.call(__MODULE__, {:user_create, id, username})
  end

  def user_deposit(id, amount) do
    GenServer.call(__MODULE__, {:user_deposit, id, amount})
  end

  def user_withdraw(id, amount) do
    GenServer.call(__MODULE__, {:user_withdraw, id, amount})
  end

  def user_get(id) do
    GenServer.call(__MODULE__, {:user_get, id})
  end

  def user_bets(id) do
    GenServer.call(__MODULE__, {:user_bets, id})
  end

  # Market interaction
  def market_create(name, description) do
    GenServer.call(__MODULE__, {:market_create, name, description})
  end

  def market_list() do
    GenServer.call(__MODULE__, {:market_list})
  end

  def market_list_active() do
    GenServer.call(__MODULE__, {:market_list_active})
  end

  def market_bets(id) do
    GenServer.call(__MODULE__, {:market_bets, id})
  end

  def market_pending_backs(id) do
    GenServer.call(__MODULE__, {:market_pending_backs, id})
  end

  def market_cancel(id) do
    GenServer.call(__MODULE__, {:market_cancel, id})
  end

  def market_settle(id, result) do
    GenServer.call(__MODULE__, {:market_settle, id, result})
  end

  def market_match(id) do
    GenServer.call(__MODULE__, {:market_match, id})
  end

  def market_pending_lays(id) do
    GenServer.call(__MODULE__, {:market_pending_lays, id})
  end

  def market_freeze(id) do
    GenServer.call(__MODULE__, {:market_freeze, id})
  end

  def market_get(id) do
    GenServer.call(__MODULE__, {:market_get, id})
  end

  # Bet interaction
  def bet_back(user_id, market_id, stake, odds) do
    GenServer.call(__MODULE__, {:bet_back, user_id, market_id, stake, odds})
  end

  def bet_lay(user_id, market_id, stake, odds) do
    GenServer.call(__MODULE__, {:bet_lay, user_id, market_id, stake, odds})
  end

  def bet_cancel(id_bet) do
    GenServer.call(__MODULE__, {:bet_cancel, id_bet})
  end

  def bet_get(id_bet) do
    GenServer.call(__MODULE__, {:bet_get, id_bet})
  end

  # Callbacks
  @impl GenServer
  def init(state) do
    {:ok, state}
  end

  @impl GenServer
  def handle_call({:user_create, id, username}, _from, state) do
    sol = Betunfair.Actions.User.user_create(id, username)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:user_deposit, id, amount}, _from, state) do
    sol = Betunfair.Actions.User.user_deposit(id, amount)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:user_withdraw, id, amount}, _from, state) do
    sol = Betunfair.Actions.User.user_withdraw(id, amount)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:user_get, id}, _from, state) do
    sol = Betunfair.Actions.User.user_get(id)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:user_bets, id}, _from, state) do
    sol = Betunfair.Actions.User.user_bets(id)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_create, name, description}, _from, state) do
    sol = Betunfair.Actions.Market.market_create(name, description)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_list}, _from, state) do
    sol = Betunfair.Actions.Market.market_list()
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_list_active}, _from, state) do
    sol = Betunfair.Actions.Market.market_list_active()
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_bets, id}, _from, state) do
    sol = Betunfair.Actions.Market.market_bets(id)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_pending_backs, id}, _from, state) do
    sol = Betunfair.Actions.Market.market_pending_backs(id)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:bet_back, user_id, market_id, stake, odds}, _from, state) do
    sol = Betunfair.Actions.Bet.bet_back(user_id, market_id, stake, odds)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:bet_lay, user_id, market_id, stake, odds}, _from, state) do
    sol = Betunfair.Actions.Bet.bet_lay(user_id, market_id, stake, odds)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:bet_cancel, id_bet}, _from, state) do
    sol = Betunfair.Actions.Bet.bet_cancel(id_bet)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_cancel, id_market}, _from, state) do
    sol = Betunfair.Actions.Market.market_cancel(id_market)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_settle, id_market, result}, _from, state) do
    sol = Betunfair.Actions.Market.market_settle(id_market, result)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_match, id_market}, _from, state) do
    sol = Betunfair.Actions.Market.market_match(id_market)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_pending_lays, id}, _from, state) do
    sol = Betunfair.Actions.Market.market_pending_lays(id)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_freeze, id}, _from, state) do
    sol = Betunfair.Actions.Market.market_freeze(id)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:bet_get, id}, _from, state) do
    sol = Betunfair.Actions.Bet.bet_get(id)
    {:reply, sol, state}
  end

  @impl GenServer
  def handle_call({:market_get, id}, _from, state) do
    sol = Betunfair.Actions.Market.market_get(id)
    {:reply, sol, state}
  end
end

defmodule Betunfair.Supervisor do
  use Supervisor

  def start_link(name) do
    Supervisor.start_link(__MODULE__, name, name: __MODULE__)
  end

  @impl true
  def init(_arg) do
    children = [
      {Betunfair, name: Betunfair}
    ]

    Supervisor.init(children, strategy: :one_for_one)
  end
end

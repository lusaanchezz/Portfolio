defmodule BetunfairTest do
  use ExUnit.Case

  test "test general" do
    Betunfair.start_link(:casa1)
    assert {:ok, "1"} = Betunfair.user_create("1", "manolo")
    assert {:ok, "2"} = Betunfair.user_create("2", "lucia")
    assert {:ok, "3"} = Betunfair.user_create("3", "julio")
    assert {:ok, _} = Betunfair.user_deposit("1", 2000)
    assert {:ok, _} = Betunfair.user_withdraw("1", 500)
    assert {:ok, _} = Betunfair.user_deposit("2", 2000)
    assert {:ok, _} = Betunfair.user_withdraw("2", 500)
    assert {:ok, _} = Betunfair.user_deposit("3", 2000)
    assert {:ok, _} = Betunfair.user_withdraw("3", 500)
    assert {:ok, %{:balance => 1500}} = Betunfair.user_get("1")
    assert {:ok, []} = Betunfair.user_bets("1")

    assert {:ok, m1} = Betunfair.market_create("Legazpi", "Legazpi vs Julio")
    assert {:ok, m2} = Betunfair.market_create("Ventas", "Ventas vs Manolo")
    assert {:ok, m3} = Betunfair.market_create("RMW", "Real Madrid gana")
    assert {:ok, m4} = Betunfair.market_create("FCW", "Barça gana")
    assert {:ok, [m1, m2, m3, m4]} = Betunfair.market_list()
    assert {:ok, [m1, m2, m3, m4]} = Betunfair.market_list_active()
    assert {:ok, []} = Betunfair.market_bets("Legazpi")

    assert {:ok, bb1} = Betunfair.bet_back("1", "Ventas", 2000, 150)
    assert {:ok, bb2} = Betunfair.bet_back("2", "Legazpi", 2000, 100)
    assert {:ok, bb3} = Betunfair.bet_back("3", "Ventas", 2000, 200)
    assert {:ok, [bb1, bb3]} = Betunfair.market_bets("Ventas")
    assert {:ok, [bb2]} = Betunfair.market_bets("Legazpi")

    assert {:ok, bl1} = Betunfair.bet_lay("1", "Legazpi", 2000, 150)
    assert {:ok, bl2} = Betunfair.bet_lay("2", "Legazpi", 2000, 100)
    assert {:ok, bl3} = Betunfair.bet_lay("3", "Legazpi", 2000, 200)
    assert {:ok, ["bb2", "bl1", "bl2", "bl3"]} = Betunfair.market_bets("Legazpi")
    assert {:ok, [bb1, bl1]} = Betunfair.user_bets("1")

    assert {:ok, [{150, bb1}, {200, bb3}]} = Betunfair.market_pending_backs("Ventas")
    assert {:ok, [{200, bl3}, {150, bl1}, {100, bl2}]} = Betunfair.market_pending_lays("Legazpi")
    assert {:ok, %{:name => "Ventas", :description => "Ventas vs Manolo",
    :status => :active}} = Betunfair.market_get("Ventas")

    assert {:ok,u1} = Betunfair.user_create("u1","Francisco Gonzalez")
    assert {:ok,u2} = Betunfair.user_create("u2","Maria Fernandez")
    assert {:ok,_} = (Betunfair.user_deposit("u1",2000))
    assert {:ok,_} = (Betunfair.user_deposit("u2",2000))
    assert {:ok,%{:name => "Francisco Gonzalez", :id => "u1", :balance => 2000}} = Betunfair.user_get("u1")
    assert {:ok,m1} = Betunfair.market_create("rmw", "Real Madrid wins")
    assert {:ok, bb1} = Betunfair.bet_back(u1,m1,1000,150)
    assert {:ok, bb2} = Betunfair.bet_back(u2, m1,1000,153)
    assert {:ok,bl1} = Betunfair.bet_lay(u1,m1,100,140)
    assert {:ok,bl2} = Betunfair.bet_lay(u2,m1,100,150)
    assert {:ok,_} = (Betunfair.market_match(m1))
    assert {:ok,%{stake: 800}} = Betunfair.bet_get(bb1)

    :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
    Betunfair.stop()
  end
'''
  test "match_bets4" do
    assert  {:ok,_} = Betunfair.start_link("testdb")
    assert {:ok,u1} = Betunfair.user_create("u1","Francisco Gonzalez")
    assert {:ok,u2} = Betunfair.user_create("u2","Maria Fernandez")
    assert {:ok,_} = (Betunfair.user_deposit(u1,2000))
    assert {:ok,_} = (Betunfair.user_deposit(u2,2000))
    assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
    assert {:ok,m1} = Betunfair.market_create("rmw","Real Madrid wins")
    assert {:ok,bb1} = Betunfair.bet_back(u1,m1,1000,150)
    assert {:ok,bb2} = Betunfair.bet_back(u1,m1,1000,153)
    assert {:ok,%{balance: 0}} = Betunfair.user_get(u1)
    assert true = (bb1 != bb2)
    assert {:ok,bl1} = Betunfair.bet_lay(u2,m1,100,140)
    assert {:ok,bl2} = Betunfair.bet_lay(u2,m1,100,150)
    assert {:ok,%{balance: 1800}} = Betunfair.user_get(u2)
    assert {:ok,_} = (Betunfair.market_cancel(m1))
    assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
    assert {:ok,%{balance: 2000}} = Betunfair.user_get(u2)

    :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
    Betunfair.stop()
  end

  test "match_bets7" do
    assert  {:ok,_} = Betunfair.start_link("testdb")
    assert {:ok,u1} = Betunfair.user_create("u1","Francisco Gonzalez")
    assert {:ok,u2} = Betunfair.user_create("u2","Maria Fernandez")
    assert {:ok,_} = (Betunfair.user_deposit(u1,2000))
    assert {:ok,_} = (Betunfair.user_deposit(u2,2000))
    assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
    assert {:ok,m1} = Betunfair.market_create("rmw","Real Madrid wins")
    assert {:ok,bb1} = Betunfair.bet_back(u1,m1,1000,150)
    assert {:ok,bb2} = Betunfair.bet_back(u1,m1,1000,153)
    assert {:ok,%{balance: 0}} = Betunfair.user_get(u1)
    assert true = (bb1 != bb2)
    assert {:ok,bl1} = Betunfair.bet_lay(u2,m1,100,140)
    assert {:ok,bl2} = Betunfair.bet_lay(u2,m1,100,150)
    assert {:ok,%{balance: 1800}} = Betunfair.user_get(u2)
    assert {:ok,_} = (Betunfair.market_freeze(m1))
    assert {:error,_} = (Betunfair.bet_lay(u2,m1,100,150))
    assert {:ok,%{balance: 1800}} = Betunfair.user_get(u1)
    assert {:ok,%{balance: 2200}} = Betunfair.user_get(u2)

    :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
    Betunfair.stop()
  end

  test "match_bets2" do
    assert  {:ok,_} = Betunfair.start_link("testdb")
    assert {:ok,u1} = Betunfair.user_create("u1","Francisco Gonzalez")
    assert {:ok,u2} = Betunfair.user_create("u2","Maria Fernandez")
    assert {:ok,_} = (Betunfair.user_deposit(u1,2000))
    assert {:ok,_} = (Betunfair.user_deposit(u2,2000))
    assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
    assert {:ok,m1} = Betunfair.market_create("rmw","Real Madrid wins")
    assert {:ok,bb1} = Betunfair.bet_back(u1,m1,1000,150)
    assert {:ok,bb2} = Betunfair.bet_back(u1,m1,1000,153)
    assert {:ok,%{balance: 0}} = Betunfair.user_get(u1)
    assert true = (bb1 != bb2)
    assert {:ok,bl1} = Betunfair.bet_lay(u2,m1,1000,140)
    assert {:ok,bl2} = Betunfair.bet_lay(u2,m1,1000,150)
    assert {:ok,%{balance: 0}} = Betunfair.user_get(u2)
    assert {:ok,_} = (Betunfair.market_match(m1))
    assert {:ok,%{stake: 0}} = Betunfair.bet_get(bb1)
    assert {:ok,%{stake: 500}} = Betunfair.bet_get(bl2)

    :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
    Betunfair.stop
  end

test "match_bets3" do
  assert  {:ok,_} = Betunfair.start_link("testdb")
  assert {:ok,u1} = Betunfair.user_create("u1","Francisco Gonzalez")
  assert {:ok,u2} = Betunfair.user_create("u2","Maria Fernandez")
  assert {:ok,_} = (Betunfair.user_deposit(u1,2000))
  assert {:ok,_} = (Betunfair.user_deposit(u2,2000))
  assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
  assert {:ok,m1} = Betunfair.market_create("rmw","Real Madrid wins")
  assert {:ok,bb1} = Betunfair.bet_back(u1,m1,1000,150)
  assert {:ok,bb2} = Betunfair.bet_back(u1,m1,1000,153)
  assert {:ok,%{balance: 0}} = Betunfair.user_get(u1)
  assert true = (bb1 != bb2)
  assert {:ok,bl1} = Betunfair.bet_lay(u2,m1,100,140)
  assert {:ok,bl2} = Betunfair.bet_lay(u2,m1,100,150)
  assert {:ok,%{balance: 1800}} = Betunfair.user_get(u2)
  assert {:ok,_} = (Betunfair.market_match(m1))
  assert {:ok,%{stake: 800}} = Betunfair.bet_get(bb1)
  assert {:ok,%{stake: 0}} = Betunfair.bet_get(bl2)
  assert {:ok,user_bets} = Betunfair.user_bets(u1)
  assert 2 = length(user_bets)

  :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
  Betunfair.stop
end

test "match_bets5" do
  assert  {:ok,_} = Betunfair.start_link("testdb")
  assert {:ok,u1} = Betunfair.user_create("u1","Francisco Gonzalez")
  assert {:ok,u2} = Betunfair.user_create("u2","Maria Fernandez")
  assert {:ok,_} = (Betunfair.user_deposit(u1,2000))
  assert {:ok,_} = (Betunfair.user_deposit(u2,2000))
  assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
  assert {:ok,m1} = Betunfair.market_create("rmw","Real Madrid wins")
  assert {:ok,bb1} = Betunfair.bet_back(u1,m1,1000,150)
  assert {:ok,bb2} = Betunfair.bet_back(u1,m1,1000,153)
  assert {:ok,%{balance: 0}} = Betunfair.user_get(u1)
  assert true = (bb1 != bb2)
  assert {:ok,_bl1} = Betunfair.bet_lay(u2,m1,100,140)
  assert {:ok,_bl2} = Betunfair.bet_lay(u2,m1,100,150)
  assert {:ok,%{balance: 1800}} = Betunfair.user_get(u2)
  assert {:ok,_} = (Betunfair.market_match(m1))
  assert {:ok,_} = (Betunfair.market_settle(m1,true))
  assert {:ok,%{balance: 2100}} = Betunfair.user_get(u1)
  assert {:ok,%{balance: 1900}} = Betunfair.user_get(u2)

  :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
  Betunfair.stop
end

test "bet_cancel1" do
  assert  {:ok,_} = Betunfair.start_link("testdb")
  assert {:ok,u1} = Betunfair.user_create(new_user("u1"),"Francisco Gonzalez")
  assert {:ok,u2} = Betunfair.user_create(new_user("u2"),"Maria Fernandez")
  assert {:ok,_} = (Betunfair.user_deposit(u1,2000))
  assert {:ok,_} = (Betunfair.user_deposit(u2,2000))
  assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
  assert {:ok,m1} = Betunfair.market_create(new_market("rmw"),"Real Madrid wins")
  assert {:ok,bb1} = Betunfair.bet_back(u1,m1,1000,150)
  assert {:ok,bb2} = Betunfair.bet_back(u1,m1,1000,153)
  assert {:ok,%{balance: 0}} = Betunfair.user_get(u1)
  assert true = (bb1 != bb2)
  assert {:ok,bl1} = Betunfair.bet_lay(u2,m1,100,140)
  assert {:ok,bl2} = Betunfair.bet_lay(u2,m1,100,150)
  assert {:ok,%{balance: 1800}} = Betunfair.user_get(u2)
  assert {:ok,_} = (Betunfair.market_match(m1))
  assert {:ok,_} = (Betunfair.bet_cancel(bl1))
  assert {:ok,_} = (Betunfair.bet_cancel(bb2))
  assert {:ok,%{balance: 1000}} = Betunfair.user_get(u1)
  assert {:ok,%{balance: 1900}} = Betunfair.user_get(u2)
  assert {:ok,_} = (Betunfair.bet_cancel(bl2))
  assert {:ok,_} = (Betunfair.bet_cancel(bb1))
  assert {:ok,%{balance: 1800}} = Betunfair.user_get(u1)
  assert {:ok,%{balance: 1900}} = Betunfair.user_get(u2)

  :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
  Betunfair.stop
end

test "match_bets6" do
  assert  {:ok,_} = Betunfair.start_link("testdb")
  assert {:ok,u1} = Betunfair.user_create(new_user("u1"),"Francisco Gonzalez")
  assert {:ok,u2} = Betunfair.user_create(new_user("u2"),"Maria Fernandez")
  assert {:ok,_} = (Betunfair.user_deposit(u1,2000))
  assert {:ok,_} = (Betunfair.user_deposit(u2,2000))
  assert {:ok,%{balance: 2000}} = Betunfair.user_get(u1)
  assert {:ok,m1} = Betunfair.market_create(new_market("rmw"),"Real Madrid wins")
  assert {:ok,bb1} = Betunfair.bet_back(u1,m1,1000,150)
  assert {:ok,bb2} = Betunfair.bet_back(u1,m1,1000,153)
  assert {:ok,%{balance: 0}} = Betunfair.user_get(u1)
  assert true = (bb1 != bb2)
  assert {:ok,_bl1} = Betunfair.bet_lay(u2,m1,100,140)
  assert {:ok,_bl2} = Betunfair.bet_lay(u2,m1,100,150)
  assert {:ok,%{balance: 1800}} = Betunfair.user_get(u2)
  assert {:ok,_} = (Betunfair.market_match(m1))
  assert {:ok,_} = (Betunfair.market_settle(m1,false))
  assert {:ok,%{balance: 1800}} = Betunfair.user_get(u1)
  assert {:ok,%{balance: 2200}} = Betunfair.user_get(u2)

  :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
  Betunfair.stop
end

test "betcancel1" do
  assert  {:ok,} = Betunfair.Supervisor.start_link(:a)
  assert {:ok,u1} = Betunfair.user_create(new_user("u1"),"Francisco Gonzalez")
  assert {:ok,u2} = Betunfair.user_create(new_user("u2"),"Maria Fernandez")

  :dets.delete_all_objects(:users)
    :dets.delete_all_objects(:user_bets)
    :dets.delete_all_objects(:markets)
    :dets.delete_all_objects(:market_bets)
    :dets.delete_all_objects(:back_bets)
    :dets.delete_all_objects(:lay_bets)
    :dets.delete_all_objects(:pending_back_bets)
    :dets.delete_all_objects(:pending_lay_bets)
  Betunfair.stop
end
'''
test "betcancel2" do
  assert  {:ok,_} = Betunfair.Supervisor.start_link(:a)
  assert {:ok,u1} = Betunfair.user_create(new_user("u1"),"Francisco Gonzalez")
  assert {:ok,u2} = Betunfair.user_create(new_user("u2"),"Maria Fernandez")
  Betunfair.stop
end

defp new_market(prefix), do: new_unique_id(prefix)
  defp new_user(prefix), do: new_unique_id(prefix)
  defp new_unique_id(prefix) do
    prefix <> "_" <> NaiveDateTime.to_iso8601(NaiveDateTime.utc_now)
  end
end

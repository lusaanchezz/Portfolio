#!/bin/bash

rm users
rm user_bets
rm markets
rm market_bets
rm lay_bets
rm back_bets
rm pending_lay_bets
rm pending_back_bets
rm matched_bets

echo "Borrado"
mix test

package com.github.wakingrufus.elo

data class LeagueState(val league: League,
                       val players: Map<String, Player> = HashMap(),
                       val history: List<RatingHistoryItem> = ArrayList())
package com.github.wakingrufus.elo

fun calculateNewLeague(league: League, games: List<Game>): LeagueState {
    var leagueState = LeagueState(league = league)
    games.stream()
            .sorted({ g1: Game, g2: Game -> (g2.entryDate.epochSecond - g1.entryDate.epochSecond).toInt() })
            .forEach { leagueState = addGameToLeague(leagueState, it) }
    return leagueState
}

fun addGameToLeague(leagueState: LeagueState, game: Game): LeagueState {
    var newLeagueState = leagueState.copy(
            players = addNewPlayers(
                    existingPlayers = leagueState.players,
                    game = game,
                    startingRating = leagueState.league.startingRating))
    val changes = calculateChanges(
            league = newLeagueState.league,
            players = newLeagueState.players,
            game = game)
    newLeagueState = newLeagueState.copy(players = applyChanges(players = newLeagueState.players, changes = changes))
    newLeagueState = newLeagueState.copy(history = leagueState.history + changes)
    return newLeagueState
}
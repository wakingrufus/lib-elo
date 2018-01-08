package com.github.wakingrufus.elo

import java.util.*

fun calculateNewLeague(league: League, games: List<Game>): LeagueState {
    return games.toList()
            .sortedWith(Comparator.comparing(Game::entryDate))
            .fold(initial = LeagueState(league = league)) { leagueState, game ->
                addGameToLeague(leagueState, game)
            }
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
package com.github.wakingrufus.elo


fun calculateTeamStartingRating(players: List<Player>): Int {
    var teamStartingRating = 0
    players.forEach { teamStartingRating += it.currentRating }
    return teamStartingRating / players.size
}

fun calculateTeamAverageGamesPlayed(players: List<Player>): Int {
    var teamGamesPlayed = 0
    players.forEach { teamGamesPlayed += it.gamesPlayed }
    return teamGamesPlayed / players.size
}

fun buildTeam(allPlayers: Map<String, Player>, teamIds: List<String>): List<Player> {
    val teamPlayers = ArrayList<Player>()
    teamIds.forEach { teamPlayers += allPlayers[it]!! }
    return teamPlayers
}

fun addNewPlayers(existingPlayers: Map<String, Player>, game: Game, startingRating: Int): Map<String, Player> {
    var newPlayerMap = existingPlayers
    val allPlayerIds = game.team1PlayerIds + game.team2PlayerIds
    allPlayerIds.forEach { playerId ->
        if (!newPlayerMap.containsKey(playerId)) {
            newPlayerMap += Pair(playerId, Player(id = playerId,
                    gamesPlayed = 0,
                    wins = 0,
                    losses = 0,
                    currentRating = startingRating))
        }
    }
    return newPlayerMap
}

fun updatePlayer(player: Player, ratingAdjustment: Int, games: Int, wins: Int, losses: Int): Player {
    return player.copy(
            currentRating = player.currentRating + ratingAdjustment,
            gamesPlayed = player.gamesPlayed + games,
            wins = player.wins + wins,
            losses = player.losses + losses)
}
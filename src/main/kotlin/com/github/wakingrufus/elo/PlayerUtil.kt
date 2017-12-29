package com.github.wakingrufus.elo


fun calculateTeamRating(players: Collection<Player>): Int {
    return players.sumBy { it.currentRating }.div(players.size)
}

fun calculateTeamGamesPlayed(players: Collection<Player>): Int {
    return players.sumBy { it.gamesPlayed }.div(players.size)
}

fun buildTeam(allPlayers: Map<String, Player>, teamIds: List<String>): Collection<Player> {
 if(teamIds.any { !allPlayers.containsKey(it) }) {
     throw RuntimeException("playerId list contains ids which no player has")
 }
  return allPlayers.filterKeys { teamIds.contains(it) }.values
}

fun addNewPlayers(existingPlayers: Map<String, Player>, game: Game, startingRating: Int): Map<String, Player> {
    var newPlayerMap = existingPlayers
    val allPlayerIds = game.team1PlayerIds + game.team2PlayerIds
    allPlayerIds.forEach { playerId ->
        if (!newPlayerMap.containsKey(playerId)) {
            newPlayerMap += Pair(playerId, Player(
                    id = playerId,
                    currentRating = startingRating
            ))
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
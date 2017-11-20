package com.github.wakingrufus.elo

import mu.KotlinLogging
import java.math.BigDecimal
import java.math.MathContext

private val logger = KotlinLogging.logger {}
fun calculateChanges(league: League, players: Map<String, Player>, game: Game): List<RatingHistoryItem> {

    val team1Players = buildTeam(allPlayers = players, teamIds = game.team1PlayerIds)
    val team1StartingRating = calculateTeamStartingRating(team1Players)
    val team1AvgGamesPlayed = calculateTeamAverageGamesPlayed(team1Players)

    val team2Players = buildTeam(allPlayers = players, teamIds = game.team2PlayerIds)
    val team2StartingRating = calculateTeamStartingRating(team2Players)
    val team2AvgGamesPlayed = calculateTeamAverageGamesPlayed(team2Players)

    val team1ExpectedScoreRatio = calculateExpectedScore(team1StartingRating, team2StartingRating, league.xi)
    val team2ExpectedScoreRatio = calculateExpectedScore(team2StartingRating, team1StartingRating, league.xi)

    logger.debug("team1ExpectedScoreRatio: " + team1ExpectedScoreRatio.toPlainString())
    logger.debug("team2ExpectedScoreRatio: " + team2ExpectedScoreRatio.toPlainString())

    val totalGoals = game.team1Score + game.team2Score
    val team1ActualScore = BigDecimal(game.team1Score).divide(BigDecimal(totalGoals), MathContext.DECIMAL32)
    val team2ActualScore = BigDecimal(game.team2Score).divide(BigDecimal(totalGoals), MathContext.DECIMAL32)

    logger.debug("team1ActualScore: " + team1ActualScore.toPlainString())
    logger.debug("team2ActualScore: " + team2ActualScore.toPlainString())

    var changeList: List<RatingHistoryItem> = ArrayList()
    for (player in team1Players) {
        logger.debug("applying changes to player: " + player.toString())
        val kFactor = calculateKfactor(league.kFactorBase, league.trialPeriod,
                league.trialKFactorMultiplier, player.gamesPlayed, team2AvgGamesPlayed)
        logger.debug("using kfactor: " + kFactor)
        val adjustment = calculateAdjustment(kFactor, team1ActualScore, team1ExpectedScoreRatio)
        logger.debug("adjustment: " + adjustment)
        val newHistory = RatingHistoryItem(
                gameId = game.id,
                playerId = player.id,
                ratingAdjustment = adjustment,
                startingRating = player.currentRating,
                win = game.team1Score > game.team2Score
        )
        changeList += newHistory
    }

    for (player in team2Players) {
        logger.debug("applying changes to player: " + player.toString())
        val kFactor = calculateKfactor(league.kFactorBase, league.trialPeriod,
                league.trialKFactorMultiplier, player.gamesPlayed, team1AvgGamesPlayed)
        logger.debug("using kfactor: " + kFactor)
        val adjustment = calculateAdjustment(kFactor, team2ActualScore, team2ExpectedScoreRatio)
        logger.debug("adjustment: " + adjustment)
        val newHistory = RatingHistoryItem(
                gameId = game.id,
                playerId = player.id,
                ratingAdjustment = adjustment,
                startingRating = player.currentRating,
                win = game.team2Score > game.team1Score
        )
        changeList += newHistory
    }
    return changeList
}

fun applyChanges(players: Map<String, Player>, changes: List<RatingHistoryItem>): Map<String, Player> {
    var newMap = players
    changes.forEach {
        newMap += Pair(it.playerId, updatePlayer(
                players[it.playerId]!!,
                it.ratingAdjustment,
                1,
                if (it.win) 1 else 0, if (it.win) 0 else 1))
    }
    return newMap
}
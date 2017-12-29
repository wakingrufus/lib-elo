package com.github.wakingrufus.elo

import mu.KotlinLogging
import java.math.BigDecimal
import java.math.MathContext

private val logger = KotlinLogging.logger {}
fun calculateChanges(league: League, players: Map<String, Player>, game: Game): List<RatingHistoryItem> {

    val team1Players = buildTeam(allPlayers = players, teamIds = game.team1PlayerIds)
    val team1StartingRating = calculateTeamRating(team1Players)

    val team2Players = buildTeam(allPlayers = players, teamIds = game.team2PlayerIds)
    val team2StartingRating = calculateTeamRating(team2Players)

    val team1ExpectedScoreRatio = calculateExpectedScore(team1StartingRating, team2StartingRating, league.xi)
    val team2ExpectedScoreRatio = calculateExpectedScore(team2StartingRating, team1StartingRating, league.xi)

    logger.debug("team1ExpectedScoreRatio: " + team1ExpectedScoreRatio.toPlainString())
    logger.debug("team2ExpectedScoreRatio: " + team2ExpectedScoreRatio.toPlainString())

    val totalGoals = game.team1Score + game.team2Score
    val team1ActualScore = BigDecimal(game.team1Score).divide(BigDecimal(totalGoals), MathContext.DECIMAL32)
    val team2ActualScore = BigDecimal(game.team2Score).divide(BigDecimal(totalGoals), MathContext.DECIMAL32)

    logger.debug("team1ActualScore: " + team1ActualScore.toPlainString())
    logger.debug("team2ActualScore: " + team2ActualScore.toPlainString())

    return team1Players.map { player ->
        RatingHistoryItem(
                gameId = game.id,
                playerId = player.id,
                ratingAdjustment = calculateAdjustment(
                        kFactor = calculateKfactor(
                                kfactorBase = league.kFactorBase,
                                trialPeriod = league.trialPeriod,
                                trialMultiplier = league.trialKFactorMultiplier,
                                playerGamesPlayed = player.gamesPlayed,
                                otherPlayerGamesPlayed = calculateTeamGamesPlayed(team2Players)
                        ),
                        score = team1ActualScore,
                        expectedScore = team1ExpectedScoreRatio
                ),
                startingRating = player.currentRating,
                win = game.team1Score > game.team2Score
        )
    }.plus(team2Players.map { player ->
        RatingHistoryItem(
                gameId = game.id,
                playerId = player.id,
                ratingAdjustment = calculateAdjustment(
                        kFactor = calculateKfactor(
                                kfactorBase = league.kFactorBase,
                                trialPeriod = league.trialPeriod,
                                trialMultiplier = league.trialKFactorMultiplier,
                                playerGamesPlayed = player.gamesPlayed,
                                otherPlayerGamesPlayed = calculateTeamGamesPlayed(team1Players)
                        ),
                        score = team2ActualScore,
                        expectedScore = team2ExpectedScoreRatio
                ),
                startingRating = player.currentRating,
                win = game.team2Score > game.team1Score
        )
    }).toList()
}

fun applyChanges(players: Map<String, Player>, changes: List<RatingHistoryItem>)
        : Map<String, Player> {
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
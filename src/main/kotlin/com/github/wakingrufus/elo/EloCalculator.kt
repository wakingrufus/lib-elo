package com.github.wakingrufus.elo

import java.math.BigDecimal

fun calculateAdjustment(kFactor: Int, score: BigDecimal, expectedScore: BigDecimal): Int {
    return BigDecimal(kFactor).multiply(score.subtract(expectedScore)).toInt()
}

fun calculateKfactor(kfactorBase: Int,
                     trialPeriod: Int,
                     trialMultiplier: Int,
                     playerGamesPlayed: Int,
                     allPlayers: List<Player>): Int {
    return when {
        playerGamesPlayed < trialPeriod -> kfactorBase * trialMultiplier
        allPlayers.any { it.gamesPlayed < trialPeriod } -> kfactorBase / trialMultiplier
        else -> kfactorBase
    }
}
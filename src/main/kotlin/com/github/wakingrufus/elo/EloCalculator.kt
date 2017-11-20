package com.github.wakingrufus.elo

import java.math.BigDecimal

fun calculateAdjustment(kFactor: Int, score: BigDecimal, expectedScore: BigDecimal): Int {
    return BigDecimal(kFactor).multiply(score.subtract(expectedScore)).toInt()
}

fun calculateKfactor(kfactorBase: Int,
                     trialPeriod: Int,
                     trialMultiplier: Int,
                     playerGamesPlayed: Int,
                     otherPlayerGamesPlayed: Int): Int {
    var kFactor = kfactorBase

    if (playerGamesPlayed < trialPeriod) {
        kFactor *= trialMultiplier

    }
    if (otherPlayerGamesPlayed < trialPeriod) {
        kFactor /= trialMultiplier
    }
    return kFactor
}
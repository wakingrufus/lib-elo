package com.github.wakingrufus.elo

import java.math.BigDecimal
import java.math.MathContext


fun calculateExpectedScore(rating1: Int, rating2: Int, xi: Int): BigDecimal {
    val q1 = calculateQ(teamRating = rating1, xi = xi)
    val q2 = calculateQ(teamRating = rating2, xi = xi)
    return calculateE(q1, q2)
}

private fun calculateQ(teamRating: Int, xi: Int): BigDecimal {
    return pow(BigDecimal.TEN, BigDecimal(teamRating).divide(BigDecimal(xi), MathContext.DECIMAL32))
}

private fun calculateE(q1: BigDecimal, q2: BigDecimal): BigDecimal {
    return q1.divide(q1.add(q2), MathContext.DECIMAL32)
}
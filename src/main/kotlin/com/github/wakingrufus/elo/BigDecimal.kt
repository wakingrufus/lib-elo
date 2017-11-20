package com.github.wakingrufus.elo

import java.math.BigDecimal
import java.math.RoundingMode

fun pow(base: BigDecimal, exponent: BigDecimal): BigDecimal {
    var result = BigDecimal.ZERO
    val signOf2 = exponent.signum()

    // Perform X^(A+B)=X^A*X^B (B = remainder)
    val dn1 = base.toDouble()
    // Compare the same row of digits according to context
    val n2 = exponent.multiply(BigDecimal(signOf2)) // n2 is now positive
    val remainderOf2 = n2.remainder(BigDecimal.ONE)
    val n2IntPart = n2.subtract(remainderOf2)
    // Calculate big part of the power using context -
    // bigger range and performance but lower accuracy
    val intPow = base.pow(n2IntPart.intValueExact())
    val doublePow = BigDecimal(Math.pow(dn1, remainderOf2.toDouble()))
    result = intPow.multiply(doublePow)

    // Fix negative power
    if (signOf2 == -1)
        result = BigDecimal.ONE.divide(result, RoundingMode.HALF_UP)
    return result
}
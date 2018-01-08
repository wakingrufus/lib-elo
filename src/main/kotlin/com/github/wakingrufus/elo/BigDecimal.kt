package com.github.wakingrufus.elo

import java.math.BigDecimal
import java.math.MathContext

fun BigDecimal.pow(exponent: BigDecimal): BigDecimal {
    val signOf2 = exponent.signum()

    // Perform X^(A+B)=X^A*X^B (B = remainder)
    val dn1 = this.toDouble()
    // Compare the same row of digits according to context
    val n2 = exponent.multiply(BigDecimal(signOf2)) // n2 is now positive
    val remainderOf2 = n2.remainder(BigDecimal.ONE)
    val n2IntPart = n2.subtract(remainderOf2)
    // Calculate big part of the power using context -
    // bigger range and performance but lower accuracy
    val intPow = this.pow(n2IntPart.intValueExact())
    val doublePow = BigDecimal(Math.pow(dn1, remainderOf2.toDouble()))
    var result = intPow.multiply(doublePow)

    // Fix negative power
    if (signOf2 == -1)
        result = BigDecimal.ONE.divide(result, MathContext.DECIMAL64)
    return result
}
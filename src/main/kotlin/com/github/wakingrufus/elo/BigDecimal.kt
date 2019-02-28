package com.github.wakingrufus.elo

import java.math.BigDecimal
import java.math.MathContext

fun BigDecimal.pow(exponent: BigDecimal, mathContext: MathContext = MathContext.DECIMAL64): BigDecimal {
    // Perform X^(A+B)=X^A*X^B (B = remainder)
    return exponent.signum().let { signOfExponent ->
        // Compare the same row of digits according to context
        exponent.multiply(BigDecimal(signOfExponent)).let { n2 ->
            n2.remainder(BigDecimal.ONE).let { remainderOfExponent ->
                // Calculate big part of the power using context -
                // bigger range and performance but lower accuracy
                this.pow(n2.subtract(remainderOfExponent).intValueExact()).let { intPow ->
                    intPow.multiply(BigDecimal(Math.pow(this.toDouble(), remainderOfExponent.toDouble()))).let {
                        // Fix negative power
                        if (signOfExponent == -1)
                            return BigDecimal.ONE.divide(it, mathContext) else it
                    }
                }
            }
        }
    }
}
package com.github.wakingrufus.elo

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class EloCalculatorTest {
    val newPlayer = Player(id = UUID.randomUUID().toString(), gamesPlayed = 9, currentRating = 1500)
    val oldPlayer = Player(id = UUID.randomUUID().toString(), gamesPlayed = 11, currentRating = 1500)
    @Test
    fun `test calculateKfactor new player`() {
        assertThat(calculateKfactor(32, 10, 2, 9, listOf(oldPlayer, newPlayer)))
                .isEqualTo(64)
    }

    @Test
    fun `test calculateKfactor playing against new player`() {
        assertThat(calculateKfactor(32, 10, 2, 11, listOf(oldPlayer, newPlayer)))
                .isEqualTo(16)
    }

    @Test
    fun `test calculateKfactor`() {
        assertThat(calculateKfactor(32, 10, 2, 11, listOf(oldPlayer)))
                .isEqualTo(32)
    }
}
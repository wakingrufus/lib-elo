package com.github.wakingrufus.elo

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerUtilKtTest {
    @Test
    fun `test calculateTeamRating`() {
        assertEquals("should return average of ratings",
                1500,
                calculateTeamRating(listOf(
                        Player(id = "1", currentRating = 1400),
                        Player(id = "2", currentRating = 1600)
                )))
    }

    @Test
    fun `test calculateTeamAverageGamesPlayed`() {
        assertEquals("returns average",
                3,
                calculateTeamGamesPlayed(listOf(
                        Player(id = "1", currentRating = 1400, gamesPlayed = 2),
                        Player(id = "2", currentRating = 1600, gamesPlayed = 4)
                )))
        assertEquals("returns average truncated",
                3,
                calculateTeamGamesPlayed(listOf(
                        Player(id = "1", currentRating = 1400, gamesPlayed = 3),
                        Player(id = "2", currentRating = 1600, gamesPlayed = 4)
                )))
    }

    @Test
    fun `test buildTeam`() {
        val p1 = Player(id = "1", currentRating = 1550)
        val p2 = Player(id = "2", currentRating = 1000)
        assertArrayEquals(listOf(p1).toTypedArray(),
                buildTeam(
                        mapOf("1" to p1, "2" to p2),
                        listOf("1")
                ).toTypedArray())
    }

    @Test(expected = RuntimeException::class)
    fun `test buildTeam with missing id`() {
        val p1 = Player(id = "1", currentRating = 1550)
        val p2 = Player(id = "2", currentRating = 1000)
        buildTeam(
                mapOf("1" to p1, "2" to p2),
                listOf("1", "3")
        )
    }
}
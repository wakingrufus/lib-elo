package com.github.wakingrufus.elo

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.util.*

class EloLeagueKtTest {

    @Test
    fun calculateNewLeague() {
        // data
        val player1Id = UUID.randomUUID().toString()
        val player2Id = UUID.randomUUID().toString()
        val league = League(kFactorBase = 32, trialKFactorMultiplier = 1)
        val game = Game(
                id = UUID.randomUUID().toString(),
                team1Score = 10,
                team2Score = 0,
                team1PlayerIds = listOf(player1Id),
                team2PlayerIds = listOf(player2Id),
                entryDate = Instant.now())


        val actual = calculateNewLeague(league = league, games = listOf(game))

        // assertions
        val expectedPlayer1 = Player(id = player1Id, currentRating = league.startingRating + 16, gamesPlayed = 1, wins = 1, losses = 0)
        val expectedPlayer2 = Player(id = player2Id, currentRating = league.startingRating - 16, gamesPlayed = 1, wins = 0, losses = 1)
        assertEquals(expectedPlayer1, actual.players[player1Id])
        assertEquals(expectedPlayer2, actual.players[player2Id])
        assertEquals(game.id, actual.history[0].gameId)
        assertEquals(player1Id, actual.history[0].playerId)
        assertEquals(16, actual.history[0].ratingAdjustment)
        assertEquals(league.startingRating, actual.history[0].startingRating)
        assertEquals(game.id, actual.history[1].gameId)
        assertEquals(player2Id, actual.history[1].playerId)
        assertEquals(-16, actual.history[1].ratingAdjustment)
        assertEquals(league.startingRating, actual.history[1].startingRating)
        assertEquals(league, actual.league)

    }

    @Test
    fun addGameToLeague() {
        // data
        val player1Id = UUID.randomUUID().toString()
        val player2Id = UUID.randomUUID().toString()
        val player1 = Player(id = player1Id, gamesPlayed = 20, currentRating = 1200, wins = 5, losses = 15)
        val player2 = Player(id = player2Id, gamesPlayed = 15, currentRating = 1600, losses = 0, wins = 15)
        val league = League(kFactorBase = 32, trialKFactorMultiplier = 1)
        val game = Game(
                id = UUID.randomUUID().toString(),
                team1Score = 9,
                team2Score = 1,
                team1PlayerIds = listOf(player1.id),
                team2PlayerIds = listOf(player2.id),
                entryDate = Instant.now())

        val leagueState = LeagueState(league = league, players = mapOf(player1.id to player1, player2.id to player2))


        val actual = addGameToLeague(leagueState = leagueState, game = game)

        // assertions
        val expectedPlayer1 = Player(
                id = player1Id,
                currentRating = player1.currentRating + 19,
                gamesPlayed = player1.gamesPlayed + 1,
                wins = player1.wins + 1,
                losses = player1.losses + 0)
        val expectedPlayer2 = Player(
                id = player2Id,
                currentRating = player2.currentRating - 19,
                gamesPlayed = player2.gamesPlayed + 1,
                wins = player2.wins + 0,
                losses = player2.losses + 1)
        assertEquals(expectedPlayer1, actual.players[player1Id])
        assertEquals(expectedPlayer2, actual.players[player2Id])

        assertEquals(game.id, actual.history[0].gameId)
        assertEquals(player1Id, actual.history[0].playerId)
        assertEquals(19, actual.history[0].ratingAdjustment)
        assertEquals(player1.currentRating, actual.history[0].startingRating)
        assertEquals(game.id, actual.history[1].gameId)
        assertEquals(player2Id, actual.history[1].playerId)
        assertEquals(-19, actual.history[1].ratingAdjustment)
        assertEquals(player2.currentRating, actual.history[1].startingRating)
        assertEquals(league, actual.league)


    }

}
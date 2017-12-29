package com.github.wakingrufus.elo

data class Player(
        val id: String,
        val currentRating: Int,
        val gamesPlayed: Int = 0,
        val wins: Int = 0,
        val losses: Int = 0
)
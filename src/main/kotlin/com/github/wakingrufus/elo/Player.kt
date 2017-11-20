package com.github.wakingrufus.elo

data class Player(
        val id: String,
        val currentRating: Int,
        val gamesPlayed: Int,
        val wins: Int,
        val losses: Int
)
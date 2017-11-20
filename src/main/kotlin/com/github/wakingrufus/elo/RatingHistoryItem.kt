package com.github.wakingrufus.elo

data class RatingHistoryItem(
        val playerId: String,
        val gameId: String,
        val ratingAdjustment: Int,
        val startingRating: Int,
        val win: Boolean
)
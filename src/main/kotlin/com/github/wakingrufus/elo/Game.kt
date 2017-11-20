package com.github.wakingrufus.elo

import java.time.Instant

data class Game(val id: String,
                val team1Score: Int,
                val team2Score: Int,
                val entryDate: Instant,
                val team1PlayerIds: List<String>,
                val team2PlayerIds: List<String>)

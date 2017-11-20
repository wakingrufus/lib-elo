package com.github.wakingrufus.elo;

data class League(val startingRating: Int = 1500,
                  val xi: Int = 1000,
                  val kFactorBase: Int = 32,
                  val trialPeriod: Int = 10,
                  val trialKFactorMultiplier: Int = 2,
                  val teamSize: Int)
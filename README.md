# lib-elo
Library for ELO calculations for game leagues

[![Run Status](https://api.shippable.com/projects/5a12e20c79f26b0700c37035/badge?branch=master)](https://app.shippable.com/github/wakingrufus/lib-elo)
[![Coverage Badge](https://api.shippable.com/projects/5a12e20c79f26b0700c37035/coverageBadge?branch=master)](https://app.shippable.com/github/wakingrufus/lib-elo)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.wakingrufus/lib-elo/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wakingrufus/lib-elo)

## Features
- Support for _n_-sized teams
- "Exhibition" period: first _n_ games can have and adjustment multiplier in order to move a player to their proper rating faster. Their opponents will get an inverse multiplier.

## Usage

First, start a league by creating a league object with the configuration for the league:

```kotlin
val league = League(teamSize = 1, kFactorBase = 32, trialKFactorMultiplier = 1)
```

You will probably want to persist this information within your application so that you can re-create this object at anytime

Next, build the game information objects:

```kotlin
val game = Game(
                id = UUID.randomUUID().toString(),
                team1Score = 10,
                team2Score = 0,
                team1PlayerIds = listOf(player1Id),
                team2PlayerIds = listOf(player2Id),
                entryDate = Instant.now())
```

Player ids are an identifier supplied by you. Just make sure that the values are consistent for a given player across all games.
Then, you can calculate the ratings:

```kotlin
val output : LeagueState = calculateNewLeague(league = league, games = listOf(game))
```

view the `LeagueState` class for information on the output.
To add more games, you do not need to repeat all of these steps. 
Simply use the previously calculated `LeagueState`:

```kotlin
 val updatedResults : LeagueState = addGameToLeague(leagueState = leagueState, game = newGame)
```
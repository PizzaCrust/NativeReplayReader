package me.tgsc.replay

import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
data class Elimination(val Eliminated: String,
                       val Eliminator: String,
                       val GunType: Int,
                       val Time: String,
                       val Knocked: Boolean)

@Serializable
data class GameStats(val Eliminations: Int,
                     val Accuracy: Double,
                     val Assists: Int,
                     val WeaponDamage: Int,
                     val OtherDamage: Int,
                     val DamageToPlayers: Double,
                     val Revives: Int,
                     val DamageTaken: Int,
                     val DamageToStructures: Int,
                     val MaterialsGathered: Int,
                     val MaterialsUsed: Int,
                     val TotalTraveled: Int)
@Serializable
data class TeamStats(val Position: Int,
                     val TotalPlayers: Int)

@Serializable
data class GameData(val GameSessionId: String,
                    val UtcTimeStartedMatch: String,
                    val CurrentPlaylist: String?,
                    val AdditionalPlaylistLevels: List<String>,
                    val ActiveGameplayModifiers: List<String>,
                    val TeamCount: Int,
                    val IsTournamentRound: Boolean,
                    val TournamentRound: Int?,
                    val AircraftStartTime: Double,
                    val SafeZoneStartTime: Double? = null,
                    val WinningTeam: Int?,
                    val WinningPlayerIds: List<Int>?)

@Serializable
data class Team(val TeamIndex: Int, val PlayerIds: List<Int>, val PlayerNames: List<String?>,
                val PartyOwnerId: Int?, val Placement: Int?)

// Henchman AI do not have Bot ID & has player name custom override, Player AI have bot ID
@Serializable
data class Player(val Id: Int,
                  val PlayerId: String?,
                  val EpicId: String?,
                  val BotId: String?,
                  val IsBot: Boolean?, // either true or null
                  val PlayerNameCustomOverride: String?, // only henchman
                  val Platform: String?,
                  val Level: Int?,
                  val SeasonLevelUIDisplay: Int?,
                  val TeamIndex: Int?,
                  val IsPartyLeader: Boolean,
                  val IsReplayOwner: Boolean,
                  val HasFinishedLoading: Boolean?,
                  val HasStartedPlaying: Boolean?,
                  val HasThankedBusDriver: Boolean?, // either true or null
                  val Placement: Int?,
                  val Kills: Int?,
                  val TeamKills: Int?,
                  val DeathCause: Int?,
                  val DeathTags: List<String>?,
                  val DeathLocation: Map<String, Double>?)

@Serializable
data class KillFeedEntry(val PlayerId: Int,
                         val PlayerName: String?,
                         val PlayerIsBot: Boolean,
                         val FinisherOrDowner: Int?,
                         val FinisherOrDownerName: String?,
                         val FinisherOrDownerIsBot: Boolean,
                         val ReplicatedWorldTimeSeconds: Double,
                         val Distance: Double?,
                         val DeathCause: Int?,
                         val DeathLocation: Map<String, Double>,
                         val DeathTags: List<String>?,
                         val IsDowned: Boolean,
                         val IsRevived: Boolean)

@Serializable
data class Replay(val Eliminations: List<Elimination>,
                  val Stats: GameStats,
                  val TeamStats: TeamStats,
                  val GameData: GameData,
                  val TeamData: List<Team>,
                  val PlayerData: List<Player>,
                  val KillFeed: List<KillFeedEntry>) {

    fun getPlayer(id: Int): Player? {
        for (playerDatum in PlayerData) {
            if (playerDatum.Id == id) {
                return playerDatum
            }
        }
        return null
    }

    companion object {
        fun fromJson(str: String) = Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true)).parse(serializer(), str)
    }

}
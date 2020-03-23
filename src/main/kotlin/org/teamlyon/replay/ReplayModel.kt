package org.teamlyon.replay

data class Elimination(var Eliminated: String,
                       var Eliminator: String,
                       var GunType: Int,
                       var Time: String,
                       var Knocked: Boolean)

data class GameStats(var Eliminations: Int,
                     var Accuracy: Double,
                     var Assists: Int,
                     var WeaponDamage: Int,
                     var OtherDamage: Int,
                     var DamageToPlayers: Double,
                     var Revives: Int,
                     var DamageTaken: Int,
                     var DamageToStructures: Int,
                     var MaterialsGathered: Int,
                     var MaterialsUsed: Int,
                     var TotalTraveled: Int)

data class TeamStats(var Position: Int,
                     var TotalPlayers: Int)

data class GameData(var GameSessionId: String,
                    var UtcTimeStartedMatch: String,
                    var CurrentPlaylist: String,
                    var AdditionalPlaylistLevels: List<String>,
                    var ActiveGameplayModifiers: List<String>,
                    var TeamCount: Int,
                    var IsTournamentRound: Boolean,
                    var TournamentRound: Int?,
                    var AircraftStartTime: Double,
                    var SafeZoneStartTime: Double,
                    var WinningTeam: Int?,
                    var WinningPlayerIds: List<Int>?)

data class Team(var TeamIndex: Int, var PlayerIds: List<Int>, var PlayerNames: List<String>,
                var PartyOwnerId: Int?, var Placement: Int)

// Henchman AI do not have Bot ID & has player name custom override, Player AI have bot ID
data class Player(var Id: Int,
                  var PlayerId: String?,
                  var EpicId: String?,
                  var BotId: String?,
                  var IsBot: Boolean?, // either true or null
                  var PlayerNameCustomOverride: String?, // only henchman
                  var Platform: String?,
                  var Level: Int?,
                  var SeasonLevelUIDisplay: Int?,
                  var TeamIndex: Int,
                  var IsPartyLeader: Boolean,
                  var IsReplayOwner: Boolean,
                  var HasFinishedLoading: Boolean?,
                  var HasStartedPlaying: Boolean?,
                  var HasThankedBusDriver: Boolean?, // either true or null
                  var Placement: Int,
                  var Kills: Int?,
                  var TeamKills: Int?,
                  var DeathCause: Int?,
                  var DeathTags: List<String>?,
                  var DeathLocation: Map<String, Double>?)

data class Replay(var Eliminations: List<Elimination>,
                  var Stats: GameStats,
                  var TeamStats: TeamStats,
                  var GameData: GameData,
                  var TeamData: List<Team>,
                  var PlayerData: List<Player>)
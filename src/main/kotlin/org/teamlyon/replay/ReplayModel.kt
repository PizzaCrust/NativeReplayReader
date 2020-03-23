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

data class Replay(var Eliminations: List<Elimination>,
                  var Stats: GameStats,
                  var TeamStats: TeamStats,
                  var GameData: GameData)
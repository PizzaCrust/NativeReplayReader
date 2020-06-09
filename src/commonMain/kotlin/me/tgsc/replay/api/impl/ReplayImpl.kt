package me.tgsc.replay.api.impl

import me.tgsc.replay.Replay
import me.tgsc.replay.api.*

fun Replay.toApi(): me.tgsc.replay.api.Replay {
    if (GameData.WinningTeam != null) {
        return CompleteReplayImpl(this)
    }
    return ReplayImpl(this)
}

private open class ReplayImpl(private val h: Replay): me.tgsc.replay.api.Replay {
    override val handle: Any
        get() = h
    override val complete: Boolean
        get() = h.GameData.WinningTeam != null
    override val sessionId: String
        get() {
            val sess = h.GameData.GameSessionId
            if (sess.contains("CK-")) {
                return sess.split("|")[1]
            }
            return sess
        }
    override val matchStartedTimestamp: String
        get() = h.GameData.UtcTimeStartedMatch
    override val teams: List<RTeam>
        get() {
            val teams = mutableListOf<RTeam>()
            for (teamDatum in h.TeamData) {
                teams.add(TeamImpl(teamDatum, this))
            }
            teams.sortBy { it.placement }
            return teams
        }
    override val players: List<RPlayer>
        get() {
            val players = mutableListOf<RPlayer>()
            for (playerDatum in h.PlayerData) {
                val player = (fromHandle(playerDatum, this))
                if (player is RHumanPlayer) {
                    if (playerDatum.EpicId != null) {
                        players.add(player)
                    }
                } else {
                    players.add(player)
                }
            }
            return players
        }
    override val humanPlayers: List<RHumanPlayer>
        get() {
            val humans = mutableListOf<RHumanPlayer>()
            for (player in players) {
                if (player is RHumanPlayer) {
                    humans.add(player)
                }
            }
            return humans
        }

    private fun convertTimeString(time: String): Long {
        val split = time.split(":")
        val mins = split[0].toInt()
        val secs = split[1].toInt()
        return (((mins * 60) + secs) * 1000).toLong()
    }

    override val eliminations: List<Elimination>
        get() {
            val kills = mutableListOf<Elimination>()
            for (elimination in h.Eliminations) {
                if (elimination.Eliminator == "Bot" || elimination.Eliminated == "Bot") {
                    continue
                }
                if (elimination.Eliminated.contains("_") || elimination.Eliminator.contains("_")) {
                    continue
                }
                kills.add(
                        Elimination(
                                playerFromStringId(elimination.Eliminator.toLowerCase())!!,
                                playerFromStringId(elimination.Eliminated.toLowerCase())!!,
                                elimination.GunType,
                                elimination.Knocked,
                                convertTimeString(elimination.Time)
                        )
                )
            }
            return kills
        }
    override val playlist: String
        get() {
            return h.GameData.CurrentPlaylist ?: "Recorder not party leader"
        }
    override val playlistLevels: List<String>
        get() = h.GameData.AdditionalPlaylistLevels
    override val gameplayModifiers: List<String>
        get() = h.GameData.ActiveGameplayModifiers
    override val tournament: Boolean
        get() = h.GameData.IsTournamentRound
    override val tournamentRound: Int?
        get() = h.GameData.TournamentRound
    override val busStartTime: Double
        get() = h.GameData.AircraftStartTime
    override val safeZoneStartTime: Double?
        get() = h.GameData.SafeZoneStartTime
    override val replayOwner: RHumanPlayer
        get() {
            for (player in this.players) {
                if (player.replayOwner) {
                    return player as RHumanPlayer
                }
            }
            throw UnsupportedOperationException("No owner")
        }
    override val custom: Boolean
        get() = h.GameData.GameSessionId.contains("CK-")
    override val customKey: String?
        get() {
            if (custom) {
                return h.GameData.GameSessionId.split("K-")[1].split("|")[0]
            }
            return null
        }
}

private class CompleteReplayImpl(private val h: Replay): CompleteReplay,
        ReplayImpl(h) {
    override val winningTeam: RTeam
        get() = this.teamFromId(h.GameData.WinningTeam!!)!!

}
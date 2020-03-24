package org.teamlyon.replay.api.impl

import org.teamlyon.replay.NativeRunner
import org.teamlyon.replay.api.*
import java.io.File
import kotlin.system.exitProcess

fun main() {
    NativeRunner().processReplay(File("duo 2.replay")) {
        val api = toApi()
        for (player in api.players) {
            if (player is RHumanPlayer) {
                println("${player.epicId} killed ${player.kills.size} players")
            }
        }
    }.get()
    exitProcess(0)
}

fun org.teamlyon.replay.Replay.toApi(): Replay {
    if (GameData.WinningTeam != null) {
        return CompleteReplayImpl(this)
    }
    return ReplayImpl(this)
}

private open class ReplayImpl(private val h: org.teamlyon.replay.Replay): Replay {
    override val handle: Any
        get() = h
    override val complete: Boolean
        get() = h.GameData.WinningTeam != null
    override val sessionId: String
        get() = h.GameData.GameSessionId
    override val matchStartedTimestamp: String
        get() = h.GameData.UtcTimeStartedMatch
    override val teams: List<RTeam>
        get() {
            val teams = mutableListOf<RTeam>()
            for (teamDatum in h.TeamData) {
                teams.add(TeamImpl(teamDatum, this))
            }
            return teams
        }
    override val players: List<RPlayer>
        get() {
            val players = mutableListOf<RPlayer>()
            for (playerDatum in h.PlayerData) {
                players.add(fromHandle(playerDatum, this))
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
                                playerFromEpicId(elimination.Eliminator.toLowerCase())!!,
                                playerFromEpicId(elimination.Eliminated.toLowerCase())!!,
                                elimination.GunType,
                                elimination.Knocked,
                                convertTimeString(elimination.Time)
                        )
                )
            }
            return kills
        }
    override val playlist: String
        get() = h.GameData.CurrentPlaylist
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
    override val safeZoneStartTime: Double
        get() = h.GameData.SafeZoneStartTime
}

private class CompleteReplayImpl(private val h: org.teamlyon.replay.Replay): CompleteReplay,
        ReplayImpl(h) {
    override val winningTeam: RTeam
        get() = this.teamFromId(h.GameData.WinningTeam!!)!!

}
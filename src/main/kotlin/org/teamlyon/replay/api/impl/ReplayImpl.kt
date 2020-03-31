package org.teamlyon.replay.api.impl

import org.teamlyon.replay.NativeRunner
import org.teamlyon.replay.api.*
import java.io.File
import java.lang.UnsupportedOperationException
import kotlin.system.exitProcess

fun main() {
    NativeRunner().processReplay(File("custom.replay")) {
        val api = toApi()
        println("complete: ${api.complete}")
        println(api.javaClass.simpleName)
        println(GameData.GameSessionId)
        println(api.sessionId)
        println(api.customKey)
        val owner = api.replayOwner
        println(owner.epicId)
        for (player in api.players) {
            if (player is RHumanPlayer) {
                println(player.epicId)
            }
        }
        //println((owner.killer!! as RHumanPlayer).epicId)
        //println(owner.killer!!.team)
        /*
        for (humanPlayer in (api as CompleteReplay).winningTeam.humanPlayers) {
            println("team detected, ${humanPlayer.epicId}")
        }
        for (player in api.players) {
            if (player is RHumanPlayer) {
                println("${player.epicId} killed ${player.kills.size} players")
                println("The killer died at ${player.timeOfDeath} with placement ${player.placement}")
                println("Team had ${player.team.kills.size} and died at ${player.team.timeOfDeath}")
                for (humanPlayer in player.team.humanPlayers) {
                    println(" - died at ${humanPlayer.timeOfDeath}")
                }
            }
        }
         */
        /*
        api.teams.forEachIndexed { index, team ->
            if (!team.botTeam) {
                print("$index. ")
                for (humanPlayer in team.humanPlayers) {
                    print(humanPlayer.epicId + " ")
                }
                print("(team kills: ${team.kills.size})\n")
            }
        }
         */
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

private class CompleteReplayImpl(private val h: org.teamlyon.replay.Replay): CompleteReplay,
        ReplayImpl(h) {
    override val winningTeam: RTeam
        get() = this.teamFromId(h.GameData.WinningTeam!!)!!

}
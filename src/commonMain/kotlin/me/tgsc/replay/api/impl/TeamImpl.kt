package me.tgsc.replay.api.impl

import me.tgsc.replay.TeamData
import me.tgsc.replay.api.*

class TeamImpl(private val obj: TeamData, private val r: Replay): RTeam {
    override val handle: Any
        get() = obj
    override val id: Int
        get() = obj.TeamIndex ?: -1
    override val botTeam: Boolean
        get() {
            var bots = false
            for (player in this.players) {
                if (player.bot) {
                    bots = true
                }
            }
            return bots
        }
    override val players: List<RPlayer>
        get() {
            val players = mutableListOf<RPlayer>()
            for (playerId in obj.PlayerIds) {
                playerId ?: continue
                val plHandle = r.playerFromId(playerId)!!
                players.add(plHandle)
            }
            return players
        }
    override val humanPlayers: List<RHumanPlayer>
        get() {
            val humans = mutableListOf<RHumanPlayer>()
            players.forEach {
                if (it is RHumanPlayer) {
                    humans.add(it)
                }
            }
            return humans
        }
    override val partyOwner: RPlayer?
        get() {
            if (obj.PartyOwnerId != null) {
                return r.playerFromId(obj.PartyOwnerId)!!
            }
            return null
        }
    override val placement: Int?
        get() = obj.Placement
    override val kills: List<Elimination>
        get() {
            val kills = mutableListOf<Elimination>()
            for (player in players) {
                kills.addAll(player.kills)
            }
            return kills
        }
    override val replay: Replay
        get() = r
    override val timeOfDeath: Long?
        get() {
            var latestTime: Long? = null
            var win = false
            for (humanPlayer in humanPlayers) {
                if (humanPlayer.placement != null) {
                    if (humanPlayer.placement!! == 1) {
                        win = true
                    }
                }
                var death = 0L
                if (humanPlayer.timeOfDeath != null) {
                    death = humanPlayer.timeOfDeath!!
                }
                if (latestTime == null) {
                    latestTime = death
                } else if (latestTime < death) {
                    latestTime = death
                }
            }
            return if (!win) {
                latestTime
            } else {
                null
            }
        }

}
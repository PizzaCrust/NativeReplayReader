package org.teamlyon.replay.api.impl

import org.teamlyon.replay.Team
import org.teamlyon.replay.api.*

class TeamImpl(private val obj: Team, private val r: Replay): RTeam {
    override val handle: Any
        get() = obj
    override val id: Int
        get() = obj.TeamIndex
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
                return r.playerFromId(obj.PartyOwnerId!!)!!
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

}
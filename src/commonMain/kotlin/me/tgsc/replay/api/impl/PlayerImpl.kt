package me.tgsc.replay.api.impl

import me.tgsc.replay.Player
import me.tgsc.replay.api.*

private open class PlayerImpl(protected val handleObj: Player, private val r: Replay): RPlayer {
    override val handle: Any
        get() = this.handleObj
    override val id: Int
        get() = handleObj.Id
    override val team: RTeam
        get() {
            for (team in r.teams) {
                if (team.id == handleObj.TeamIndex) {
                    return team
                }
            }
            throw UnsupportedOperationException("User has no team")
        }
    override val partyLeader: Boolean
        get() = handleObj.IsPartyLeader
    override val replayOwner: Boolean
        get() = handleObj.IsReplayOwner
    override val death: DeathInfo?
        get() {
            return if (handleObj.DeathCause != null) {
                DeathInfo(handleObj.DeathCause, fromMapToLoc(handleObj.DeathLocation!!),
                        handleObj.DeathTags!!)
            } else {
                null
            }
        }
    override val kills: List<Elimination>
        get() {
            val list = mutableListOf<Elimination>()
            for (elim in r.eliminations) {
                if (elim.killer.id == elim.victim.id) {
                    continue
                }
                if (!elim.knocked && !elim.victim.bot && elim.killer.id == id) {
                    list.add(elim)
                }
            }
            return list
        }
    override val bot: Boolean
        get() = handleObj.IsBot != null && handleObj.IsBot
    override val replay: Replay
        get() = r
    override val placement: Int?
        get() {
            return handleObj.Placement
        }
}

fun fromHandle(obj: Player, r: Replay): RPlayer {
    return if (obj.IsBot != null && obj.IsBot) {
        // henchman or player ai
        if (obj.BotId != null) {
            PlayerAIImpl(obj, r)
        } else {
            NPCAIImpl(obj, r)
        }
    } else {
        HumanPlayerImpl(obj, r)
    }
}

private class HumanPlayerImpl(handleObj: Player, r: Replay): RHumanPlayer, PlayerImpl(handleObj,
        r) {
    override val epicId: String
        get() = handleObj.EpicId!!.toLowerCase()
    override val epicName: String
        get() = resolver.resolve(epicId)
    override val platform: String
        get() = handleObj.Platform!!
    override val level: Int
        get() = handleObj.Level!!

    val deathElimination: Elimination? // latest death
        get() {
            var death: Elimination? = null
            for (elimination in this.replay.eliminations) {
                if (elimination.victim is RHumanPlayer) {
                    // suicides
                    if (elimination.killer.id == elimination.victim.id) {
                        continue
                    }
                    if (elimination.victim.epicId == this.epicId) {
                        death = elimination
                    }
                }
            }
            return death
        }

    override val killer: RPlayer?
        get() {
            return deathElimination?.killer
        }
    override val timeOfDeath: Long?
        get() {
            return deathElimination?.time
        }
}

private class PlayerAIImpl(handleObj: Player, r: Replay): RPlayerBot, PlayerImpl(handleObj, r) {
    override val botId: String
        get() = handleObj.BotId!!.toLowerCase()
}

private class NPCAIImpl(handleObj: Player, r: Replay): RHenchmanBot, PlayerImpl(handleObj, r) {
    override val name: String
        get() = handleObj.PlayerNameCustomOverride!!

}
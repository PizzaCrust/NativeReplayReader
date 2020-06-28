package me.tgsc.replay.api

import me.tgsc.replay.FVector

data class Location(val x: Double, val y: Double, val z: Double)

fun fromVectorToLoc(map: FVector): Location {
    return Location(map.X.toDouble(), map.Y.toDouble(), map.Z.toDouble())
}

data class DeathInfo(val cause: Int, val location: Location, val tags: List<String>)

data class Elimination(val killer: RPlayer,
                       val victim: RPlayer,
                       val gunType: Int,
                       val knocked: Boolean,
                       val time: Long)

/**
 * Represents a resolver that will resolve epic ids to epic names.
 */
interface EpicResolver {

    /**
     * Bulk resolves epic ids to names and caches the data.
     */
    fun resolve(str: List<String>)

    /**
     * Resolve a specific account id to display name. Will add/use cache when necessary.
     */
    fun resolve(str: String): String

}

lateinit var resolver: EpicResolver

/**
 * Represents a player in a replay.
 * This can be a henchman AI, player AI, or a human player.
 */
interface RPlayer {

    val handle: Any

    /**
     * Represents the replay's internal id of the player.
     * Can be used to find more data regarding the player.
     */
    val id: Int

    /**
     * Represents the player's team.
     */
    val team: RTeam

    /**
     * Represents if this player is the leader of the party of his team.
     */
    val partyLeader: Boolean

    /**
     * Represents if this player is the recorder of the replay.
     */
    val replayOwner: Boolean

    /**
     * Represents the player's death. Can be null if he didn't die at the latest state of the
     * recorder. (however, is not reliable)
     */
    val death: DeathInfo?

    /**
     * Represents the kills by this player.
     */
    val kills: List<Elimination>

    /**
     * Represents if this player is a bot. Can be henchman or player AI.
     */
    val bot: Boolean

    /**
     * Represents the replay that this object is responsible to
     */
    val replay: Replay

    /**
     * Represents the placement, can be null if replay cant compute placement
     */
    val placement: Int?

}

interface RHumanPlayer: RPlayer {

    /**
     * Epic id of this player, lowercase from default
     */
    val epicId: String

    /**
     * Epic name resolved by the implementation from the epic id
     */
    val epicName: String

    /**
     * Platform, ex: PSN, WIN
     */
    val platform: String

    /**
     * The level of this player
     */
    val level: Int

    /**
     * Represents the killer of the player
     */
    val killer: RPlayer?

    /**
     * Represents the time of death of the player. Will be null if replay doesn't have or if the
     * player didn't die.
     */
    val timeOfDeath: Long?

}

/**
 * Represents an Player AI that simulates a player. (rather than dedicated in-game NPCs)
 */
interface RPlayerBot: RPlayer {

    val botId: String

}

/**
 * Represents an NPC AI that serves a purpose in the game.
 */
interface RHenchmanBot: RPlayer {

    val name: String // example: shadow henchman

}
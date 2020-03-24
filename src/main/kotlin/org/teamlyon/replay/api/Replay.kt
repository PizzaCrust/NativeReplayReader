package org.teamlyon.replay.api

interface Replay {

    val handle: Any

    /**
     * Indicates whether the recorder has completely recorded the game from start to end.
     */
    val complete: Boolean

    /**
     * Represents the game session id, as indicated at the top left corner inside the match.
     */
    val sessionId: String

    /**
     * Represents the timestamp when the match started in UTC.
     */
    val matchStartedTimestamp: String

    /**
     * Represents the teams in the match.
     */
    val teams: List<RTeam>

    /**
     * Represents the players in the match.
     */
    val players: List<RPlayer>

    fun playerFromId(int: Int): RPlayer? {
        for (player in this.players) {
            if (player.id == int) {
                return player
            }
        }
        return null
    }

    fun playerFromEpicId(str: String): RHumanPlayer? {
        for (player in this.players) {
            if (player is RHumanPlayer) {
                if (player.epicId == str) {
                    return player
                }
            }
        }
        return null
    }

    /**
     * Represents human players
     */
    val humanPlayers: List<RHumanPlayer>

    /**
     * Represents the kills in the match.
     */
    val eliminations: List<Elimination>

    /**
     * Represents the playlist that this replay was recorded in. (Game mode, such as Arena Duos)
     */
    val playlist: String

    val playlistLevels: List<String>

    val gameplayModifiers: List<String>

    /**
     * Indicates whether this replay was recorded in a tournament match.
     */
    val tournament: Boolean

    /**
     * Represents the round of the tournament, if this is a tournament match.
     */
    val tournamentRound: Int?

    /**
     * When the battle bus starts
     */
    val busStartTime: Double

    /**
     * When the first zone starts
     */
    val safeZoneStartTime: Double

}

/**
 * Represents a replay that has been recorded from start of the match to the end.
 */
interface CompleteReplay: Replay {

    val winningTeam: RTeam

}
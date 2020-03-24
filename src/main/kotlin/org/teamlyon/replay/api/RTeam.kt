package org.teamlyon.replay.api

interface RTeam {

    val handle: Any

    /**
     * Team ID, or commonly known as TeamIndex
     */
    val id: Int

    /**
     * Consists of or made of bots.
     */
    val botTeam: Boolean

    /**
     * Represents the players in this team. Could be bots or humans
     */
    val players: List<RPlayer>

    /**
     * Represents the human players in this team.
     */
    val humanPlayers: List<RHumanPlayer>

    /**
     * Represents the party leader of the party.
     */
    val partyOwner: RPlayer?

    /**
     * Represents the placement of the party.
     */
    val placement: Int?

    /**
     * Represents the kills by the team
     */
    val kills: List<Elimination>

    /**
     * Represents the replay that this object is from
     */
    val replay: Replay

}
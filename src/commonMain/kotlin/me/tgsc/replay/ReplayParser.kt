package me.tgsc.replay

/**
 * Represents an interface that interfaces with a replay parser that can parse based on the
 * specified type.
 */
interface ReplayParser<T, R: ReplayParser.Ticket> {

    enum class ParseMode {
        EventsOnly, Minimal, Normal, Full, Debug, Ignore
    }

    interface Ticket {
        val completed: Boolean
    }

    /**
     * Processes the specified resource replay with specified parse mode. Returns ticket that
     * will get updated with the replay parsing concurrently.
     */
    fun processReplay(inputResource: T,
                      parseMode: ParseMode = ParseMode.Minimal,
                      block: Replay.() -> Unit): R

}
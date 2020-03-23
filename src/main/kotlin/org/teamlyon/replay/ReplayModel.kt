package org.teamlyon.replay

data class Elimination(var Eliminated: String,
                       var Eliminator: String,
                       var GunType: Int,
                       var Time: String,
                       var Knocked: Boolean)

data class Replay(var Eliminations: List<Elimination>)
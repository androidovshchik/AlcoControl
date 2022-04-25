package com.alcocontrol.model

class Snapshot(
    var isFront: Boolean,
    var orientation: Int
) {

    var rotation = 0

    lateinit var data: ByteArray

    override fun toString(): String {
        return "Snapshot(isFront=$isFront, orientation=$orientation, rotation=$rotation)"
    }
}
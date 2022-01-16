package com.akame.akameproject.decorate

class DecorateDe2(absDecorate: AbsDecorate) : DecorateImpl() {
    init {
        this.abs = absDecorate
    }

    override fun testAbs() {
        println("DecorateDe2")
    }
}
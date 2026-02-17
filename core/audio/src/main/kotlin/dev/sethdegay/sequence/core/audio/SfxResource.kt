package dev.sethdegay.sequence.core.audio

import androidx.annotation.RawRes

internal enum class SfxResource(@param:RawRes val id: Int) {
    BELL(R.raw.bell),
    TICK_ODD(R.raw.tick_odd),
    TICK_EVEN(R.raw.tick_even),
}
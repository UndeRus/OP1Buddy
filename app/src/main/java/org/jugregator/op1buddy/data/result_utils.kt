package org.jugregator.op1buddy.data

sealed class LCE<out C> {
    data object Loading: LCE<Nothing>()
    class Content<C>(val data: C): LCE<C>()
    data object Error: LCE<Nothing>()
}

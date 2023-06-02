package com.connor.fuckcolorapp.utils

import com.connor.fuckcolorapp.states.Empty
import com.connor.fuckcolorapp.states.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext

val eventBus = MutableSharedFlow<Event>()
val stickEvent = MutableSharedFlow<Event>()
suspend fun subscribe(block: suspend (Event) -> Unit) {
    eventBus.zip(stickEvent) { a, b -> getNotEmpty(a, b) }.collect(block)
}

suspend fun post(event: Event, isStick: Boolean = false) = withContext(Dispatchers.IO) {
    if (isStick) {
        stickEvent.emit(event)
        eventBus.emit(Empty)
    } else {
        eventBus.emit(event)
        stickEvent.emit(Empty)
    }
}

private fun getNotEmpty(a: Event, b: Event) = when {
    a !is Empty -> a
    b !is Empty -> b
    else -> Empty
}

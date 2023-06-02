package com.connor.fuckcolorapp.utils

import com.connor.fuckcolorapp.states.Empty
import com.connor.fuckcolorapp.states.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext

val eventBus = MutableSharedFlow<Event>()
val stickEvent = MutableSharedFlow<Event>(replay = 1)
suspend fun subscribe(block: suspend (Event) -> Unit) {
    eventBus.zip(stickEvent) { a, b -> getNotEmpty(a, b) }.collect(block)
}

suspend fun post(event: Event) = withContext(Dispatchers.IO) {
    val e = async { eventBus.emit(event) }
    val se = async { stickEvent.emit(Empty) }
    e.await(); se.await()
}

suspend fun postWithStick(event: Event) = withContext(Dispatchers.IO) {
    val se = async { stickEvent.emit(event) }
    val e = async { eventBus.emit(Empty) }
    e.await(); se.await()
}

private fun getNotEmpty(a: Event, b: Event) = when {
    a !is Empty -> a
    b !is Empty -> b
    else -> Empty
}

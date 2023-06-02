package com.connor.fuckcolorapp.utils

import com.connor.fuckcolorapp.states.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val eventBus = MutableSharedFlow<Event>()
val stickEvent = MutableSharedFlow<Event>(replay = 1)
fun CoroutineScope.subscribe(block: suspend (Event) -> Unit) {
    launch { eventBus.collect(block) }
    launch { stickEvent.collect(block) }
}

suspend fun post(event: Event, isStick: Boolean = false) = withContext(Dispatchers.IO) {
    if (!isStick) eventBus.emit(event)
    else stickEvent.emit(event)
}
package com.connor.fuckcolorapp.utils

import com.connor.fuckcolorapp.states.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext

val eventBus = MutableSharedFlow<Event>()
suspend fun subscribe(block: suspend (Event) -> Unit) { eventBus.collect(block) }

suspend fun post(event: Event) = withContext(Dispatchers.IO) { eventBus.emit(event) }
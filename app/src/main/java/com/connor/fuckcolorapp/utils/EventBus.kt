package com.connor.fuckcolorapp.utils

import com.connor.fuckcolorapp.states.Event
import kotlinx.coroutines.flow.MutableSharedFlow

private val eventBus = MutableSharedFlow<Event>()
suspend fun subscribe(block: suspend (Event) -> Unit) { eventBus.collect(block) }

suspend fun post(event: Event) { eventBus.emit(event) }
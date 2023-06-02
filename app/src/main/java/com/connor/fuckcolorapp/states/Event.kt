package com.connor.fuckcolorapp.states

sealed interface Event

data class CheckError(val msg: String) : Event

object PureApp : Event
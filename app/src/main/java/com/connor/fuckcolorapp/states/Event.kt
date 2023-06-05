package com.connor.fuckcolorapp.states

sealed interface Event

data class CheckError(val msg: String) : Event
object PureApp : Event

enum class Message : Event {
    T1, T2
}

/**
 * post(Message.T1)
 *
 * subscribe<Message> {
 *    when (it) {
 *      Message.T1 -> showToast(it.name)
 *      Message.T2 -> showToast(it.name)
 *      }
 *  }
 */
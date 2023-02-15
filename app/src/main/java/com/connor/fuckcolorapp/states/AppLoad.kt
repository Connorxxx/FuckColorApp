package com.connor.fuckcolorapp.states

sealed class AppLoad {
    object Loading : AppLoad()
    object Loaded : AppLoad()
}

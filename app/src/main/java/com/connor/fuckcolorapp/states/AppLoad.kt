package com.connor.fuckcolorapp.states

sealed class AppLoad {
    object Loading : AppLoad()
    object UserLoaded : AppLoad()
    object SystemLoaded : AppLoad()
    object AllLoaded : AppLoad()
    object DisableLoaded : AppLoad()
    object All : AppLoad()
}

inline fun AppLoad.onLoading(block: () -> Unit) {
    if (this is AppLoad.Loading) block()
}

inline fun AppLoad.onUserLoaded(block: () -> Unit) {
    if (this is AppLoad.UserLoaded) block()
}

inline fun AppLoad.onSystemLoaded(block: () -> Unit) {
    if (this is AppLoad.SystemLoaded) block()
}

inline fun AppLoad.onAllLoaded(block: () -> Unit) {
    if (this is AppLoad.AllLoaded) block()
}

inline fun AppLoad.onDisableLoaded(block: () -> Unit) {
    if (this is AppLoad.DisableLoaded) block()
}

inline fun AppLoad.onAll(block: () -> Unit) {
    if (this is AppLoad.All) block()
}

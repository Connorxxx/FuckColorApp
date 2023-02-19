package com.connor.fuckcolorapp.states

import com.connor.fuckcolorapp.models.AppInfo

sealed class AppLoad {

    object Nothing: AppLoad()
    object Loading : AppLoad()
    object UserLoaded : AppLoad()
    object SystemLoaded : AppLoad()
    object AllLoaded : AppLoad()
}

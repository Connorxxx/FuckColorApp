package com.connor.fuckcolorapp.viewmodels

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.models.AppInfo
import com.connor.fuckcolorapp.models.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    val appList = ArrayList<AppInfo>()

    init {
        val pm = App.app.packageManager
        viewModelScope.launch {
            repository.queryPackage(PackageManager.MATCH_SYSTEM_ONLY).also { query ->
                query.forEach {
                    appList.add(AppInfo(it.loadLabel(pm), it.activityInfo.packageName, it.loadIcon(pm)))
                }
                appList.sortBy { list -> list.label.toString() }
            }
        }
    }

}
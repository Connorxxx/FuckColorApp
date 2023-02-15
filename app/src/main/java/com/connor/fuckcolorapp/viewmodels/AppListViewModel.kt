package com.connor.fuckcolorapp.viewmodels

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.models.AppInfo
import com.connor.fuckcolorapp.models.Repository
import com.connor.fuckcolorapp.states.AppLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : ViewModel() {

    val appList = ArrayList<AppInfo>()

    private val _appListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val appListState = _appListState.asStateFlow()

    init {
        val pm = application.packageManager
        viewModelScope.launch {
            repository.queryPackage(PackageManager.MATCH_ALL).also { query ->
                query.forEach {
                    appList.add(AppInfo(it.loadLabel(pm), it.activityInfo.packageName, it.loadIcon(pm)))
                }
                appList.sortBy { list -> list.label.toString() }
            }
            _appListState.emit(AppLoad.Loaded)
        }
    }

}
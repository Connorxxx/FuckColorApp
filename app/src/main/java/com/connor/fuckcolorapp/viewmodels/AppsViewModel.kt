package com.connor.fuckcolorapp.viewmodels

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.models.AppInfo
import com.connor.fuckcolorapp.models.Repository
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.ui.fragment.SystemAppFragment
import com.connor.fuckcolorapp.ui.fragment.UserAppFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : ViewModel() {

    private val _appListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val appListState = _appListState.asStateFlow()

    private val _systemListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val systemListState = _systemListState.asStateFlow()

//    val appList = ArrayList<AppInfo>()
//    val systemAppList = ArrayList<AppInfo>()

    private val ResolveInfo.isSystemApp: Boolean
        get() = activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == ApplicationInfo.FLAG_SYSTEM

    init {
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                if (App.userAppList.isEmpty()) {
                    val pm = application.packageManager
                    repository.queryPackage(PackageManager.MATCH_ALL)
                        .filter { !it.isSystemApp }
                        .also { query ->
                            query.forEach { resolveInfo ->
                                App.userAppList.add(
                                    AppInfo(
                                        resolveInfo.loadLabel(pm),
                                        resolveInfo.activityInfo.packageName,
                                        resolveInfo.loadIcon(pm),
                                        true
                                    )
                                )
                            }
                            App.userAppList.sortBy { list -> list.label.toString() }
                        }
                }
                _appListState.emit(AppLoad.UserLoaded)
            }
            launch {
                if (App.systemAppList.isEmpty()) {
                    val pm = application.packageManager
                    repository.queryPackage(PackageManager.MATCH_SYSTEM_ONLY).also { query ->
                        query.forEach { resolveInfo ->
                            App.systemAppList.add(
                                AppInfo(
                                    resolveInfo.loadLabel(pm),
                                    resolveInfo.activityInfo.packageName,
                                    resolveInfo.loadIcon(pm),
                                    false
                                )
                            )
                        }
                        App.systemAppList.sortBy { list -> list.label.toString() }
                    }
                }
                _systemListState.emit(AppLoad.SystemLoaded)
            }
        }
    }

    fun uploadUser() {

    }

    fun uploadSystem() {
        viewModelScope.launch {
            _appListState.emit(AppLoad.SystemLoaded)
        }
    }

    val titles = ArrayList<String>().apply {
        add("User")
        add("System")
    }

    val fragments = ArrayList<Fragment>().apply {
        add(UserAppFragment())
        add(SystemAppFragment())
    }
}
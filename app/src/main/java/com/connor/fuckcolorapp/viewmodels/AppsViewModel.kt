package com.connor.fuckcolorapp.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.models.Repository
import com.connor.fuckcolorapp.states.AppLoad
import com.connor.fuckcolorapp.ui.fragment.SystemAppFragment
import com.connor.fuckcolorapp.ui.fragment.UserAppFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val repository: Repository,
    app: App
) : ViewModel() {

    private val _appListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val appListState = _appListState.asStateFlow()

    private val _systemListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val systemListState = _systemListState.asStateFlow()

    init {
        if (app.userAppList.isEmpty() || app.systemAppList.isEmpty())
            getAppsList()
        else {
            uploadUser()
            uploadSystem()
        }
    }

    fun getAppsList() {
        "getAppsList".logCat()
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                repository.getUserAppList()
                _appListState.emit(AppLoad.UserLoaded)
            }
            launch {
                repository.getSystemAppList()
                _systemListState.emit(AppLoad.SystemLoaded)
            }
        }
    }


    fun setLoading() {
        _appListState.value = AppLoad.Nothing
        _systemListState.value = AppLoad.Nothing
    }

    fun uploadUser() {
        _appListState.value = AppLoad.UserLoaded
    }

    fun uploadSystem() {
        _systemListState.value = AppLoad.SystemLoaded
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
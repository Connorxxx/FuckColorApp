package com.connor.fuckcolorapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.models.Repository
import com.connor.fuckcolorapp.states.AppLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val repository: Repository,
    private val app: App
) : ViewModel() {

    private val _appListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val appListState = _appListState.asStateFlow()

    private val _systemListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val systemListState = _systemListState.asStateFlow()

    private val _allListState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val allListState = _allListState.asStateFlow()

    val hasCheck get() = app.userAppList.any { it.isCheck } || app.systemAppList.any { it.isCheck }  || app.allAppList.any { it.isCheck }

    init {
        if (app.userAppList.isEmpty() || app.systemAppList.isEmpty())
            getAppsList()
        else {
            upload()
        }
    }

    fun getAppsList() {
        viewModelScope.launch(Dispatchers.Default) {
            launch {
                getUser()
            }
            launch {
                getSystem()
            }
            launch {
                getAll()
            }
        }
    }

    fun loadAll() {
        viewModelScope.launch(Dispatchers.Default) {
            getAll()
        }
    }
    private suspend fun getAll() {
        repository.getAllAppList()
        _allListState.emit(AppLoad.AllLoaded)
    }

    fun loadSystem() {
        viewModelScope.launch(Dispatchers.Default) {
            getSystem()
        }
    }
    private suspend fun getSystem() {
        repository.getSystemAppList()
        _systemListState.emit(AppLoad.SystemLoaded)
    }

    fun loadUser() {
        viewModelScope.launch(Dispatchers.Default) {
            getUser()
        }
    }

    private suspend fun getUser() {
        repository.getUserAppList()
        _appListState.emit(AppLoad.UserLoaded)
    }


    fun setUserLoading() {
        _appListState.value = AppLoad.Nothing
    }

    fun setSystemLoading() {
        _systemListState.value = AppLoad.Nothing
    }

    fun setAllAppsLoading() {
        _allListState.value = AppLoad.Nothing
    }

    fun setLoading() {
        _appListState.value = AppLoad.Nothing
        _systemListState.value = AppLoad.Nothing
        _allListState.value = AppLoad.Nothing

    }

    fun upload() {
        _appListState.value = AppLoad.UserLoaded
        _systemListState.value = AppLoad.SystemLoaded
        _allListState.value = AppLoad.AllLoaded
    }

}
package com.connor.fuckcolorapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.models.Repository
import com.connor.fuckcolorapp.states.AppLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val repository: Repository,
    private val app: App
) : ViewModel() {

    private val _listState = MutableStateFlow<AppLoad>(AppLoad.Loading)
    val listState = _listState.asStateFlow()

    private val _listEvent = MutableSharedFlow<AppLoad>()
    val listEvent = _listEvent.asSharedFlow()

    val hasCheck get() = app.userAppList.any { it.isCheck } || app.systemAppList.any { it.isCheck } || app.allAppList.any { it.isCheck }

    init {
        if (app.userAppList.isEmpty() || app.systemAppList.isEmpty())
            getAppsList()
        else {
            upload()
        }
    }

    fun setAppEnable(packageName: String) {
        repository.setAppState(packageName, true)
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
            launch {
                getDisable()
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
        _listEvent.emit(AppLoad.AllLoaded)
    }

    fun loadDisable() {
        viewModelScope.launch(Dispatchers.Default) {
            getDisable()
        }
    }

    private suspend fun getDisable() {
        repository.getDisableList()
        _listEvent.emit(AppLoad.DisableLoaded)
    }

    fun loadSystem() {
        viewModelScope.launch(Dispatchers.Default) {
            getSystem()
        }
    }

    private suspend fun getSystem() {
        repository.getSystemAppList()
        _listEvent.emit(AppLoad.SystemLoaded)
    }

    fun loadUser() {
        viewModelScope.launch(Dispatchers.Default) {
            getUser()
        }
    }

    private suspend fun getUser() {
        repository.getUserAppList()
        _listEvent.emit(AppLoad.UserLoaded)
    }

    private fun upload() {
        viewModelScope.launch {
            _listState.emit(AppLoad.All)
        }
    }

}
package com.connor.fuckcolorapp.viewmodels

import androidx.lifecycle.ViewModel
import com.connor.fuckcolorapp.models.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _permissionState = MutableStateFlow(false)
    val permissionState = _permissionState.asStateFlow()

    init {
        _permissionState.value = repository.checkPermission()
    }

    fun disableApp(packageName: String) {
        if (permissionState.value)
            repository.disableApp(packageName);
    }

    fun uninstallApp(packageName: String) {
        if (permissionState.value)
            repository.uninstallApp(packageName);
    }
}
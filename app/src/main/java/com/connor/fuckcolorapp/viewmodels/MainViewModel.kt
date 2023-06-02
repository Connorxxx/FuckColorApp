package com.connor.fuckcolorapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.fuckcolorapp.models.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    init {
        repository.checkPermission()
    }

    fun checkShizuku(block: () -> Unit) {
        viewModelScope.launch {
            repository.checkShizuku {
                block()
            }
        }
    }
}
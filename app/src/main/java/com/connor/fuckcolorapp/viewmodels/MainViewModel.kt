package com.connor.fuckcolorapp.viewmodels

import androidx.lifecycle.ViewModel
import com.connor.fuckcolorapp.models.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    init {
        repository.checkPermission()
    }

    fun checkShizuku(block: () -> Unit) {
        repository.checkShizuku {
            block()
        }
    }
}
package com.connor.fuckcolorapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.connor.fuckcolorapp.R
import com.connor.fuckcolorapp.databinding.ActivityAppListBinding

class AppListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAppListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)
    }
}
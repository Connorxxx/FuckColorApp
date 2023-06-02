package com.connor.fuckcolorapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.connor.fuckcolorapp.databinding.ActivityMainBinding
import com.connor.fuckcolorapp.datastore.DataStoreManager
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.extension.startActivity
import com.connor.fuckcolorapp.extension.startService
import com.connor.fuckcolorapp.services.PackageService
import com.connor.fuckcolorapp.states.CheckError
import com.connor.fuckcolorapp.states.PureApp
import com.connor.fuckcolorapp.ui.AppsActivity
import com.connor.fuckcolorapp.ui.dialog.AlertDialogFragment
import com.connor.fuckcolorapp.utils.subscribe
import com.connor.fuckcolorapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MainViewModel>()

    private val shizukuPackage by lazy { "moe.shizuku.privileged.api" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.cardStart.setOnClickListener {
            AlertDialogFragment(
                getString(R.string.warning),
                getString(R.string.head_tips),
                getString(R.string.confirm),
                true
            ) {
                viewModel.checkShizuku {
                    binding.tvHead.text = getString(R.string.running)
                    binding.cardStart.isEnabled = false
                    startService<PackageService> {
                        putExtra("is_select", false)
                    }
                }
            }.show(supportFragmentManager, AlertDialogFragment.TAG)
        }
        binding.cardApps.setOnClickListener {
            viewModel.checkShizuku {
                startActivity<AppsActivity> { }
            }
        }
        binding.layoutShizuku.setOnClickListener {
            packageManager.getLaunchIntentForPackage(shizukuPackage)
                ?.let { startActivity(it) }
                ?: startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$shizukuPackage")
                    )
                )
        }
        binding.layoutHelp.setOnClickListener {
            AlertDialogFragment(
                getString(R.string.title),
                getString(R.string.uninstall_detail),
                getString(R.string.ok)
            ) { }.show(supportFragmentManager, AlertDialogFragment.TAG)
        }
        binding.layoutAbout.setOnClickListener {
            AlertDialogFragment(
                getString(R.string.app_name),
                BuildConfig.VERSION_NAME,
                getString(R.string.ok)
            ) { }
                .show(supportFragmentManager, AlertDialogFragment.TAG)
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    dataStoreManager.pureFlow.collect {
                        if (it) {
                            binding.imgHead.load(R.drawable.check_circle)
                            binding.tvHead.text = getString(R.string.finish)
                        } else {
                            binding.imgHead.load(R.drawable.play_circle)
                            binding.tvHead.text = getString(R.string.start)
                        }
                    }
                }
                launch {
                    subscribe {
                        when (it) {
                            is CheckError -> showToast(it.msg)
                            is PureApp -> {
                                dataStoreManager.storePureState(true)
                                binding.cardStart.isEnabled = true
                                binding.tvHead.text = getString(R.string.finish)
                            }
                        }
                    }
                }
            }
        }
    }
}
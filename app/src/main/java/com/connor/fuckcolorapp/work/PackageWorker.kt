package com.connor.fuckcolorapp.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.connor.fuckcolorapp.extension.logCat
import com.connor.fuckcolorapp.extension.showToast
import com.connor.fuckcolorapp.models.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class PackageWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        inputData.getString("PACKAGE_NAME")?.let {
            repository.uninstallAppWithCheck(it).also { b ->
                b.logCat()
            }
        }
        "doWork Success".logCat()
        return Result.success()
    }

}
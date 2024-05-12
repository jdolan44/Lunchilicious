package com.dolanj7.lunchilicious.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dolanj7.lunchilicious.domain.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MenuWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    private val menuDb = MenuDatabase.getDatabase(applicationContext)
    private val menuRepo = MenuRepositoryWebImpl(menuDb)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                periodicLog()
                menuRepo.refresh()
                Result.success()
            } catch (throwable: Throwable) {
                Log.i("Error", throwable.toString())
                Result.failure()
            }
        }
    }
    private suspend fun periodicLog() {
        delay(100)
        Log.i("WORKER: ", System.currentTimeMillis().toString())
    }
}
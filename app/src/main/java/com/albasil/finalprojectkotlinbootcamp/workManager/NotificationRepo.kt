package com.albasil.finalprojectkotlinbootcamp.workManager

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.albasil.finalprojectkotlinbootcamp.MainActivity
import java.util.concurrent.TimeUnit


class NotificationRepo () {
    fun myNotification(mainActivity: MainActivity){
        val myWorkRequest= PeriodicWorkRequest
            .Builder(KoalaWorker::class.java,15, TimeUnit.MINUTES)
            .setInputData(workDataOf(
                "title" to "Koala App",
                "message" to "New Article")
            )
            .build()
        WorkManager.getInstance(mainActivity).enqueueUniquePeriodicWork(
            "periodicStockWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            myWorkRequest
        )
    }
}
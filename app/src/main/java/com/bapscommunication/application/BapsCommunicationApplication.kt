package com.bapscommunication.application

import android.os.PowerManager
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp


open class BapsCommunicationApplication: MultiDexApplication() {
    private lateinit var wakeLock: PowerManager.WakeLock
    override fun onCreate() {
        super.onCreate()

        //Init Of Fabric for CrashLytics
       // Fabric.with(fabric)*/
        FirebaseApp.initializeApp(this)
    }


}
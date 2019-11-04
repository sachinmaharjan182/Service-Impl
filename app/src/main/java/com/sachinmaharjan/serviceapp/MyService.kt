package com.sachinmaharjan.serviceapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log


class MyService : Service() {

    var isRandomNoGeneratorOn = false
    var randomNumber = -1
    var myBinder: IBinder = MyServiceBinder()


     inner class MyServiceBinder : Binder() {
        val service: MyService
            get() = this@MyService
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d(getString(R.string.service_tag),"Service Binded...")
        return myBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(getString(R.string.service_tag),"Service Stopped...")
        stopRandomNumberGenerator()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(getString(R.string.service_tag),"Service Started...")
        isRandomNoGeneratorOn = true
        Thread(Runnable {
           startRandomNumberGenerator()
        }).start()
        return START_STICKY
    }

    fun startRandomNumberGenerator(){
        while (isRandomNoGeneratorOn){
            Thread.sleep(1000)
            randomNumber = (0..100).random()
            Log.d(getString(R.string.service_tag),"Random Number: ${randomNumber}")
        }
    }

    private fun stopRandomNumberGenerator() {
        isRandomNoGeneratorOn = false
    }
}
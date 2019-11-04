package com.sachinmaharjan.serviceapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var serviceIntent: Intent
    lateinit var myService: MyService
    var serviceConnection: ServiceConnection? = null
    var isServiceBound:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(applicationContext,MyService::class.java)

        bt_start_service.setOnClickListener {
            startService(serviceIntent)
        }

        bt_stop_service.setOnClickListener {
            stopService(serviceIntent)
        }

        bt_bind_service.setOnClickListener {
            bindService()
        }

        bt_unbind_service.setOnClickListener { unBindService()}

        bt_get_number.setOnClickListener { setRandomNumber() }
    }

    private fun unBindService() {
        if (isServiceBound){
            Log.d(getString(R.string.service_tag),"Service Unbounded...")
            serviceConnection?.let { unbindService(it) }
            isServiceBound = false
        }
    }

    private fun bindService() {
        if (serviceConnection == null){
            serviceConnection = object : ServiceConnection{
                override fun onServiceDisconnected(p0: ComponentName?) {
                    Log.d(getString(R.string.service_tag),"Service Unbounded...")
                    isServiceBound = false
                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                    Log.d(getString(R.string.service_tag),"Service Bounded...")
                    val myServiceBinder = p1 as MyService.MyServiceBinder
                    myService = p1.service
                    isServiceBound = true
                }

            }
        }

        bindService(serviceIntent, serviceConnection!!,Context.BIND_AUTO_CREATE)
    }

    private fun setRandomNumber() {
        if (isServiceBound){
            tv_number.setText("Random number : ${myService.randomNumber}")
        }else{
            tv_number.setText("Service Not Bound ")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unBindService()
    }
}

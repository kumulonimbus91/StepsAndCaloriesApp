package com.nenadvukojevic.stepsandcaloriesapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants.ACTION_PAUSE_SERVICE
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants.ACTION_STOP_SERVICE
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants.NOTIFICATION_ID
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.MainActivity
import timber.log.Timber

class TrackingService : LifecycleService(), SensorEventListener {

    var isFirstTime = true
    private var sensorManager: SensorManager? = null
    companion object {
        var totalSteps = 0f
    }







    override fun onCreate() { //first time you create service
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (stepSensor == null) {
            Toast.makeText(
                this,
                "No sensor for step counter detected on this device",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }
    private var previousTotalSteps = 0f

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int { //every time service starts(start service method)

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstTime) {
                        startForegroundService()
                        //loadData()
                        //totalSteps = intent.getFloatExtra("steps", 0.0F)
                        isFirstTime = false
                    } else {
                        Timber.d("Resuming service...")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val steps =  prefs.getString("walkingSteps", "0")




        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_directions_walk_24)
            .setContentTitle("Steps")
            .setContentText(steps)
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensor: Sensor = event!!.sensor

        if (sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            totalSteps++
            saveData(totalSteps)
            //val currentSteps = totalSteps.toInt()





        }

    }
    fun saveData(steps: Float) {
        val sharedPrefs =
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.putString("walkingSteps", steps.toString())
        editor?.apply()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    private fun loadData() {
        val sharedPrefs =
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNum: String? = sharedPrefs!!.getString("walkingSteps", "0")
        Log.d("MainActivity", "$savedNum")
        previousTotalSteps = savedNum!!.toFloat()
        totalSteps = previousTotalSteps



    }


}



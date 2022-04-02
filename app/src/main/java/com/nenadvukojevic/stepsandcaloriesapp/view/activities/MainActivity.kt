package com.nenadvukojevic.stepsandcaloriesapp.view.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.ActivityMainBinding
import com.nenadvukojevic.stepsandcaloriesapp.helper.AlarmReceiver


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment


    private lateinit var mNavController: NavController

    private lateinit var context: Context




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT



        context = getApplicationContext();





        supportActionBar?.hide()







        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val mNavController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_calories,
                R.id.navigation_belly,
                R.id.navigation_bmi,
                R.id.add_food_fr

            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)

        mBinding.navView.setupWithNavController(mNavController)









        setAlarm()


    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null)
    }



    private fun setAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000] = pendingIntent
    }




}



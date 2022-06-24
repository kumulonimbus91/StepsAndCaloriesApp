package com.nenadvukojevic.stepsandcaloriesapp.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.nenadvukojevic.stepsandcaloriesapp.ApplicationStepsAndCalories
import com.nenadvukojevic.stepsandcaloriesapp.TrackingService
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabase
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabaseDao
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.MainActivity
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.AddFoodFragment
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.HomeFragment
import java.lang.String
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val dataSource = FoodDatabase.getInstance(MainActivity()).foodDatabaseDao
        val currentCalendar: Calendar = Calendar.getInstance()
        val currentTime: Date = currentCalendar.getTime()
        val setCalendar: Calendar = Calendar.getInstance()
        setCalendar.set(Calendar.HOUR_OF_DAY,0)
        setCalendar.set(Calendar.MINUTE, 0)
        setCalendar.set(Calendar.SECOND, 0)
        val setTime: Date = setCalendar.getTime()

        val sharedPrefs: SharedPreferences =
            context!!.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        var savedNum: kotlin.String? = sharedPrefs!!.getString("walkingSteps", "0")
        val editor = sharedPrefs?.edit()

        if (currentTime.after(setTime)) {
// do another task
        } else {

            dataSource.deleteAll()
            editor.remove("walkingSteps")
            //editor.clear()
            editor?.putString("walkingSteps", "0")
            editor.apply()


        }

    }
}
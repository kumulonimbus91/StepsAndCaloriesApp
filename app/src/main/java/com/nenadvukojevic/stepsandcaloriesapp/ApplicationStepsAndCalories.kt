package com.nenadvukojevic.stepsandcaloriesapp

import android.app.Application
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabase
import timber.log.Timber


/**
 * A application class where we can define the variable scope to use through out the application.
 */

class ApplicationStepsAndCalories: Application (){
 /**Using by lazy so the database and the repository are only created when they're needed
  * rather than when the application starts.
  */
 /**
  * The "lazy" keyword used for creating a new instance that uses the specified initialization function
  * and the default thread-safety mode [LazyThreadSafetyMode.SYNCHRONIZED].
  *
  * If the initialization of a value throws an exception, it will attempt to reinitialize the value at next access.
  *
  * Note that the returned instance uses itself to synchronize on. Do not synchronize from external code on
  * the returned instance as it may cause accidental deadlock. Also this behavior can be changed in the future.
  */

// private val database by lazy { FoodDatabase.getInstance(this) }



 companion object {
  lateinit var instance: ApplicationStepsAndCalories


 }

 override fun onCreate() {
  super.onCreate()
  Timber.plant(Timber.DebugTree())
  instance = this
 }




}
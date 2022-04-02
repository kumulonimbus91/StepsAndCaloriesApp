package com.nenadvukojevic.stepsandcaloriesapp.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabaseDao
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.AddFoodViewModel
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.SharedViewModel

class SharedViewModelFactory(private val dataSource: FoodDatabaseDao,
                             private val application: Application
): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel( dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }




}
package com.nenadvukojevic.stepsandcaloriesapp.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabaseDao
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodModel
import com.nenadvukojevic.stepsandcaloriesapp.model.entities.Food
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.getCurrentDayString
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.MainActivity
import kotlinx.coroutines.*

class AddFoodViewModel(
    food: Food,
    val datasource: FoodDatabaseDao,
    app: Application
) : AndroidViewModel(app) {

    /** COROUTINES */
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    /** BINDABLES */
    private val _selectedFood = MutableLiveData<Food>()

    val selectedFood: LiveData<Food>
        get() = _selectedFood

    val currentGramsString = MutableLiveData<String>()

    // Navigation Back to Overview
    val navigateToOverview = MutableLiveData<Boolean>()


    init {
        navigateToOverview.value = false
        _selectedFood.value = food
        currentGramsString.value = "100"
    }

    /** UI's LiveData */

    val displayKcalPer100G = Transformations.map(selectedFood) { food ->
        app.applicationContext.getString(R.string.display_kcal_per_100g, food.nutrients.kcal)
    }


    val displayCurrentCarbs = Transformations.map(currentGramsString) { gramsString ->
        val carbsPerOneGram = selectedFood.value!!.nutrients.carbs / 100

        if (gramsString.isEmpty()) {
            app.applicationContext.getString(R.string.format_grams, "0".toDouble())
        } else {
            app.applicationContext.getString(
                R.string.format_grams,
                gramsString.toDouble().times(carbsPerOneGram)
            )
        }
    }

    val displayCurrentProteins = Transformations.map(currentGramsString) { gramsString ->
        val proteinsPerOneGram = selectedFood.value!!.nutrients.protein / 100

        if (gramsString.isEmpty()) {
            app.applicationContext.getString(R.string.format_grams, "0".toDouble())
        } else {
            app.applicationContext.getString(
                R.string.format_grams,
                gramsString.toDouble().times(proteinsPerOneGram)
            )
        }
    }

    val displayCurrentFats = Transformations.map(currentGramsString) { gramsString ->
        val fatsPerOneGram = selectedFood.value!!.nutrients.fat / 100

        if (gramsString.isEmpty()) {
            app.applicationContext.getString(R.string.format_grams, "0".toDouble())
        } else {
            app.applicationContext.getString(
                R.string.format_grams,
                gramsString.toDouble().times(fatsPerOneGram)
            )
        }
    }

    val displayCurrentTotalKcal = Transformations.map(currentGramsString) { gramsString ->
        val kcalPerOneGram = selectedFood.value!!.nutrients.kcal / 100

        if (gramsString.isEmpty()) {
            app.applicationContext.getString(R.string.format_total_kcal, "0".toDouble())
        } else {
            app.applicationContext.getString(
                R.string.format_total_kcal,
                gramsString.toDouble().times(kcalPerOneGram)
            )
        }
    }

    val displayCarbsPercent = Transformations.map(selectedFood) { food ->
        app.applicationContext.getString(R.string.format_percent, food.nutrients.carbsPercent)
    }

    val displayProteinsPercent = Transformations.map(selectedFood) { food ->
        app.applicationContext.getString(R.string.format_percent, food.nutrients.proteinPercent)
    }

    val displayFatsPercent = Transformations.map(selectedFood) { food ->
        app.applicationContext.getString(R.string.format_percent, food.nutrients.fatPercent)
    }

    /** Database */

    private suspend fun insert(foodModel: FoodModel) {
        withContext(Dispatchers.IO) {
            datasource.insert(foodModel)
        }
    }

    fun onAddFoodSave() {
        uiScope.launch {
            val carbsPerOneGram = selectedFood.value!!.nutrients.carbs / 100
            val proteinsPerOneGram = selectedFood.value!!.nutrients.protein / 100
            val fatsPerOneGram = selectedFood.value!!.nutrients.fat / 100
            val kcalPerOneGram = selectedFood.value!!.nutrients.kcal / 100


            val currentGrams = currentGramsString.value!!.toInt()

            val foodModel = FoodModel(
                name = selectedFood.value?.layoutName,
                grams = currentGrams.toDouble(),
                carbs = currentGrams.times(carbsPerOneGram),
                proteins = currentGrams.times(proteinsPerOneGram),
                fats = currentGrams.times(fatsPerOneGram),
                kcal = currentGrams.times(kcalPerOneGram).toInt(),
                date = getCurrentDayString()

            )
            showToast(getApplication())
            insert(foodModel)

        }


    }

    override fun onCleared() {
        super.onCleared()
        // cancel all coroutines
        viewModelJob.cancel()
    }

    fun showToast(context: Context) {
        Toast.makeText(context, "Food added", Toast.LENGTH_SHORT).show()
    }


}
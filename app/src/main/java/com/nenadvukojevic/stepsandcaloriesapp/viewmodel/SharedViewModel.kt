package com.nenadvukojevic.stepsandcaloriesapp.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.getCurrentDayString
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabaseDao
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodModel
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.HomeFragment
import kotlinx.coroutines.*

class SharedViewModel(
    val database: FoodDatabaseDao,
    app: Application
) : AndroidViewModel(app) {

    private var calories = MutableLiveData<Int>(0)
    val caloriesToRead: LiveData<Int> = calories

    fun saveCalories(caloriesNum: Int) {
        calories.value = caloriesNum

    }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var dateSelected = MutableLiveData(getCurrentDayString())
    fun setDateSelected(newDate: String) {
        dateSelected.value = newDate
    }

    val foods = Transformations.switchMap(dateSelected) { date ->
        database.getAllFoodFromDay(date)
    }


    val foodTotal = Transformations.map(foods) { foods ->
        var gramsTotal = 0.0
        var carbsTotal = 0.0
        var proteinsTotal = 0.0
        var fatsTotal = 0.0
        var kcalTotal = 0.0

        for (food in foods) {
            gramsTotal += food.grams
            carbsTotal += food.carbs
            proteinsTotal += food.proteins
            fatsTotal += food.fats
            kcalTotal += food.kcal
        }

        FoodModel(
            name = "",
            grams = gramsTotal,
            carbs = carbsTotal,
            proteins = proteinsTotal,
            fats = fatsTotal,
            kcal = kcalTotal.toInt(),
            date = ""

        )

    }

    val displayTotalKcal = Transformations.map(foodTotal) { food ->
        app.applicationContext.getString(R.string.format_double, food.kcal)
    }
    val displayTotalCarbs = Transformations.map(foodTotal) { food ->
        app.applicationContext.getString(R.string.format_double, food.carbs)
    }

    val displayTotalProteins = Transformations.map(foodTotal) { food ->
        app.applicationContext.getString(R.string.format_double, food.proteins)
    }

    val displayTotalFats = Transformations.map(foodTotal) { food ->
        app.applicationContext.getString(R.string.format_double, food.fats)
    }

    val displayCarbsPercent = Transformations.map(foodTotal) { food ->
        app.applicationContext.getString(R.string.format_percent, food.carbsPercent)
    }

    val displayProteinsPercent = Transformations.map(foodTotal) { food ->
        app.applicationContext.getString(R.string.format_percent, food.proteinPercent)
    }

    val displayFatsPercent = Transformations.map(foodTotal) { food ->
        app.applicationContext.getString(R.string.format_percent, food.fatPercent)
    }


    private suspend fun deleteFood(foodModel: FoodModel) {
        withContext(Dispatchers.IO) {
            database.deleteFood(foodModel)
        }
    }

    fun onDeleteChoosedFood(foodModel: FoodModel) {
        uiScope.launch {
            deleteFood(foodModel)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // cancel all coroutines
        viewModelJob.cancel()
    }

    fun calculateCaloriesMetric(
        gender: String?,
        weightMetric: Int?,
        heightMetric: Int?,
        age: Int?,
        goal: String?
    ): Int {

        var amrInt: Int = 0
        var resultInt: Int = 0




        if (gender == "Male") {
            if (goal == "LOSE") { // the problem is that lose is always not null, therefore it goes to lose whatever user selects in radio button.
                val result: Double =
                    (10 * weightMetric!!) + (6.25 * heightMetric!!) - (5 * age!!) + 5
                resultInt = result.toInt()
                val amr = (resultInt * 1.15)
                amrInt = amr.toInt()
                return amrInt

            } else if (goal == "LOSEHEAVY") {
                val result: Double =
                    (10 * weightMetric!!) + (6.25 * heightMetric!!) - (5 * age!!) + 5
                resultInt = result.toInt()
                val amr = (resultInt * 1.1)
                amrInt = amr.toInt()
                return amrInt

            } else if (goal == "MAINTAIN") {
                val result: Double =
                    (10 * weightMetric!!) + (6.25 * heightMetric!!) - (5 * age!!) + 5
                resultInt = result.toInt()
                val amr = (resultInt * 1.28)
                amrInt = amr.toInt()
                return amrInt

            }


        } else if (gender == "Female") {
            if (goal == "LOSE") {
                val result: Double =
                    (10 * weightMetric!!) + (6.25 * heightMetric!!) - (5 * age!!) - 161
                resultInt = result.toInt()
                val amr = (resultInt * 1.15)
                amrInt = amr.toInt()
                return amrInt

            } else if (goal == "LOSEHEAVY") {
                val result: Double =
                    (10 * weightMetric!!) + (6.25 * heightMetric!!) - (5 * age!!) - 161
                resultInt = result.toInt()
                val amr = (resultInt * 1.1)
                amrInt = amr.toInt()
                return amrInt

            } else if (goal == "MAINTAIN") {
                val result: Double =
                    (10 * weightMetric!!) + (6.25 * heightMetric!!) - (5 * age!!) - 161
                resultInt = result.toInt()
                val amr = (resultInt * 1.28)
                amrInt = amr.toInt()

                return amrInt
            }


        }
        return amrInt

    }

    fun calculateCaloriesUs(
        gender: String?,
        weightUs: Int?,
        heightUs: Int?,
        age: Int?,
        goal: String?
    ): Int {

        var resultInt = 0
        var amrInt = 0


        if (gender == "Male") {
            if (goal == "LOSE") {
                val result: Double =
                    (66.47 + (6.24 * weightUs!!) + (12.7 * heightUs!!) - (6.75 * age!!))
                resultInt = result.toInt()
                val amr = resultInt * 1.15
                amrInt = amr.toInt()
                return amrInt

            } else if (goal == "LOSEHEAVY") {
                val result: Double =
                    (66 + (6.2 * weightUs!!) + (12.7 * heightUs!!) - (6.75 * age!!))
                resultInt = result.toInt()
                val AMR = resultInt * 1.1
                amrInt = AMR.toInt()
                return amrInt
            } else if (goal == "MAINTAIN") {
                val result: Double =
                    (66 + (6.2 * weightUs!!) + (12.7 * heightUs!!) - (6.75 * age!!))
                resultInt = result.toInt()
                val AMR = resultInt * 1.28
                amrInt = AMR.toInt()
                return amrInt
            }

        } else if (gender == "Female") {
            if (goal == "LOSE") {
                val result: Double =
                    (655 + (4.35 * weightUs!!) + (4.7 * heightUs!!) - (4.7 * age!!))
                resultInt = result.toInt()
                val AMR = resultInt * 1.15
                amrInt = AMR.toInt()
                return amrInt

            } else if (goal == "LOSEHEAVY") {
                val result: Double =
                    (655 + (4.35 * weightUs!!) + (4.7 * heightUs!!) - (4.7 * age!!))
                resultInt = result.toInt()
                val AMR = resultInt * 1.1
                amrInt = AMR.toInt()
                return amrInt
            } else if (goal == "MAINTAIN") {
                val result: Double =
                    (655 + (4.35 * weightUs!!) + (4.7 * heightUs!!) - (4.7 * age!!))
                resultInt = result.toInt()
                val AMR = resultInt * 1.28
                amrInt = AMR.toInt()
                return amrInt
            }


        }
        return amrInt
    }

    fun calculateSteps(goal: String?): Int {

        var steps: Int = 0

        if (goal == "LOSE") {
            steps = 10000
            return steps

        } else if (goal == "LOSEHEAVY") {
            steps = 13000
            return steps
        } else if (goal == "MAINTAIN") {
            steps = 6000
            return steps
        }
        return steps
    }

    fun percentageOfGoalSteps(steps: Int, goal: Int): Int {
        val percentage: Int = (100 * steps) / goal


        return percentage


    }

    fun percentageOfGoalCalories(calories: Int, goal: Int): Int {
        val percentage: Int = (100 * calories) / goal


        return percentage


    }

    fun saveData(steps: Float) {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.putString("walkingSteps", steps.toString())
        editor?.apply()
    }

    fun burnedCalories(steps: Int): Int {
        val burnedKcal: Double = steps * 0.045
        val result = burnedKcal.toInt()
        return result
    }


}







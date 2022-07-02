package com.nenadvukojevic.stepsandcaloriesapp.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.nenadvukojevic.stepsandcaloriesapp.model.network.ApiService
import com.nenadvukojevic.stepsandcaloriesapp.model.entities.Food
import com.nenadvukojevic.stepsandcaloriesapp.utils.Constants
import kotlinx.coroutines.launch

class CaloriesViewModel : ViewModel() {


    val word = MutableLiveData<String>() // connected in EditText XML fragment_calories

    private val _response = MutableLiveData<String>()

    private val _searchListFood = MutableLiveData<List<Food>>()

    val searchListFood: LiveData<List<Food>>
        get() = _searchListFood

    private val _navigateToSelectedFood = MutableLiveData<Food>()

    val navigateToSelectedFood: MutableLiveData<Food>
        get() = _navigateToSelectedFood

    val searchInProgress = MutableLiveData<Boolean>()

    val showFoodNotFound = MutableLiveData<Boolean>()

    init {
        searchInProgress.value = false
        showFoodNotFound.value = false
    }


    fun getSearchFoodResponse() {
        if (word.value == "" || word.value == null)
            return
        searchInProgress.value = true
        viewModelScope.launch {
            val responseDeffered = ApiService.retrofitService.getSpecificFoodAsync(
                word.value ?: "",
                Constants.APP_ID,
                Constants.APP_KEY
            )
            try {
                val responseJson = responseDeffered.await()
                val hintsList = responseJson.hints
                searchInProgress.value = false
                showFoodNotFound.value = false
                val auxFoodList: MutableList<Food> = mutableListOf()
                for (hint in hintsList) {
                    auxFoodList.add(hint.food)
                }
                _searchListFood.value = auxFoodList

            } catch (e: Exception) {
                searchInProgress.value = false
                showFoodNotFound.value = true
                _response.value = "Failure: ${e.message}"
            }

        }


    }

    /**
     * When the property is clicked, set the [_navigateToSelectedFood] [MutableLiveData]
     * @param food that was clicked
     */
    fun displayAddFood(food: Food) {
        _navigateToSelectedFood.value = food
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedFood is set to null
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun displayAddFoodIsComplete() {
        _navigateToSelectedFood.value = null
    }


}
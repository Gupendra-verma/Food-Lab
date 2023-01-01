package com.example.foodlab.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodlab.DB.MealDatabase
import com.example.foodlab.pojo.*
import com.example.foodlab.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase):ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()

    private var saveStateRandomMeal:Meal? = null

    fun getRandomMeal(){
        saveStateRandomMeal?.let { randomMeal->
            randomMealLiveData.postValue(randomMeal)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!=null){
                    val randomMeal:Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveStateRandomMeal = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body()!=null){
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{

            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
//                if(response.body()!=null){
//                    categoryLiveData.value = response.body()!!.categories
//                }
                response.body()?.let {
                    categoryList ->
                    categoryLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun observeCategories():LiveData<List<Category>>{
        return categoryLiveData
    }

    fun observePopularItems():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeRandomMeal():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observeFavoriteMeals():LiveData<List<Meal>>{
        return favoriteMealsLiveData
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}
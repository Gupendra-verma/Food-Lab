package com.example.foodlab.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodlab.DB.MealDatabase
import com.example.foodlab.pojo.Meal
import com.example.foodlab.pojo.MealList
import com.example.foodlab.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: MealDatabase):ViewModel() {
    val mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){

        RetrofitInstance.api.getMealDetail(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!=null){
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity",t.message.toString())
            }

        })
    }

    fun observeMealDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun isMealSavedInDatabase(mealId: String): Boolean {
        var meal: Meal? = null
        viewModelScope.launch {
            meal = mealDatabase.mealDao().getMealById(mealId)
        }

        if(meal==null)
            return false

        return true

    }

    fun deleteMeal(mealId:String){
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMealById(mealId)
        }

    }

}
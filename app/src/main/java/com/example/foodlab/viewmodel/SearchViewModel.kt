package com.example.foodlab.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodlab.pojo.MealsByCategory
import com.example.foodlab.pojo.MealsByCategoryList
import com.example.foodlab.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchViewModel():ViewModel() {
    private var searchedMealLiveData = MutableLiveData<List<MealsByCategory>>()

    fun searchedMealDetail(name:String, context: Context?){
        RetrofitInstance.api.getMealByName(name).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                if(response.body()?.meals==null){
                    Toast.makeText(context?.applicationContext,"No such a meal",Toast.LENGTH_SHORT).show()
                }else{
                    searchedMealLiveData.postValue(response.body()!!.meals)
                }
            }
            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("SearchFragment",t.message.toString())
            }

        })

        }

    fun observeSearchedMeal():LiveData<List<MealsByCategory>>{
        return searchedMealLiveData
    }


}
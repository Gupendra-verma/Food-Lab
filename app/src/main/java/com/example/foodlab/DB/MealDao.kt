package com.example.foodlab.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodlab.pojo.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal:Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>>

    @Query("SELECT * FROM mealInformation WHERE idMeal =:id")
    fun getMealById(id:String):Meal

    @Query("Delete FROM mealInformation WHERE idMeal =:id")
    fun deleteMealById(id:String)
}
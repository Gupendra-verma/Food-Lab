package com.example.foodlab.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodlab.adapters.CategoryMealsAdapter
import com.example.foodlab.databinding.ActivityCategoryMealsBinding
import com.example.foodlab.fragments.HomeFragment
import com.example.foodlab.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_ID
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_NAME
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_THUMB
import com.example.foodlab.pojo.MealsByCategory
import com.example.foodlab.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel:CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val categoryName = intent.getStringExtra(CATEGORY_NAME)!!
        binding.tvCategoryCount.text = categoryName
        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(categoryName)

        prepareCategoryRecycleView()
        observeMealsCategory()
        onMealClick()

        }

    private fun onMealClick() {
        categoryMealsAdapter.onItemClick = { meals->
            val intent = Intent(applicationContext,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meals.idMeal)
            intent.putExtra(MEAL_NAME,meals.strMeal)
            intent.putExtra(MEAL_THUMB,meals.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareCategoryRecycleView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter

        }
    }

    private fun observeMealsCategory() {
        categoryMealsViewModel.observeMealsLiveData().observe(this) { meals ->
            categoryMealsAdapter.setMealsList(meals as ArrayList<MealsByCategory>)
        }
    }


}

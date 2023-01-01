package com.example.foodlab.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.foodlab.DB.MealDatabase
import com.example.foodlab.R
import com.example.foodlab.databinding.ActivityMealBinding
import com.example.foodlab.fragments.HomeFragment
import com.example.foodlab.pojo.Meal
import com.example.foodlab.viewmodel.MealViewModel
import com.example.foodlab.viewmodel.MealViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MealActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMealBinding
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm:MealViewModel
    private lateinit var savedMeal:Meal



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)

        mealMvvm = ViewModelProvider(this,mealViewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInView()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailsLiveData()
        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnFavLotti.setOnClickListener {

//            if(isMealSavedInDatabase()){
//
//                binding.btnFavLotti.apply {
//                    setMinAndMaxProgress(0.5f,1.0f)
//                    playAnimation()
//                }
//
//                deleteMeal()
//                Snackbar.make(
//                    findViewById(android.R.id.content),
//                    "Meal was deleted",
//                    Snackbar.LENGTH_SHORT).show()
//
//            }else{
                binding.btnFavLotti.apply {
                    setMinAndMaxProgress(0.0f,0.5f)
                    playAnimation()
                }

                savedMeal.let {
                    mealMvvm.insertMeal(it)
                    Toast.makeText(this,"Meal saved",Toast.LENGTH_SHORT).show()
                }
//            }


        }
    }

    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this
        ) {
            onResponseCase()
            val meal = it
            savedMeal = meal
            binding.tvCategory.text = "Category : ${meal.strCategory}"
            binding.tvArea.text = "Area : ${meal.strArea}"
            binding.tvInstructionSteps.text = meal.strInstructions
            youtubeLink = meal.strYoutube!!

        }
    }

    private fun setInformationInView() {

        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.igMealDetails)


        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))


    }

    private fun getMealInformationFromIntent() {
        val intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun onYoutubeImageClick(){
        binding.igYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun isMealSavedInDatabase(): Boolean {
        return mealMvvm.isMealSavedInDatabase(mealId)
    }

    private fun deleteMeal(){
        mealMvvm.deleteMeal(mealId)
    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.btnFavLotti.visibility = View.INVISIBLE
        binding.igYoutube.visibility = View.INVISIBLE


    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.btnFavLotti.visibility = View.VISIBLE
        binding.igYoutube.visibility = View.VISIBLE

    }
}
package com.example.foodlab.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodlab.R
import com.example.foodlab.activities.CategoryMealsActivity
import com.example.foodlab.activities.MainActivity
import com.example.foodlab.activities.MealActivity
import com.example.foodlab.adapters.CategoriesAdapter
import com.example.foodlab.adapters.MostPopularAdapter
import com.example.foodlab.databinding.FragmentHomeBinding
import com.example.foodlab.pojo.Category
import com.example.foodlab.pojo.Meal
import com.example.foodlab.pojo.MealsByCategory
import com.example.foodlab.viewmodel.HomeViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainViewModel: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter:MostPopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.example.foodlab.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodlab.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodlab.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodlab.fragments.nameCategory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = (activity as MainActivity).mainViewModel


        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoadingCase()
        preparePopularItemsRecyclerView()
        prepareCategoriesRecyclerView()
        mainViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        mainViewModel.getPopularItems()
        observePopularItems()
        onPopularItemClick()

        mainViewModel.getCategories()
        observeCategories()
        onCategoryClick()

        onSearchIconClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategories() {
        mainViewModel.observeCategories().observe(viewLifecycleOwner)
        { categories->

            categoriesAdapter.setCategoryList(categories as ArrayList<Category>)
            cancelLoadingCase()
        }

    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meals ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meals.idMeal)
            intent.putExtra(MEAL_NAME, meals.strMeal)
            intent.putExtra(MEAL_THUMB, meals.strMealThumb)

            startActivity(intent)

        }
    }

    private fun prepareCategoriesRecyclerView(){
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(activity,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }
    private fun preparePopularItemsRecyclerView() {
        binding.recViewPopularMeals.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }

    }

    private fun observePopularItems() {
        mainViewModel.observePopularItems().observe(viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)

        }
    }

    private fun onRandomMealClick() {
        binding.cardRandomMeal.setOnClickListener{
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        mainViewModel.observeRandomMeal().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.igRandomMeal)

            randomMeal = meal
            cancelLoadingCase()
        }
    }

    private fun onSearchIconClick(){
        binding.igSearch.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun showLoadingCase(){
        binding.apply {
            headerLinear.visibility = View.INVISIBLE
            tvWhatWouldYou.visibility = View.INVISIBLE
            cardRandomMeal.visibility = View.INVISIBLE
            tvOverPopularItems.visibility = View.INVISIBLE
            recViewPopularMeals.visibility = View.INVISIBLE
            tvCategories.visibility = View.INVISIBLE
            cardCategories.visibility = View.INVISIBLE
            loadingAnimation.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun cancelLoadingCase(){
        binding.apply {
            headerLinear.visibility = View.VISIBLE
            tvWhatWouldYou.visibility = View.VISIBLE
            cardRandomMeal.visibility = View.VISIBLE
            tvOverPopularItems.visibility = View.VISIBLE
            recViewPopularMeals.visibility = View.VISIBLE
            tvCategories.visibility = View.VISIBLE
            cardCategories.visibility = View.VISIBLE
            loadingAnimation.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

}
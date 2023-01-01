package com.example.foodlab.fragments

import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodlab.R
import com.example.foodlab.activities.MealActivity
import com.example.foodlab.adapters.CategoryMealsAdapter
import com.example.foodlab.databinding.FragmentSearchBinding
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_ID
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_NAME
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_THUMB
import com.example.foodlab.pojo.MealsByCategory
import com.example.foodlab.viewmodel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var searchedMealAdapter:CategoryMealsAdapter
    private lateinit var searchViewModel:SearchViewModel
    private lateinit var navBar:BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchedMealAdapter = CategoryMealsAdapter()
        searchViewModel = ViewModelProviders.of(this)[SearchViewModel::class.java]

        navBar = requireActivity().findViewById(R.id.btm_nav)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)

        binding.searchView.requestFocus()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        searchQuery()
        prepareSearchedMealRecyclerView()
        observeSearchLiveData()
        onMealClick()

    }



    private fun observeSearchLiveData() {
        searchViewModel.observeSearchedMeal().observe(viewLifecycleOwner){mealList ->
            searchedMealAdapter.setMealsList(mealList as ArrayList<MealsByCategory>)

        }
    }

    private fun prepareSearchedMealRecyclerView() {
        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(context, 2,GridLayoutManager.VERTICAL,false)
            adapter = searchedMealAdapter
        }
    }

    private fun searchQuery(){

        binding.searchView.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                navBar.visibility = View.GONE
            }else{
                navBar.visibility = View.VISIBLE
            }
        }

        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                searchViewModel.searchedMealDetail(p0!!,context)
                return true
            }

        })
    }

    private fun onMealClick() {
        searchedMealAdapter.onItemClick = { meals->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meals.idMeal)
            intent.putExtra(MEAL_NAME,meals.strMeal)
            intent.putExtra(MEAL_THUMB,meals.strMealThumb)
            startActivity(intent)
        }
    }
}
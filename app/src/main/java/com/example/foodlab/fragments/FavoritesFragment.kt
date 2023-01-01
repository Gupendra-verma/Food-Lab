package com.example.foodlab.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodlab.activities.MainActivity
import com.example.foodlab.activities.MealActivity
import com.example.foodlab.adapters.FavoritesMealsAdapter
import com.example.foodlab.databinding.FragmentFavoritesBinding
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_ID
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_NAME
import com.example.foodlab.fragments.HomeFragment.Companion.MEAL_THUMB
import com.example.foodlab.pojo.Meal
import com.example.foodlab.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding:FragmentFavoritesBinding
    private lateinit var mainViewModel: HomeViewModel
    private lateinit var favoritesMealsAdapter: FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = (activity as MainActivity).mainViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavoriteMeal()
        prepareFavoriteRecyclerView()
        onMealClick()

        val itemTouchHelper = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ):Boolean{ return true}

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favoriteMeal = favoritesMealsAdapter.differ.currentList[position]
                mainViewModel.deleteMeal(favoriteMeal)
                showDeleteSnackBar(favoriteMeal)
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)

    }

    private fun showDeleteSnackBar(favoriteMeal: Meal) {
        Snackbar.make(requireView(),"Meal was deleted",Snackbar.LENGTH_LONG).apply {
            setAction("undo",View.OnClickListener {
                mainViewModel.insertMeal(favoriteMeal)
            }).show()
        }
    }

    private fun prepareFavoriteRecyclerView() {
        favoritesMealsAdapter = FavoritesMealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesMealsAdapter
        }
    }

    private fun observeFavoriteMeal() {
        mainViewModel.observeFavoriteMeals().observe(viewLifecycleOwner){ meals->
            favoritesMealsAdapter.differ.submitList(meals)
            if(meals.isEmpty()){
                binding.tvNoFavorites.visibility = View.VISIBLE
            }else{
                binding.tvNoFavorites.visibility = View.GONE
            }

        }
    }

    private fun onMealClick() {
        favoritesMealsAdapter.onItemClick = { meals->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meals.idMeal)
            intent.putExtra(MEAL_NAME,meals.strMeal)
            intent.putExtra(MEAL_THUMB,meals.strMealThumb)
            startActivity(intent)
        }
    }
}
package com.example.foodlab.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodlab.R
import com.example.foodlab.activities.CategoryMealsActivity
import com.example.foodlab.activities.MainActivity
import com.example.foodlab.adapters.CategoriesAdapter
import com.example.foodlab.databinding.FragmentCategoriesBinding
import com.example.foodlab.pojo.Category
import com.example.foodlab.viewmodel.HomeViewModel

class CategoriesFragment : Fragment() {
  lateinit var binding:FragmentCategoriesBinding
  private lateinit var categoryAdapter:CategoriesAdapter
  private lateinit var mainMVVM:HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainMVVM = (activity as MainActivity).mainViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareCategoryRecyclerView()
        observeCategory()
        onCategoryClick()

    }

    private fun observeCategory() {
        mainMVVM.observeCategories().observe(viewLifecycleOwner){categories->
            categoryAdapter.setCategoryList(categories as ArrayList<Category>)
        }
    }

    private fun prepareCategoryRecyclerView() {
        categoryAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(activity,3,GridLayoutManager.VERTICAL,false)
            adapter = categoryAdapter

        }
    }

    private fun onCategoryClick() {
        categoryAdapter.onItemClick = { category->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }
}
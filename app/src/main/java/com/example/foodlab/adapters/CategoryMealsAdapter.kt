package com.example.foodlab.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.foodlab.databinding.MealsItemBinding
import com.example.foodlab.pojo.Category
import com.example.foodlab.pojo.MealsByCategory
import com.example.foodlab.pojo.MealsByCategoryList

class CategoryMealsAdapter():RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder> (){

    lateinit var onItemClick:((MealsByCategory)-> Unit)
    private var mealsList = ArrayList<MealsByCategory>()
    fun setMealsList(mealsList:ArrayList<MealsByCategory>){
        this.mealsList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(MealsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
      Glide.with(holder.itemView)
          .load(mealsList[position].strMealThumb)
          .into(holder.binding.igMeal)

        holder.binding.tvMealName.text = mealsList[position].strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    class CategoryMealsViewHolder(val binding:MealsItemBinding):ViewHolder(binding.root)
}
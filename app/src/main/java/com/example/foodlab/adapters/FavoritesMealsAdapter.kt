package com.example.foodlab.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.foodlab.databinding.MealsItemBinding
import com.example.foodlab.pojo.Meal
import com.example.foodlab.pojo.MealsByCategory

class FavoritesMealsAdapter():RecyclerView.Adapter<FavoritesMealsAdapter.MealsViewHolder>() {

    lateinit var onItemClick:((Meal)-> Unit)
    private val diffUtil = object :DiffUtil.ItemCallback<Meal>(){

        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    class MealsViewHolder(val binding: MealsItemBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        return MealsViewHolder(MealsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val meal = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.igMeal)

        holder.binding.tvMealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
package com.example.foodlab.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodlab.databinding.CategoryItemBinding
import com.example.foodlab.pojo.Category
import com.example.foodlab.pojo.CategoryList

class CategoriesAdapter():RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private var categoriesList = ArrayList<Category>()
     lateinit var onItemClick:((Category)-> Unit)

    fun setCategoryList(categoriesList:ArrayList<Category>) {
        this.categoriesList = categoriesList
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoriesList[position].strCategoryThumb)
            .into(holder.binding.igCategory)

        holder.binding.tvCategoryName.text = categoriesList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick.invoke(categoriesList[position])
        }
    }

    override fun getItemCount(): Int {
      return categoriesList.size
    }

    class CategoriesViewHolder(val binding:CategoryItemBinding):RecyclerView.ViewHolder(binding.root)
}
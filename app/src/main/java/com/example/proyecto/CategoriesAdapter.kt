package com.example.proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter(
private val categories: List<String>, // Lista de categorías
private val onCategoryClick: (String) -> Unit // Acción al hacer clic en una categoría
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.textViewCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category
        holder.itemView.setOnClickListener {
            onCategoryClick(category) // Llamar a la acción cuando se haga clic
        }
    }

    override fun getItemCount(): Int = categories.size
}



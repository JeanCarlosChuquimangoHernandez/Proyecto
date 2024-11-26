package com.example.proyecto

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCategories)

        // Configura el RecyclerView para mostrar categorías
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL ,
            false
        )
        val dbHelper = DatabaseHelper(requireContext())
        val categories = dbHelper.getCategories() // Obtener categorías desde la base de datos

        // Configura el adaptador con la lista de categorías
        recyclerView.adapter = CategoriesAdapter(categories) { category ->
            openCategory(category) // Acción al hacer clic en una categoría
        }
        val itemSpacing = 16 // Espacio en píxeles
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = itemSpacing // Espacio debajo de cada ítem
                outRect.top = itemSpacing // Espacio encima de cada ítem (opcional)
                outRect.right = itemSpacing // Espacio a la derecha
                outRect.left = itemSpacing // Espacio a la izquierda
            }
        })
        return view
    }

    private fun openCategory(category: String) {
        Log.d("CategoriesFragment", "Abriendo categoría: $category")
        val fragment = ProductsFragment.newInstance(category)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}


package com.example.proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home_fragment : Fragment() {

    private lateinit var productAdapter: ProductsAdapter
    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): Home_fragment {
            val fragment = Home_fragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        // Inicializa el DatabaseHelper
        databaseHelper = DatabaseHelper(requireContext())

        // Configura el RecyclerView de Productos
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts)
        recyclerViewProducts.layoutManager = GridLayoutManager(requireContext(), 2)

        // Obtener la categoría seleccionada del argumento
        val category = arguments?.getString(ARG_CATEGORY) ?: "Todos"

        // Obtener productos de la base de datos según la categoría
        val products = if (category == "Todos") {
            databaseHelper.getProducts().toMutableList() // Convertimos a MutableList
        } else {
            databaseHelper.getProductsForCategory(category).toMutableList() // Convertimos a MutableList
        }

        // Configura el adaptador de productos
        productAdapter = ProductsAdapter(
            products = products,
            isCartMode = false, // Modo general (no carrito)
            onAddToCartClick = { product ->
                Cart.addProduct(product) // Agregar producto al carrito
                Toast.makeText(
                    requireContext(),
                    "Producto agregado: ${product.title}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onIncreaseQuantityClick = null, // No se utiliza en el Home
            onDecreaseQuantityClick = null, // No se utiliza en el Home
            onUpdateTotal = {} // No se utiliza en el Home
        )
        recyclerViewProducts.adapter = productAdapter

        return view
    }
}







package com.example.proyecto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsFragment : Fragment() {

    private lateinit var adapter: ProductsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private val TAG = "ProductsFragment"

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String?): ProductsFragment {
            val fragment = ProductsFragment()
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
        Log.d(TAG, "onCreateView iniciado")
        val view = inflater.inflate(R.layout.fragment_products, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        Log.d(TAG, "RecyclerView configurado")

        databaseHelper = DatabaseHelper(requireContext())
        val category = arguments?.getString(ARG_CATEGORY)
        Log.d(TAG, "Categoría recibida: $category")

        val products = if (category != null) {
            Log.d(TAG, "Cargando productos para la categoría: $category")
            databaseHelper.getProductsForCategory(category)
        } else {
            Log.d(TAG, "Cargando todos los productos")
            databaseHelper.getProducts()
        }

        // Configuración del adaptador
        adapter = ProductsAdapter(
            products = products.toMutableList(),
            isCartMode = false, // Modo general (no es carrito)
            onAddToCartClick = { product ->
                Cart.addProduct(product) // Agregar al carrito
                Toast.makeText(
                    requireContext(),
                    "Producto agregado: ${product.title}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onIncreaseQuantityClick = null, // No se usa en este fragmento
            onDecreaseQuantityClick = null, // No se usa en este fragmento
            onUpdateTotal = {} // No se usa en este fragmento
        )

        recyclerView.adapter = adapter
        Log.d(TAG, "Adapter configurado con ${products.size} productos")
        return view
    }

    private fun getProductList(): List<Product> {
        // Aquí debería ir la lógica para obtener los productos
        return databaseHelper.getProducts()
    }

    fun updateProducts(category: String?) {
        val products = if (category != null) {
            databaseHelper.getProductsForCategory(category)
        } else {
            databaseHelper.getProducts()
        }
        adapter.updateProducts(products.toMutableList())
    }
}



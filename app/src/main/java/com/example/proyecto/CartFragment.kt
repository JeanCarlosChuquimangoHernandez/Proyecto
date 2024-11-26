package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var buttonPayment: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(context)

        totalPriceTextView = view.findViewById(R.id.totalPrice)

        // Obtener productos del carrito y configurar el adaptador
        val cartProducts = Cart.getProducts()
        val adapter = ProductsAdapter(
            products = cartProducts.toMutableList(),
            isCartMode = true,
            onAddToCartClick = {}, // No se usa en el carrito
            onIncreaseQuantityClick = { product ->
                Log.d("CartFragment", "Aumentar cantidad para ${product.title}")
                Cart.addProduct(product) // Incrementa la cantidad en el carrito
                updateCartView()
            },
            onDecreaseQuantityClick = { product ->
                Log.d("CartFragment", "Disminuir cantidad para ${product.title}")
                Cart.removeProduct(product) // Disminuye la cantidad o elimina el producto
                updateCartView()
            },
            onUpdateTotal = { total ->
                totalPriceTextView.text = "Total: S/ %.2f".format(total)
            }
        )

        recyclerView.adapter = adapter

        // Configurar botón de pago
        buttonPayment = view.findViewById(R.id.buttonPayment)
        buttonPayment.setOnClickListener {
            val intent = Intent(activity, MainActivity2::class.java)
            val totalPrice = cartProducts.sumOf { it.price * it.quantity }
            intent.putExtra("TOTAL_PRICE", totalPrice)
            intent.putParcelableArrayListExtra("CART_PRODUCTS", ArrayList(cartProducts)) // Pasar productos
            startActivity(intent)
        }

        updateTotalPrice(cartProducts)

        return view
    }

    // Actualizar vista del carrito
    private fun updateCartView() {
        val cartProducts = Cart.getProducts()
        (recyclerView.adapter as ProductsAdapter).updateProducts(cartProducts)
        updateTotalPrice(cartProducts)
    }

    // Calcular y actualizar el precio total
    private fun updateTotalPrice(products: List<Product>) {
        val totalPrice = products.sumOf { it.price * it.quantity }
        totalPriceTextView.text = "S/ %.2f".format(totalPrice)
    }
}


package com.example.proyecto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PurchaseHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyHistoryText: TextView
    private val db = FirebaseFirestore.getInstance()
    private val purchases = mutableListOf<Purchase>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("PurchaseHistoryFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_purchase_history, container, false)

        // Inicializar RecyclerView y TextView
        recyclerView = view.findViewById(R.id.purchaseHistoryRecyclerView)
        emptyHistoryText = view.findViewById(R.id.emptyHistoryText)

        recyclerView.layoutManager = LinearLayoutManager(context)

        Log.d("PurchaseHistoryFragment", "RecyclerView y LayoutManager inicializados")

        // Llamar al método para obtener el historial de compras
        fetchPurchaseHistory()

        return view
    }

    private fun fetchPurchaseHistory() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("PurchaseHistoryFragment", "fetchPurchaseHistory called - User ID: $userId")

        if (userId == null) {
            Log.e("PurchaseHistoryFragment", "Error: User ID is null")
            Toast.makeText(context, "No se encontró un usuario autenticado.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                Log.d("PurchaseHistoryFragment", "Firestore query successful - ${result.size()} orders found")

                purchases.clear()
                for (document in result) {
                    try {
                        val purchase = document.toObject(Purchase::class.java)
                        Log.d("PurchaseHistoryFragment", "Order retrieved: $purchase")
                        purchases.add(purchase)
                    } catch (e: Exception) {
                        Log.e("PurchaseHistoryFragment", "Error parsing document: ${document.id}", e)
                    }
                }

                if (purchases.isEmpty()) {
                    Log.d("PurchaseHistoryFragment", "No purchases found")
                    emptyHistoryText.visibility = View.VISIBLE
                } else {
                    Log.d("PurchaseHistoryFragment", "Purchases loaded successfully")
                    emptyHistoryText.visibility = View.GONE
                    recyclerView.adapter = PurchaseHistoryAdapter(purchases)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PurchaseHistoryFragment", "Error fetching purchase history", exception)
                Toast.makeText(context, "Error al cargar el historial", Toast.LENGTH_SHORT).show()
            }
    }
}

package com.example.proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PurchaseHistoryAdapter(private val purchases: List<Purchase>) :
    RecyclerView.Adapter<PurchaseHistoryAdapter.PurchaseViewHolder>() {

    class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val totalPriceText: TextView = itemView.findViewById(R.id.totalPriceText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val productListText: TextView = itemView.findViewById(R.id.productListText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase_history, parent, false)
        return PurchaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val purchase = purchases[position]
        holder.totalPriceText.text = "Total: S/ ${purchase.totalPrice}"
        holder.dateText.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(purchase.date))
        holder.productListText.text = purchase.products.joinToString("\n") { "${it.title} x${it.quantity}" }
    }

    override fun getItemCount(): Int = purchases.size
}

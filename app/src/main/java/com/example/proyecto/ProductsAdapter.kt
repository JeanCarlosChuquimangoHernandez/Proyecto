package com.example.proyecto

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ProductsAdapter(
    private var products: MutableList<Product>,
    private val isCartMode: Boolean = false,
    private val onAddToCartClick: (Product) -> Unit,
    private val onIncreaseQuantityClick: ((Product) -> Unit)? = null,
    private val onDecreaseQuantityClick: ((Product) -> Unit)? = null,
    private val onUpdateTotal: (Double) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val addToCartButton: Button = itemView.findViewById(R.id.AddCartClick)
        val increaseButton: Button = itemView.findViewById(R.id.increaseButton)
        val decreaseButton: Button = itemView.findViewById(R.id.decreaseButton)
        val quantityText: TextView = itemView.findViewById(R.id.quantityText)
        val quantityControls: LinearLayout = itemView.findViewById(R.id.quantityControls)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productName.text = product.title
        holder.productPrice.text = "S/ %.2f".format(product.price * product.quantity)
        holder.quantityText.text = product.quantity.toString()
        val context = holder.itemView.context
        val imageName = product.image_Path?.replace(".jpg", "")
        val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        if (imageResId != 0) {
            holder.productImage.setImageResource(imageResId)
        } else {
            Log.e("ProductsAdapter", "Recurso de imagen no encontrado: ${product.image_Path}")
        }
        holder.quantityText.text = "Cantidad: ${product.quantity}"
        if (isCartMode) {
            holder.addToCartButton.visibility = View.GONE
            holder.quantityControls.visibility = View.VISIBLE
            holder.quantityText.visibility = View.VISIBLE

            holder.increaseButton.setOnClickListener {
                if (product.quantity < product.stock) {
                    product.quantity++
                    holder.quantityText.text = product.quantity.toString()
                    holder.productPrice.text = "S/ %.2f".format(product.price * product.quantity)
                    onIncreaseQuantityClick?.invoke(product)
                    onUpdateTotal(getCartTotal())
                }else{
                    Toast.makeText(
                        holder.itemView.context,
                        "No puedes comprar mas unidades. Stock disponibles ${product.stock}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            holder.decreaseButton.setOnClickListener {
                if (product.quantity > 1) {
                    product.quantity-= 1
                    holder.quantityText.text = product.quantity.toString()
                    holder.productPrice.text = "S/ %.2f".format(product.price * product.quantity)
                    onDecreaseQuantityClick?.invoke(product)
                    onUpdateTotal(getCartTotal())
                }else{
                    products.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, products.size)
                    onUpdateTotal.invoke(products.sumOf { it.price * it.quantity })

                    // Mostrar mensaje opcional
                    Toast.makeText(
                        holder.itemView.context,
                        "El producto ${product.title} fue eliminado del carrito.",
                        Toast.LENGTH_SHORT
                    ).show()}
            }
        } else {
            holder.addToCartButton.visibility = View.VISIBLE
            holder.quantityControls.visibility = View.GONE
            holder.addToCartButton.setOnClickListener {
                onAddToCartClick(product)
            }
        }
        holder.itemView.setOnClickListener {
            val productDetailFragment = ProductDetailFragment.newInstance(product)
            if (context is AppCompatActivity) {
                context.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, productDetailFragment)
                    .addToBackStack(null) // Asegura el funcionamiento del botón "Atrás"
                    .commit()
            }
        }
    }

    override fun getItemCount(): Int = products.size

    private fun getCartTotal(): Double {
        return products.sumOf { it.price * it.quantity }
    }

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}



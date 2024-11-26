package com.example.proyecto

object Cart {
    private val products = mutableMapOf<Int, CartItem>()

    data class CartItem(
        val product: Product,
        var quantity: Int
    )

    // Agregar un producto al carrito
    fun addProduct(product: Product) {

        if (products.containsKey(product.id)) {
            // Incrementar la cantidad si ya existe en el carrito
            products[product.id]?.quantity = products[product.id]?.quantity?.plus(1) ?: 1
        } else {
            // Agregar el producto con cantidad inicial 1
            products[product.id] = CartItem(product, 1)
        }
    }

    // Obtener la lista de productos con sus cantidades
    fun getProducts(): List<Product> {
        return products.values.map { it.product }
    }

    // Obtener un CartItem específico por ID del producto
    fun getCartItem(productId: Int): CartItem? {
        return products[productId]
    }

    // Remover un producto o disminuir su cantidad
    fun removeProduct(product: Product) {
        if (products.containsKey(product.id)) {
            val currentQuantity = products[product.id]?.quantity ?: 0
            if (currentQuantity > 1) {
                // Disminuir la cantidad si es mayor a 1
                products[product.id]?.quantity = currentQuantity - 1
            } else {
                // Eliminar el producto si la cantidad es 1
                products.remove(product.id)
            }
        }
    }

    // Vaciar el carrito
    fun clearCart() {
        products.clear()
    }
}

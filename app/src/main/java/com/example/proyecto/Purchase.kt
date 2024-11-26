package com.example.proyecto

data class Purchase(
    val userId: String = "",
    val date: Long = 0L,
    val totalPrice: Double = 0.0,
    val products: List<Product> = emptyList()
)

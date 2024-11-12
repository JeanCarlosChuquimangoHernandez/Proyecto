package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menucategorias) // Referencia directa al layout

        // Referencia directa a las vistas por ID
        val ropaCategory = findViewById<ImageView>(R.id.ropa_image)
        val juguetesCategory = findViewById<ImageView>(R.id.juguetes_image)
        val comidaCategory = findViewById<ImageView>(R.id.comida_image)
        val bebidasCategory = findViewById<ImageView>(R.id.bebidas_image)

        // Asignar clics a las imágenes para navegar a la segunda actividad
        ropaCategory.setOnClickListener {
            navigateToSecondActivity("Ropa")
        }

        juguetesCategory.setOnClickListener {
            navigateToSecondActivity("Juguetes")
        }

        comidaCategory.setOnClickListener {
            navigateToSecondActivity("Comida")
        }

        bebidasCategory.setOnClickListener {
            navigateToSecondActivity("Bebidas")
        }
    }

    // Función que navega a la segunda actividad, pasando el nombre de la categoría seleccionada
    private fun navigateToSecondActivity(categoryName: String) {
        val intent = Intent(this, MainActivity7::class.java)
        intent.putExtra(
            "CATEGORY_NAME",
            categoryName
        )  // Pasamos la categoría seleccionada a la siguiente actividad
        startActivity(intent)
    }
}
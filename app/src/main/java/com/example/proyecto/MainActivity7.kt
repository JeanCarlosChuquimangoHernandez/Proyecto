package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categoria2)

        val cambioActividad7 = findViewById<ImageView>(R.id.cambioactividad7)

        cambioActividad7.setOnClickListener {
            navigateToSecondActivity("kfc")
        }
    }
         private fun navigateToSecondActivity(categoryName: String) {
            val intent = Intent(this, MainActivity8::class.java)
            intent.putExtra(
                "CATEGORY_NAME",
                categoryName
            )  // Pasamos la categoría seleccionada a la siguiente actividad
            startActivity(intent)
        }
    }

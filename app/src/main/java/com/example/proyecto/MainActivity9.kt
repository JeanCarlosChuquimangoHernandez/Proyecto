package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity9 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalleventa)

        // Inicializar vistas
        val ivImagenProducto = findViewById<ImageView>(R.id.ivImagenProducto)
        val ivQr = findViewById<ImageView>(R.id.ivQr)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnOrdenar = findViewById<Button>(R.id.cambiar10)

        // Obtener las imágenes pasadas desde el Intent
        val foodImageResId = intent.getIntExtra("food_image", 0)
        val qrImageResId = intent.getIntExtra("qr_image", 0)

        // Configurar la imagen de la comida, si se pasó correctamente
        if (foodImageResId != 0) {
            ivImagenProducto.setImageResource(foodImageResId)
        } else {
            Toast.makeText(this, "Imagen de comida no disponible", Toast.LENGTH_SHORT).show()
        }

        // Configurar la imagen del código QR, si se pasó correctamente
        if (qrImageResId != 0) {
            ivQr.setImageResource(qrImageResId)
        } else {
            Toast.makeText(this, "Imagen de código QR no disponible", Toast.LENGTH_SHORT).show()
        }

        // Configuración del botón "Volver"
        btnVolver.setOnClickListener {
            finish() // Regresa a la actividad anterior
        }

        // Configuración del botón "Ordenar"
        btnOrdenar.setOnClickListener {
            Toast.makeText(this, "Orden realizada con éxito", Toast.LENGTH_SHORT).show()
        }
        val cambioActividad10 = findViewById<Button>(R.id.cambiar10)

        cambioActividad10.setOnClickListener {
            val explicitIntent = Intent(this, MainActivity10::class.java)

            startActivity(explicitIntent)
        }
    }
}

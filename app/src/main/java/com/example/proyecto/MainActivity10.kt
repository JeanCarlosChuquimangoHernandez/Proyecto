package com.example.proyecto

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity10 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registronegocio)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val spCategoria = findViewById<Spinner>(R.id.spCategoria)
        val etUbicacion = findViewById<EditText>(R.id.etUbicacion)
        val etHorario = findViewById<EditText>(R.id.etHorario)
        val ivImagen = findViewById<ImageView>(R.id.ivImagen)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // Configuración del Spinner para Categoría
        val categorias = arrayOf("Restaurante", "Tienda", "Servicio")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter

        // Validación del campo de correo electrónico
        btnRegistrar.setOnClickListener {
            val email = etEmail.text.toString()
            val descripcion = etDescripcion.text.toString()
            val categoria = spCategoria.selectedItem.toString()
            val ubicacion = etUbicacion.text.toString()
            val horario = etHorario.text.toString()

            // Validación de formato de correo electrónico
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Por favor, ingrese un correo electrónico válido"
                return@setOnClickListener
            }

            // Validación de que los campos no estén vacíos
            if (email.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty() || horario.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si pasa todas las validaciones
            Toast.makeText(this, "Negocio registrado con éxito en la categoría $categoria", Toast.LENGTH_SHORT).show()

            // Limpieza de los campos después del registro
            etEmail.text.clear()
            etDescripcion.text.clear()
            etUbicacion.text.clear()
            etHorario.text.clear()
            spCategoria.setSelection(0)
        }

        // Configuración de la imagen (puedes añadir lógica para abrir la galería o la cámara)
        ivImagen.setOnClickListener {
            Toast.makeText(this, "Función para cargar imagen no implementada", Toast.LENGTH_SHORT).show()
        }

    }
}
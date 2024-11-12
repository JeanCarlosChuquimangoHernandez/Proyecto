package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro
)
        val cambioActividad4 = findViewById<Button>(R.id.cambioActividad4)

        cambioActividad4.setOnClickListener {
            val explicitIntent = Intent(this, MainActivity5::class.java)

            startActivity(explicitIntent)
        }
    }
}
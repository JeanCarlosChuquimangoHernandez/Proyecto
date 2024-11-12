package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity8 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map
        )
        val cambioActividad8 = findViewById<Button>(R.id.cambioactividad8)

        cambioActividad8.setOnClickListener {
            val explicitIntent = Intent(this, MainActivity9::class.java)

            startActivity(explicitIntent)
        }
    }
}
package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usuario
        )
        val cambioActividad5 = findViewById<Button>(R.id.cambioactividad5)

        cambioActividad5.setOnClickListener {
            val explicitIntent = Intent(this, MainActivity6::class.java)

            startActivity(explicitIntent)
        }
    }
}




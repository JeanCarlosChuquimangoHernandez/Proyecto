package com.example.proyecto
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.correo2pasos)

        val cambioActividad2 = findViewById<Button>(R.id.cambioActividad2)

        cambioActividad2.setOnClickListener {
            val explicitIntent = Intent(this, MainActivity3::class.java)

            startActivity(explicitIntent)
        }

    }
}
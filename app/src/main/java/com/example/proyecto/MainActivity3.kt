package com.example.proyecto


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmacion2pasos)

        val etVerificationCode = findViewById<EditText>(R.id.etVerificationCode)
        val btnVerify = findViewById<Button>(R.id.cambioActividad3)
        val volveractividad1 = findViewById<TextView>(R.id.volveractividad1)

        volveractividad1.setOnClickListener {
            // Volver a MainActivity si es necesario
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnVerify.setOnClickListener {
            val verificationCode = etVerificationCode.text.toString()

            if (verificationCode.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce el código de verificación", Toast.LENGTH_SHORT).show()
            } else {
                // Verificar el código ingresado
                verifyCode(verificationCode)
            }
        }
    }

    private fun verifyCode(code: String) {
        // Aquí iría la lógica real de verificación
        // Este es solo un ejemplo. Cambia "123456" por el código correcto o verifica contra el servidor
        if (code == "123456") {  // Supón que este es el código de ejemplo
            Toast.makeText(this, "Código verificado correctamente", Toast.LENGTH_SHORT).show()
            // Navegar a la actividad principal o cualquier otra actividad después de la verificación
            val intent = Intent(this, MainActivity4::class.java) // O la actividad principal
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show()
        }
    }
}

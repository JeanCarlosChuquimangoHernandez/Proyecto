package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogleSignIn: Button

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Inicializar Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        

        // Inicializar vistas
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin = findViewById(R.id.btnLogin)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)

        btnGoogleSignIn.setOnClickListener {
            val intent = Intent(this, GoogleSignInActivity::class.java)
            startActivity(intent)
        }
        // Configurar listeners
        setListeners()
        val textViewRegister = findViewById<TextView>(R.id.Registrate)
        textViewRegister.setOnClickListener {
            // Redirigir a la pantalla de registro
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }
    }

    private fun setListeners() {

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (checkCredentials(email, password)) {
                loginUser(email, password)
            }
        }
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        return when {
            !email.contains("@") || email.length < 6 -> {
                etEmail.error = getString(R.string.error_invalid_email)
                etEmail.requestFocus()
                false
            }
            password.length < 6 -> {
                etPassword.error = getString(R.string.error_invalid_password)
                etPassword.requestFocus()
                false
            }
            else -> true
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user?.isEmailVerified == true) {
                        // Redirige a la actividad principal y finaliza MainActivity
                        val intent = Intent(this, MainActivity6::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        finishAffinity() // Finalizar MainActivity para que no quede en el historial de navegación
                    } else {
                        Toast.makeText(this, "Verifica tu correo electrónico", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserInFirestore(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d(TAG, "User exists in Firestore")
                    // Usuario encontrado, redirigir a otra actividad
                    navigateToMainActivity6()
                    finish()
                } else {
                    Log.d(TAG, "User does not exist in Firestore")
                    Toast.makeText(this, "Cuenta no encontrada en la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error checking user in Firestore", e)
            }
    }

    private fun navigateToMainActivity6() {
        val intent = Intent(this, MainActivity6::class.java)
        startActivity(intent)
        finish()
    }
}



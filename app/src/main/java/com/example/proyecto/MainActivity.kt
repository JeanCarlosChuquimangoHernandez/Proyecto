package com.example.proyecto

import android.app.VoiceInteractor
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

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
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)

        // Configurar listeners
        setListeners()
    }

    private fun setListeners() {
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (checkCredentials(email, password)) {
                registerNewUser(email, password)
            }
        }

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

    private fun registerNewUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = firebaseAuth.currentUser
                    sendEmailVerification(user)
                    saveUserToFirestore(user?.uid, email)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Verification email sent to ${user.email}")
                    Toast.makeText(this, "Correo de verificación enviado", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                }
            }
    }

    private fun saveUserToFirestore(userId: String?, email: String) {
        if (userId != null) {
            val userMap = hashMapOf("email" to email)
            firestore.collection("users").document(userId).set(userMap)
                .addOnSuccessListener {
                    Log.d(TAG, "User added to Firestore")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding user to Firestore", e)
                }
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = firebaseAuth.currentUser
                    if (user?.isEmailVerified == true) {
                        checkUserInFirestore(user.uid)
                    } else {
                        Toast.makeText(this, "Verifica tu correo electrónico", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
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
                    navigateToMainActivity4()
                } else {
                    Log.d(TAG, "User does not exist in Firestore")
                    Toast.makeText(this, "Cuenta no encontrada en la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error checking user in Firestore", e)
            }
    }

    private fun navigateToMainActivity4() {
        val intent = Intent(this, MainActivity6::class.java)
        startActivity(intent)
        finish()
    }
}



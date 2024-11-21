package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity6 : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "GoogleSignInActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menucategorias) // Referencia directa al layout

        // Referencia directa a las vistas por ID
        val ropaCategory = findViewById<ImageView>(R.id.ropa_image)
        val juguetesCategory = findViewById<ImageView>(R.id.juguetes_image)
        val comidaCategory = findViewById<ImageView>(R.id.comida_image)
        val bebidasCategory = findViewById<ImageView>(R.id.bebidas_image)

        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)



        val user = FirebaseAuth.getInstance().currentUser


        if (user != null) {
            // Establecer el nombre del usuario en el TextView
            userNameTextView.text = "Hola, ${user.displayName}"
        } else {
            userNameTextView.text = "Usuario no autenticado"
        }

        val db = FirebaseFirestore.getInstance()

        if (user != null) {
            // Obtener el documento del usuario desde Firestore
            val userRef = db.collection("users").document(user.uid)
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Obtener el nombre del usuario desde el documento
                    val userName = document.getString("name")

                    // Mostrar el nombre del usuario en el TextView
                    val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
                    userNameTextView.text = "Hola, $userName"
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error al obtener los datos del usuario", e)
            }
        }


        // Asignar clics a las imágenes para navegar a la segunda actividad
        ropaCategory.setOnClickListener {
            navigateToSecondActivity("Ropa")
        }

        juguetesCategory.setOnClickListener {
            navigateToSecondActivity("Juguetes")
        }

        comidaCategory.setOnClickListener {
            navigateToSecondActivity("Comida")
        }

        bebidasCategory.setOnClickListener {
            navigateToSecondActivity("Bebidas")
        }
        // Configurar GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    override fun onDestroy() {
        super.onDestroy()

        // Cerrar sesión de Firebase
        FirebaseAuth.getInstance().signOut()

        // Cerrar sesión de Google Sign-In
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build())

        googleSignInClient.signOut()

        Log.d(TAG, "Usuario cerrado de sesión exitosamente")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Si quieres que el botón de atrás no haga nada
        // super.onBackPressed() no se llama, así que no se vuelve atrás

        // O si quieres que el botón de atrás envíe al usuario a la pantalla de inicio
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    // Función que navega a la segunda actividad, pasando el nombre de la categoría seleccionada
    private fun navigateToSecondActivity(categoryName: String) {
        val intent = Intent(this, MainActivity7::class.java)
        intent.putExtra(
            "CATEGORY_NAME",
            categoryName
        )  // Pasamos la categoría seleccionada a la siguiente actividad
        startActivity(intent)
    }

}
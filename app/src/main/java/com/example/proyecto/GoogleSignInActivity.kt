package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.firestore.FirebaseFirestore

class GoogleSignInActivity : AppCompatActivity(){

        private lateinit var googleSignInClient: GoogleSignInClient
        private lateinit var firebaseAuth: FirebaseAuth
        private val TAG = "GoogleSignInActivity"

        // Definir el Activity Result Launcher
        private val googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account?.id)
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                    Toast.makeText(this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Cerrar sesión de Firebase si hay un usuario autenticado
            FirebaseAuth.getInstance().signOut()

            // Configuración de Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            // Inicializar Firebase Auth
            firebaseAuth = FirebaseAuth.getInstance()

            // Iniciar el proceso de inicio de sesión con Google
            signInWithGoogle()
        }

        private fun signInWithGoogle() {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent) // Usar el launcher en lugar de startActivityForResult
        }

        private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = firebaseAuth.currentUser
                        updateUI(user)
                        saveUserDataToDatabase(user, account)
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        private fun saveUserDataToDatabase(user: FirebaseUser?, account: GoogleSignInAccount) {
            if (user != null) {
                // Guardar en Firestore
                val userData = hashMapOf(
                    "uid" to user.uid,
                    "name" to account.displayName,
                    "email" to account.email,
                    "photoUrl" to account.photoUrl.toString(),
                )

                val db = FirebaseFirestore.getInstance()
                val userRef = db.collection("users").document(user.uid)

                userRef.set(userData)
                    .addOnSuccessListener {
                        Log.d(TAG, "Datos de usuario guardados correctamente en Firestore")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error al guardar los datos en Firestore", e)
                    }
            }else {
                Log.w(TAG, "El usuario no está autenticado, no se pueden guardar los datos.")}
        }

        // Cerramos sesión de Firebase y Google Sign-In
        override fun onDestroy() {
            super.onDestroy()

            // Cerrar sesión de Firebase
            FirebaseAuth.getInstance().signOut()

            // Cerrar sesión de Google Sign-In
            googleSignInClient.signOut()

            Log.d(TAG, "Sesión cerrada correctamente cuando la actividad se destruye")
        }

        private fun updateUI(user: FirebaseUser?) {
            if (user != null) {
                // Redirigir al usuario a la siguiente actividad
                val intent = Intent(this, MainActivity6::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al autenticar con Google", Toast.LENGTH_SHORT).show()
            }
        }

}
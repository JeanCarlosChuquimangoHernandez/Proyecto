package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserProfileFragment : Fragment() {
    private val TAG = "UserProfileFragment"
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var buttonPurchaseHistory: Button
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Inicialización de vistas
        userNameTextView = view.findViewById(R.id.userName)
        userEmailTextView = view.findViewById(R.id.userEmail)
        profileImageView = view.findViewById(R.id.profileImage)
        buttonPurchaseHistory = view.findViewById(R.id.buttonPurchaseHistory)
        logoutButton = view.findViewById(R.id.buttonLogout)

        // Inicializar FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Verificar el estado de autenticación del usuario
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            Log.d(TAG, "Usuario autenticado con UID: $userId")

            // Obtener datos del usuario
            val name = currentUser.displayName ?: "Nombre no disponible"
            val email = currentUser.email ?: "Correo no disponible"
            val photoUrl = currentUser.photoUrl?.toString()

            userNameTextView.text = name
            userEmailTextView.text = email

            if (!photoUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(photoUrl)
                    .into(profileImageView)
            }

            Log.d(TAG, "Datos de usuario cargados correctamente: $name, $email")
        }

        buttonPurchaseHistory.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PurchaseHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        // Botón de cerrar sesión
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            // Redirigir al login después de cerrar sesión
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }

        return view
    }
}





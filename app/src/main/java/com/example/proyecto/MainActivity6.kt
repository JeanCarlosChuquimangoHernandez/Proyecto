package com.example.proyecto

import android.content.Intent
import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity6 : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menucategorias)

        // Configura la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Inicializa FirebaseAuth y GoogleSignInClient
        firebaseAuth = FirebaseAuth.getInstance()

        // Inicializa GoogleSignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Verifica si el usuario está autenticado
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Si no hay usuario autenticado, redirige al login
            redirectToLogin()
        } else {
            Log.d("MainActivity6", "Usuario autenticado: ${currentUser.email}")
            setupBottomNavigationView()
            loadInitialFragment(savedInstanceState)
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, GoogleSignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupBottomNavigationView() {
        // Configurar el BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> loadFragment(Home_fragment())
                R.id.nav_categories -> loadFragment(CategoriesFragment())
                R.id.nav_cart -> loadFragment(CartFragment())
                R.id.nav_user -> loadFragment(UserProfileFragment())
            }
            true
        }
    }

    private fun loadInitialFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.selectedItemId = R.id.nav_home
            loadFragment(Home_fragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "GoogleSignInActivity"
    }
}

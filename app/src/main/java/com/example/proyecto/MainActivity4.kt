package com.example.proyecto

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity4 : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var btnRegister: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        // Inicializar Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inicializar vistas
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm)
        etBirthdate = findViewById(R.id.etBirthdate)
        btnRegister = findViewById(R.id.btnRegister)
        val etLocation = findViewById<TextInputEditText>(R.id.et_location)
        val tvSelectedLocation = findViewById<TextView>(R.id.tv_selected_location)
        // Configurar el selector de fecha para Fecha de Nacimiento

        etLocation.setOnClickListener {
            val intent = Intent(this, MainActivity8::class.java)
            startActivityForResult(intent, REQUEST_CODE_MAP)
        }

        supportFragmentManager.setFragmentResultListener("locationKey", this) { _, bundle ->
            val selectedLocation = bundle.getString("selectedLocation")
            tvSelectedLocation.text = selectedLocation
        }

        etBirthdate.setOnClickListener {
            showMaterialDatePicker()
        }

        // Configurar listener de registro
        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etPasswordConfirm.text.toString()
            val birthdate = etBirthdate.text.toString()

            if (checkCredentials(email, password, confirmPassword)) {
                registerNewUser(fullName, email, password, birthdate)
            }
        }
        // Configurar el listener para el texto de "¿Ya tienes cuenta?"
        val textViewLogin = findViewById<TextView>(R.id.textView14)
        textViewLogin.setOnClickListener {
            // Redirigir a la pantalla de inicio de sesión
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra RegisterActivity para evitar volver al registro
        }

    }
    companion object {
        const val REQUEST_CODE_MAP = 1

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK) {
            val selectedAddress = data?.getStringExtra("selectedAddress")
            val locationTextView = findViewById<TextView>(R.id.tv_selected_location)
            locationTextView.text = selectedAddress
        }
    }


    private fun showMaterialDatePicker() {
        // Limita la selección de fecha para que no sea futura
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.before(System.currentTimeMillis()))

        // Crear el selector de fecha
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona tu fecha de nacimiento")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        // Mostrar el selector y procesar la fecha seleccionada
        datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener { selection ->
            // Convertir la fecha seleccionada a formato legible
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.format(Date(selection))
            etBirthdate.setText(date)
        }
    }
    private fun checkCredentials(email: String, password: String, confirmPassword: String): Boolean {
        return when {
            !email.contains("@") || email.length < 6 -> {
                etEmail.error = "Correo no válido"
                etEmail.requestFocus()
                false
            }
            password.length < 6 -> {
                etPassword.error = "Contraseña muy corta"
                etPassword.requestFocus()
                false
            }
            password != confirmPassword -> {
                etPasswordConfirm.error = "Las contraseñas no coinciden"
                etPasswordConfirm.requestFocus()
                false
            }
            else -> true
        }
    }
    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Correo de verificación enviado. Revisa tu bandeja de entrada.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al enviar el correo de verificación", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun registerNewUser(fullName: String, email: String, password: String, birthdate: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    sendEmailVerification(user)
                    saveUserToFirestore(user?.uid, fullName, email, birthdate)
                    showVerificationMessage() // Mostrar mensaje de verificación
                } else {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFirestore(userId: String?, fullName: String, email: String, birthdate: String) {
        if (userId != null) {
            val userMap = hashMapOf(
                "fullName" to fullName,
                "email" to email,
                "birthdate" to birthdate
            )
            firestore.collection("users").document(userId).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registro completado. Inicia sesión.", Toast.LENGTH_SHORT).show()
                    redirectToLogin() // Redirige al login después de un registro exitoso
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun showVerificationMessage() {
        // Mostrar un cuadro de diálogo que explique que deben verificar el correo
        AlertDialog.Builder(this)
            .setTitle("Verificación de Correo Electrónico")
            .setMessage("Registro exitoso. Por favor, verifica tu correo electrónico antes de iniciar sesión.")
            .setPositiveButton("Aceptar") { _, _ ->
                // Volver a la pantalla de inicio de sesión
                redirectToLogin()
            }
            .setCancelable(false)
            .show()
    }
    private fun redirectToLogin() {
        // Redirige a MainActivity4 para el inicio de sesión
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra RegisterActivity para evitar regresar al registro
    }
}

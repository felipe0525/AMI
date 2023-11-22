package com.example.pentalikami

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.pentalikami.databinding.ActivityInicioSesionBinding
import com.google.firebase.auth.FirebaseAuth


class InicioSesion : AppCompatActivity() {
    // Creacion de la instancia de ActivityInicioBinding
    private lateinit var binding: ActivityInicioSesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializacion del binding
        binding = ActivityInicioSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()

    }


    private fun setup() {

        // Establece el OnClickListener para el botón de registro
        binding.registrarseAhoraButton.setOnClickListener {
            // Inicia la actividad Registro
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        binding.inicioSesionButton.setOnClickListener {
            if (binding.correoEditText.text.isNotEmpty() && binding.contrasenaEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.correoEditText.text.toString(),
                    binding.contrasenaEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showPrincipal(it.result?.user?.email ?: "")
                    } else {
                        showAlert()
                    }
                }
            }else {
                showIncompleteFieldsAlert()

            }


        }

    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El usuario o contraseña son incorrectos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showPrincipal(correo: String) {
        val intentPrincipal = Intent(this, MisListas::class.java).apply {
            putExtra("correo", correo)
        }
        startActivity(intentPrincipal)
        finish()
    }

    private fun showIncompleteFieldsAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Campos Incompletos")
        builder.setMessage("Por favor, completa todos los campos para iniciar sesión.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
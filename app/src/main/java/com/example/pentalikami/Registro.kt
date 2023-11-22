package com.example.pentalikami

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pentalikami.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AlertDialog


class Registro : AppCompatActivity() {
    // Creacion de la instancia de ActivityRegistroBinding
    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializacion del binding
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUp()


    }


    private fun setUp() {
        binding.registroButton.setOnClickListener {

            if (binding.correoEditText.text.isNotEmpty() && binding.contrasenaEditText.text.isNotEmpty() && binding.ConfirmarContrasenaEditText.text.isNotEmpty()) {

                if (binding.contrasenaEditText.text.trim()
                        .toString() == binding.ConfirmarContrasenaEditText.text.trim().toString()
                ) {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.correoEditText.text.toString(),
                        binding.contrasenaEditText.text.trim().toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "El usuario fue creado exitosamente", Toast.LENGTH_LONG).show()
                            showPrincipal(it.result?.user?.email ?: "")
                        } else {
                            showAlert()
                        }
                    }

                } else {
                    showPasswordsDoNotMatchAlert()

                }
            } else {
                showIncompleteFieldsAlert()
            }
        }
    }


    private fun showPasswordsDoNotMatchAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Contraseñas No Coinciden")
        builder.setMessage("Las contraseñas ingresadas no coinciden. Por favor, intenta nuevamente.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showIncompleteFieldsAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Campos Incompletos")
        builder.setMessage("Por favor, completa todos los campos para continuar.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
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
        val intentPrincipal = Intent(this, PasoUno::class.java).apply {
            putExtra("correo", correo)
        }
        startActivity(intentPrincipal)
        finish()
    }


}
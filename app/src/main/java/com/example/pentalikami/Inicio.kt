package com.example.pentalikami

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pentalikami.databinding.ActivityInicioBinding
import com.google.firebase.auth.FirebaseAuth

class Inicio : AppCompatActivity() {
    // Creacion de la instancia de ActivityInicioBinding
    private lateinit var binding: ActivityInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // Inicializacion del binding
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Verifica si el usuario ya está autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Usuario ya está autenticado, redirige a la pantalla principal
            showPrincipal(currentUser.email ?: "")
        }

        // Establecer el OnClickListener en el iniciarSesionImageView
        binding.iniciarSesionButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad InicioSesion
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }

        // Establecer el OnClickListener en el registrarseImageView
        binding.registrarseButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad InicioSesion
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

    }

    private fun showPrincipal(correo: String) {
        val intentPrincipal = Intent(this, MisListas::class.java).apply {
            putExtra("correo", correo)
        }
        startActivity(intentPrincipal)
        finish()
    }
}



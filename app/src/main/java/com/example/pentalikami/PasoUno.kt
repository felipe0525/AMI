package com.example.pentalikami

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pentalikami.databinding.ActivityPasoUnoBinding

class PasoUno : AppCompatActivity() {
    // Creacion de la instancia de ActivityPasoUnoBinding
    private lateinit var binding: ActivityPasoUnoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializacion del binding
        binding = ActivityPasoUnoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Establecer el OnClickListener en el pasoUnoAtrasButton
        binding.pasoUnoAtrasButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad PasoUno
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }


        // Establecer el OnClickListener en el pasoUnoSiguienteButton
        binding.pasoUnoSiguienteButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad PasoUno
            val intent = Intent(this, PasoDos::class.java)
            startActivity(intent)
        }



    }
}
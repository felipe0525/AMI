package com.example.pentalikami

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pentalikami.databinding.ActivityPasoDosBinding

class PasoDos : AppCompatActivity() {
    // Creacion de la instancia de ActivityPasoDosBinding
    private lateinit var binding: ActivityPasoDosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializacion del binding
        binding = ActivityPasoDosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Establecer el OnClickListener en el pasoUnoAtrasButton
        binding.pasoDosAtrasButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad PasoUno
            val intent = Intent(this, PasoUno::class.java)
            startActivity(intent)
        }


        // Establecer el OnClickListener en el pasoUnoSiguienteButton
        binding.pasoDosSiguienteButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad PasoUno
            val intent = Intent(this, PasoTres::class.java)
            startActivity(intent)
        }



    }
}
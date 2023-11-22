package com.example.pentalikami

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pentalikami.databinding.ActivityPasoTresBinding

class PasoTres : AppCompatActivity() {
    // Creacion de la instancia de ActivityPasoTresBinding
    private lateinit var binding: ActivityPasoTresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializacion del binding
        binding = ActivityPasoTresBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Establecer el OnClickListener en el pasoUnoAtrasButton
        binding.pasoTresAtrasButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad PasoUno
            val intent = Intent(this, PasoDos::class.java)
            startActivity(intent)
        }


        // Establecer el OnClickListener en el pasoUnoSiguienteButton
        binding.pasoTresSiguienteButton.setOnClickListener {
            // Crea un Intent para iniciar la actividad PasoUno
            val intent = Intent(this, NuevaLista::class.java)
            startActivity(intent)
        }
    }
}
package com.example.pentalikami

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pentalikami.databinding.ActivityMisListasBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast


class MisListas : AppCompatActivity() {

    private lateinit var binding: ActivityMisListasBinding
    private var correo: String? = null
    private lateinit var db: FirebaseFirestore
    private lateinit var listaMercadoAdapter: ListaMercadoAdapter
    private val listasDeMercado = mutableListOf<ListaDeMercado>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisListasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        correo = intent.getStringExtra("correo")
        db = FirebaseFirestore.getInstance()

        setupBottomNavigation()
        logDocumentos()

        listaMercadoAdapter = ListaMercadoAdapter(listasDeMercado) { listaSeleccionada ->
            eliminarLista(listaSeleccionada)
        }
        binding.listaMercados.adapter = listaMercadoAdapter
        binding.listaMercados.layoutManager = LinearLayoutManager(this)

        binding.bottomNavigation.selectedItemId = R.id.navigation_mis_listas

        
    }

    private fun logDocumentos() {
        correo?.let { email ->
            db.collection("usuarios").document(email).collection("listasDeMercado").get()
                .addOnSuccessListener { resultado ->
                    listasDeMercado.clear() // Limpia la lista anterior
                    for (documento in resultado) {
                        val lista = documento.toObject(ListaDeMercado::class.java)
                        listasDeMercado.add(lista)
                    }
                    listaMercadoAdapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
                }
                .addOnFailureListener { exception ->
                    Log.e("ERROR", "Error al obtener los documentos", exception)
                }
        }
    }

    private fun eliminarLista(lista: ListaDeMercado) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("¿Desea eliminar ${lista.nombre}?")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, id ->
                // Eliminar la lista si se presiona Aceptar
                confirmarEliminacion(lista)
            }
            .setNegativeButton("Cancelar") { dialog, id ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmación de Eliminación")
        alert.show()
    }

    private fun confirmarEliminacion(lista: ListaDeMercado) {
        correo?.let { email ->
            db.collection("usuarios").document(email).collection("listasDeMercado")
                .document(lista.nombre)
                .delete()
                .addOnSuccessListener {
                    listasDeMercado.remove(lista)
                    listaMercadoAdapter.notifyDataSetChanged()
                    Toast.makeText(this, " ${lista.nombre} ha sido eliminada", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("ERROR", "Error al eliminar la lista", exception)
                }
        }
    }



    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_mis_listas -> {

                    true
                }

                R.id.navigation_nueva_lista -> {
                    // Iniciar la actividad MisListas y pasar el correo como extra
                    val intentMisListas = Intent(this, NuevaLista::class.java).apply {
                        putExtra("correo", correo)
                    }
                    startActivity(intentMisListas)
                    true
                }

                R.id.navigation_salir -> {
                    // Aquí deberías agregar el código para cerrar la sesión de Firebase o el servicio que estés utilizando
                    FirebaseAuth.getInstance().signOut()

                    // Después de cerrar sesión, iniciar la actividad de inicio de sesión
                    val intentInicioSesion = Intent(this, InicioSesion::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intentInicioSesion)
                    finish() // Cierra esta actividad
                    true
                }
                else -> false
            }
        }
    }
}
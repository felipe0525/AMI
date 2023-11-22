package com.example.pentalikami

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pentalikami.databinding.ActivityNuevaListaBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.view.LayoutInflater
import android.content.pm.PackageManager
import com.google.firebase.auth.FirebaseAuth
import androidx.recyclerview.widget.ItemTouchHelper

class NuevaLista : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityNuevaListaBinding
    private var correo: String? = null
    private var nombre: String? = null
    private var contenido: String? = null
    private var tipo: String? = null
    private val productos = mutableListOf<Producto>()
    private val REQUEST_CODE = 100
    private val REQUEST_CAMERA_PERMISSION = 200
    private var documentId = "7702025125531"
    private var globalNumber: String = documentId + ".png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fechaActual = Calendar.getInstance().time
        val formateador = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val fechaFormateada = formateador.format(fechaActual)
        //binding.fechaTextView.text = fechaFormateada

        binding.fechaTextView.text = "Sabado, 24 de Julio de 2023 "

        db.collection("productos").document(documentId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    nombre = document.getString("nombre")
                    contenido = document.getString("contenido")
                    tipo = document.getString("tipo")
                    obtenerUrlImagen()
                }
            }

        correo = intent.getStringExtra("correo")
        obtenerUrlImagen()

        binding.guardarListabutton.setOnClickListener {
            mostrarDialogoNombreLista()
        }

        setupBottomNavigation()

        binding.bottomNavigation.selectedItemId = R.id.navigation_nueva_lista

        binding.camaraButton.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openScanner()
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }

        binding.eliminarListabutton.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Confirmar Eliminación")
                setMessage("¿Seguro que desea eliminar la lista?")
                setPositiveButton("Aceptar") { dialog, _ ->
                    productos.clear()
                    productoAdapter.notifyDataSetChanged()
                    Toast.makeText(this@NuevaLista, "Lista eliminada.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }

        recyclerView = findViewById(R.id.listaProductos)
        productoAdapter = ProductoAdapter(productos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productoAdapter

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                productos.removeAt(position)
                productoAdapter.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private fun obtenerUrlImagen() {
        val storageReference =
            FirebaseStorage.getInstance().reference.child("images/${globalNumber}")

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            iniciarListaProductosConImagen(imageUrl)
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun obtenerUrlImagenParaProducto(producto: Producto) {
        val storageReference =
            FirebaseStorage.getInstance().reference.child("images/${globalNumber}")

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            producto.imagenRuta = imageUrl
            agregarProductoALista(producto)
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun agregarProductoALista(nuevoProducto: Producto) {
        // Agregar el producto a la lista y notificar al adaptador
        productos.add(nuevoProducto)
        productoAdapter.notifyItemInserted(productos.size - 1)
    }

    private fun iniciarListaProductosConImagen(imageUrl: String) {
        if (nombre != null && contenido != null && tipo != null) {
            productos.addAll(
                listOf(
                )
            )

            recyclerView = findViewById(R.id.listaProductos)
            productoAdapter = ProductoAdapter(productos)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = productoAdapter
        } else {
        }
    }

    private fun mostrarDialogoNombreLista() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nombre_lista, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)

        val editTextNombreLista = dialogView.findViewById<EditText>(R.id.editTextNombreLista)
        editTextNombreLista.setText(binding.tituloListatextView.text.toString())

        dialogBuilder
            .setTitle("Guardar Lista de Mercado")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                val nombreLista = editTextNombreLista.text.toString()
                verificarNombreUnicoYGuardar(nombreLista)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun verificarNombreUnicoYGuardar(nombreLista: String) {
        correo?.let { correoUsuario ->
            db.collection("usuarios").document(correoUsuario)
                .collection("listasDeMercado")
                .document(nombreLista)
                .get()
                .addOnSuccessListener { documento ->
                    if (documento.exists()) {
                        Toast.makeText(
                            this,
                            "El nombre de la lista ya existe. Por favor, elige otro nombre.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        binding.tituloListatextView.text = nombreLista
                        guardarListaDeMercado(nombreLista)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error al verificar el nombre de la lista.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun guardarListaDeMercado(nombreLista: String) {
        val listaDeMercado = ListaDeMercado(
            nombre = nombreLista,
            fecha = binding.fechaTextView.text.toString(),
            productos = productos,
            cantidadProductos = productos.size.toString(),
            ubicacion = "Jumbo Calle 80"
        )

        correo?.let {
            val listaNombre = listaDeMercado.nombre
            db.collection("usuarios").document(it)
                .collection("listasDeMercado")
                .document(listaNombre)
                .set(listaDeMercado)
                .addOnSuccessListener {
                    Toast.makeText(this, "Lista de Mercado Guardada con éxito", Toast.LENGTH_SHORT)
                        .show()
                    // Redirigir a la actividad MisListas
                    val intentMisListas = Intent(this, MisListas::class.java)
                    intentMisListas.putExtra("correo", correo)
                    startActivity(intentMisListas)
                }
                .addOnFailureListener {
                    // Manejar el error
                    Toast.makeText(this, "Error al guardar la lista de mercado", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }


    private fun openScanner() {
        val intent = Intent(this, CaptureActivity::class.java)
        intent.putExtra(
            "SCAN_FORMATS",
            "QR_CODE,PDF_417,CODE_39,CODE_93,CODE_128,DATA_MATRIX,UPC_A,UPC_E,EAN_8,EAN_13"
        )
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openScanner()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val result = IntentIntegrator.parseActivityResult(resultCode, data)
                if (result != null) {
                    if (result.contents == null) {
                        Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
                    } else {
                        // Obtener el código de barras escaneado
                        val codigoBarras = result.contents
                        buscarProductoPorCodigoBarras(codigoBarras)
                    }
                }
            }
        }
    }

    private fun buscarProductoPorCodigoBarras(codigoBarras: String) {
        db.collection("productos").document(codigoBarras).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Actualizar documentId con el nuevo código de barras
                    documentId = codigoBarras
                    globalNumber = documentId + ".png"

                    // Crear un nuevo producto con la información obtenida
                    val nuevoProducto = document.toObject(Producto::class.java)
                    nuevoProducto?.let {
                        it.unidades = 1 // Establecer las unidades a 1
                        // Obtener la URL de la imagen para el nuevo producto
                        obtenerUrlImagenParaProducto(it)
                    }
                } else {
                    Toast.makeText(this, "Producto no encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al buscar el producto.", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_mis_listas -> {
                    // Iniciar la actividad MisListas y pasar el correo como extra
                    val intentMisListas = Intent(this, MisListas::class.java).apply {
                        putExtra("correo", correo)
                    }
                    startActivity(intentMisListas)
                    true
                }

                R.id.navigation_nueva_lista -> {
                    // Ya estás en NuevaLista, no es necesario iniciarla de nuevo.
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
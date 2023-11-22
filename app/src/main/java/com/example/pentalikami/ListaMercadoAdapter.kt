package com.example.pentalikami

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pentalikami.R

class ListaMercadoAdapter(
    private val listas: List<ListaDeMercado>,
    private val onEliminarClicked: (ListaDeMercado) -> Unit
) : RecyclerView.Adapter<ListaMercadoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val numberOfProductsTextView: TextView = view.findViewById(R.id.numberOfProductsTextView)
        val eliminarButton: ImageView = view.findViewById(R.id.EliminarMiListaButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_mis_listas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = listas[position]
        holder.titleTextView.text = lista.nombre
        holder.dateTextView.text = lista.fecha
        holder.numberOfProductsTextView.text = "${lista.cantidadProductos} Productos"
        holder.eliminarButton.setOnClickListener { onEliminarClicked(lista) }
    }

    override fun getItemCount() = listas.size
}

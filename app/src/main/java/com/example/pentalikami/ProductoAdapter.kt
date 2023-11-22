package com.example.pentalikami

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductoAdapter(private val productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_nueva_lista_productos, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int = productos.size

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productoImageView: ImageView = itemView.findViewById(R.id.productoImageView)
        private val nombreProductoTextView: TextView = itemView.findViewById(R.id.nombreProductoTextView)
        private val detalleContenidoProductotextView: TextView = itemView.findViewById(R.id.detalleContenidoProductotextView)
        private val tipoProductotextView: TextView = itemView.findViewById(R.id.tipoProductotextView)
        private val cantidadProductoTextView: TextView = itemView.findViewById(R.id.cantidadProductoTextView)
        private val decrementarButton: ImageView = itemView.findViewById(R.id.decrementarButton)
        private val incrementarButton: ImageView = itemView.findViewById(R.id.incrementarButton)

        fun bind(producto: Producto) {
            nombreProductoTextView.text = producto.nombre
            detalleContenidoProductotextView.text = producto.contenido
            tipoProductotextView.text = producto.tipo
            cantidadProductoTextView.text = producto.unidades.toString()

            Glide.with(itemView.context)
                .load(producto.imagenRuta)
                .into(productoImageView)

            decrementarButton.setOnClickListener {
                if (producto.unidades > 1) {
                    producto.unidades--
                    cantidadProductoTextView.text = producto.unidades.toString()
                }
            }

            incrementarButton.setOnClickListener {
                producto.unidades++
                cantidadProductoTextView.text = producto.unidades.toString()
            }
        }
    }
}

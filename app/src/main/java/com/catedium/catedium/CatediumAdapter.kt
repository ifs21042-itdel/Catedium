package com.catedium.catedium

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Kucing(
    val imageResId: Int,
    val name: String,
    val description: String,
    val scientific_name: String,
    val distribution: String,
    val habitat: List<String>,
    val characteristics: List<String>
)

class CatediumAdapter(private val kucingList: List<Kucing>) : RecyclerView.Adapter<CatediumAdapter.KucingViewHolder>() {

    inner class KucingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewKucing: ImageView = itemView.findViewById(R.id.imageViewKucing)
        val textViewJudul: TextView = itemView.findViewById(R.id.textViewJudul)
        val textViewDeskripsi: TextView = itemView.findViewById(R.id.textViewDeskripsi)
        val buttonSelengkapnya: Button = itemView.findViewById(R.id.buttonSelengkapnya)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KucingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kucing, parent, false)
        return KucingViewHolder(view)
    }

    override fun onBindViewHolder(holder: KucingViewHolder, position: Int) {
        val kucing = kucingList[position]
        holder.imageViewKucing.setImageResource(kucing.imageResId)
        holder.textViewJudul.text = kucing.name
        holder.textViewDeskripsi.text = kucing.description

        holder.buttonSelengkapnya.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailHewanActivity::class.java).apply {
                putExtra("name", kucing.name)
                putExtra("imageResId", kucing.imageResId)
                putExtra("description", kucing.description)
                putExtra("scientific_name", kucing.scientific_name)
                putExtra("habitat", ArrayList(kucing.habitat))
                putExtra("characteristics", ArrayList(kucing.characteristics))
                putExtra("distribution", kucing.distribution)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = kucingList.size
}

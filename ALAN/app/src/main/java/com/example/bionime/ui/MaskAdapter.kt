package com.example.bionime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bionime.R
import com.example.bionime.data.Mask

class MaskAdapter(private val onItemClick: (Mask) -> Unit) :
    RecyclerView.Adapter<MaskAdapter.MaskViewHolder>() {

    private var masks: List<Mask> = emptyList()

    fun setMasks(newMasks: List<Mask>) {
        masks = newMasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mask, parent, false)
        return MaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaskViewHolder, position: Int) {
        val mask = masks[position]
        holder.bind(mask)
        holder.itemView.setOnClickListener { onItemClick(mask) }
    }

    override fun getItemCount() = masks.size

    class MaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val maskAdultTextView: TextView = itemView.findViewById(R.id.maskAdultTextView)
        private val maskChildTextView: TextView = itemView.findViewById(R.id.maskChildTextView)
        private val townTextView: TextView = itemView.findViewById(R.id.townTextView)

        fun bind(mask: Mask) {
            nameTextView.text = mask.name
            addressTextView.text = mask.address
            maskAdultTextView.text = "成人口罩數: ${mask.maskAdult}"
            maskChildTextView.text = "兒童口罩數: ${mask.maskChild}"
            townTextView.text = mask.town
        }
    }
}
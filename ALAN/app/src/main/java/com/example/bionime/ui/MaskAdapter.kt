package com.example.bionime.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bionime.data.Mask
import com.example.bionime.databinding.ItemMaskBinding

class MaskAdapter(private val onItemClick: (Mask) -> Unit) :
    RecyclerView.Adapter<MaskAdapter.MaskViewHolder>() {

    private var masks: List<Mask> = emptyList()

    fun setMasks(newMasks: List<Mask>) {
        masks = newMasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaskViewHolder {
        val binding = ItemMaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaskViewHolder, position: Int) {
        val mask = masks[position]
        holder.bind(mask)
        holder.itemView.setOnClickListener { onItemClick(mask) }
    }

    override fun getItemCount() = masks.size

    class MaskViewHolder(private val binding: ItemMaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mask: Mask) {
            binding.apply {
                nameTextView.text = mask.name
                addressTextView.text = mask.address
                maskAdultTextView.text = "成人口罩數: ${mask.maskAdult}"
                maskChildTextView.text = "兒童口罩數: ${mask.maskChild}"
                townTextView.text = mask.town
            }
        }
    }
}

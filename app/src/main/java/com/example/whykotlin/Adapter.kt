package com.example.whykotlin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.databinding.CellCustomBinding

class Adapter(val clickListener: (Int) -> Unit) :RecyclerView.Adapter<Adapter.CellViewHolder>() {
    class CellViewHolder(binding: CellCustomBinding): RecyclerView.ViewHolder(binding.root) {
        val caseViewer: View = binding.caseCalendrier//Id du text dans cell_custom.xml pour (1,2,3 au début)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = CellCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (22-7)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        //val plate = items[position]

        holder.caseViewer.setOnClickListener {
            Log.d("click", "click on ${position}")
            //clickListener(plate)
        }
    }
}
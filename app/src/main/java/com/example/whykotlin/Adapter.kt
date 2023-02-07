package com.example.whykotlin

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.databinding.CellHourBinding

class Adapter() :RecyclerView.Adapter<Adapter.CellViewHolder>() {
    class CellViewHolder(binding: CellHourBinding): RecyclerView.ViewHolder(binding.root) {
        val hourLabel = binding.hourLabel
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = CellHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (22-7 +1)*8
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        //val plate = items[position]
        val hour = position / 8 //Chaque ligne de 8 element on revient à la ligne donc chaque ligne est une heure
        val weekDay = position % 8 //Chaque colonne
        val days = listOf("Lun","Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")
        if(hour == 0 && weekDay != 0){
            holder.hourLabel.text = days[weekDay - 1]
            //holder.hourLabled.rotation = -60F
        }
        else if( weekDay == 0 && hour != 0){
            holder.hourLabel.text = "${hour.toInt()+6}H"
        }
        else if(hour ==0 && weekDay==0){
            holder.hourLabel.text = "/" //Vide
        }
        else if(weekDay == 6 && hour >= 4 && hour <= 11){
            holder.hourLabel.text = "X"
            holder.hourLabel.setTextColor(Color.parseColor("#FFFF0000"))
        }
        else{
            holder.hourLabel.text = "O"
            holder.hourLabel.setTextColor(Color.parseColor("#FF00FF00"))
        }
    }
}
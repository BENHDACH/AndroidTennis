package com.example.whykotlin

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.databinding.ActivityClendrierBinding
import com.example.whykotlin.databinding.CellHourBinding
import java.util.Calendar
import java.util.Locale

class Adapter(val clickListener: (Int, Int) -> Unit) :RecyclerView.Adapter<Adapter.CellViewHolder>() {
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
        //--------
        var currentTNext : Array<String> = arrayOf("","","","","","","","")
        var calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        var samedi : Int = 0 //On est samedi par defaut et on verifiera le reste
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        val currentT = "$dayOfMonth $month"
        for(i in 1..7){
            calendar.add(Calendar.DATE,1)
            /* On cherche le jour correspondant au Samedi dans la semaine */
            var dayS = calendar[Calendar.DAY_OF_WEEK]
            if(dayS == Calendar.SATURDAY){
                samedi = i+1 //On saute le jour [0] on est donc a i+1 jour de Samedi

            }
            var dayOfMonthNext = calendar.get(Calendar.DAY_OF_MONTH)
            var monthNext = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
            currentTNext[i] = "$dayOfMonthNext $monthNext"

        }
       // calendar.add(Calendar.DATE, 1)
        //val dayOfMonth1 = calendar.get(Calendar.DAY_OF_MONTH)
        //val month1 = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        //val currentT1 = "$dayOfMonth1 $month1"

        //--------
        val days = listOf("${currentT}","${currentTNext[1]}", "${currentTNext[2]}", "${currentTNext[3]}", "${currentTNext[4]}", "${currentTNext[5]}", "${currentTNext[6]}")
        if(hour == 0 && weekDay != 0){
            holder.hourLabel.text = days[weekDay - 1]
            holder.hourLabel.rotation = -60F
        }
        else if( weekDay == 0 && hour != 0){
            holder.hourLabel.text = "${hour.toInt()+6}H"
        }
        else if(hour ==0 && weekDay==0){
            holder.hourLabel.text = "/" //Vide
        }
        else if(weekDay == samedi && hour >= 4 && hour <= 11){
            holder.hourLabel.text = "X"
            holder.hourLabel.setTextColor(Color.parseColor("#FFFF0000"))

        }
        else{

            holder.hourLabel.text = "O"
            holder.hourLabel.setTextColor(Color.parseColor("#FF00FF00"))
            holder.hourLabel.setOnClickListener{
                //val intent = Intent(ClendrierActivity::class.java, ReservationActivity::class.java)
                //startActivity(intent)
                clickListener(weekDay, hour)
            }

        }
    }
}
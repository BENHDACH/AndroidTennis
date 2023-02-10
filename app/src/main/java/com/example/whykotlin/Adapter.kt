package com.example.whykotlin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.databinding.CellHourBinding
import java.util.Calendar

class Adapter(val clickListener: (Int, Int) -> Unit, val flecheClick: Boolean, val terrain: String) :RecyclerView.Adapter<Adapter.CellViewHolder>() {
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
        //Log.e("CroisMoii","Les position ${position}")
       // Log.e("FLeche ?","${flecheClick}")
        val hour = position / 8 //Chaque ligne de 8 element on revient à la ligne donc chaque ligne est une heure
        val weekDay = position % 8 //Chaque colonne
        //--------
        var currentTNext : Array<String> = arrayOf("","","","","","","","")
        var calendar = Calendar.getInstance()
        if(flecheClick){
            calendar.add(Calendar.DATE,7)
        }

        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        var samedi : Int = 0 //On est samedi par defaut et on verifiera le reste
        val month = calendar.get(Calendar.MONTH )+1
       // val year = calendar.get(Calendar.YEAR) //Fonctionne pour l'id
        //Log.e("LEL","Just give it ${month}")
        val currentT = "$dayOfMonth/$month "
        for(i in 1..7){

            calendar.add(Calendar.DATE,1)
            /* On cherche le jour correspondant au Samedi dans la semaine */
            var dayS = calendar[Calendar.DAY_OF_WEEK]
            if(dayS == Calendar.SATURDAY){
                samedi = i+1 //On saute le jour [0] on est donc a i+1 jour de Samedi

            }
            var dayOfMonthNext = calendar.get(Calendar.DAY_OF_MONTH)
            var monthNext = calendar.get(Calendar.MONTH) +1
            currentTNext[i] = "$dayOfMonthNext/$monthNext"

        }



        val days = listOf("${currentT}","${currentTNext[1]}", "${currentTNext[2]}", "${currentTNext[3]}", "${currentTNext[4]}", "${currentTNext[5]}", "${currentTNext[6]}")
        //Colonne
        if(hour == 0 && weekDay != 0){
            holder.hourLabel.text = days[weekDay - 1]
            holder.hourLabel.rotation = -60F
            holder.hourLabel.setTextColor(Color.parseColor("#FF000000"))
            holder.hourLabel.setTextSize(1,14f)
        }
        //Ligne
        else if( weekDay == 0 && hour != 0){
            holder.hourLabel.text = "${hour.toInt()+6}H"
            holder.hourLabel.rotation = 0F
            holder.hourLabel.setTextColor(Color.parseColor("#FF000000"))
            holder.hourLabel.setTextSize(1,14f)
        }
        //Position = 0
        else if(hour ==0 && weekDay==0){
            holder.hourLabel.text = "/" //Vide
            holder.hourLabel.setTextColor(Color.parseColor("#FF000000"))
            holder.hourLabel.setTextSize(1,14f)
        }
        //Samedi est reserve de 10H à 18H
        else if(weekDay == samedi && hour >= 4 && hour <= 11){
            holder.hourLabel.text = "X"
            holder.hourLabel.rotation = 0F
            holder.hourLabel.setTextColor(Color.parseColor("#FFFF0000"))
            holder.hourLabel.setTextSize(1,30f)

        }
        else{
            /* TO DO
            FireBase -> Jours -|
                                Heures -|
                                          X || O (reserved or not)

            weekDay donnera le jour a chercher, hour donnera l'heure
            SI la valeur de ce jour et cette heure est O || X alors holder.hourLabel.text = "O" || "X"

                FIN */
            holder.hourLabel.text = "O"
            holder.hourLabel.rotation = 0F
            holder.hourLabel.setTextColor(Color.parseColor("#FF00FF00"))
            holder.hourLabel.setTextSize(1,30f)
            holder.hourLabel.setOnClickListener{
                //val intent = Intent(ClendrierActivity::class.java, ReservationActivity::class.java)
                //startActivity(intent)
                clickListener(weekDay, hour)
            }

        }
    }

     fun modifyText(holder : CellViewHolder,newValue: String) {
         holder.hourLabel.text = newValue
         notifyDataSetChanged()
    }
}
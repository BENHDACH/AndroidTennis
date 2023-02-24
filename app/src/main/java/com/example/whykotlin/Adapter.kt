package com.example.whykotlin

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.databinding.CellHourBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class Adapter(val clickListener: (Int, Int, Int, String, String, Boolean, String) -> Unit, val flecheClick: Boolean, val terrain: String) :RecyclerView.Adapter<Adapter.CellViewHolder>() {
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

        var bonusWeekDay = 0
        if(flecheClick){ //Semaine 2 actif ?
            bonusWeekDay = 7 //Ajouter 7 jours au jours en colonne (a envoyer vers reservation)
        }
        val hour = position / 8 //Chaque ligne de 8 element on revient à la ligne donc chaque ligne est une heure
        val weekDay = position % 8 //Chaque colonne
        //--------
        //utiliser pour afficher le nom du jour
        var currentTNext : Array<String> = arrayOf("","","","","","","","")
        //utiliser pour get la data selon un id precis (avec années)
        var idDataNext : Array<String> = arrayOf("","","","","","","","")
        var xTrue : Boolean = false
        var calendar = Calendar.getInstance()
        if(flecheClick){
            calendar.add(Calendar.DATE,7)
        }

        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        var samedi : Int = 0 //On est samedi par defaut et on verifiera le reste
        val month = calendar.get(Calendar.MONTH )+1
        val year = calendar.get(Calendar.YEAR) //Fonctionne pour l'id
        //Log.e("LEL","Just give it ${month}")
        val currentT = "$dayOfMonth/$month "
        val idData = "$dayOfMonth-$month-$year"
        for(i in 1..7){

            calendar.add(Calendar.DATE,1)
            /* On cherche le jour correspondant au Samedi dans la semaine */
            var dayS = calendar[Calendar.DAY_OF_WEEK]
            if(dayS == Calendar.SATURDAY){
                samedi = i+1 //On saute le jour [0] on est donc a i+1 jour de Samedi

            }
            var dayOfMonthNext = calendar.get(Calendar.DAY_OF_MONTH)
            var monthNext = calendar.get(Calendar.MONTH) +1
            var yearNext = calendar.get(Calendar.YEAR)
            currentTNext[i] = "$dayOfMonthNext/$monthNext"
            idDataNext[i] = "$dayOfMonthNext-$monthNext-$yearNext"


        }



        val days = listOf("${currentT}","${currentTNext[1]}", "${currentTNext[2]}", "${currentTNext[3]}", "${currentTNext[4]}", "${currentTNext[5]}", "${currentTNext[6]}")
        val daysForData = listOf("${idData}","${idDataNext[1]}","${idDataNext[2]}","${idDataNext[3]}","${idDataNext[4]}","${idDataNext[5]}","${idDataNext[6]}")

        //Colonne donc jour (Jour/Mois)
        if(hour == 0 && weekDay != 0){
            holder.hourLabel.text = days[weekDay - 1]
            holder.hourLabel.rotation = -60F
            holder.hourLabel.setTextColor(Color.parseColor("#FF000000"))
            holder.hourLabel.setTextSize(1,14f)
        }
        //Ligne donc heures
        else if( weekDay == 0 && hour != 0){
            holder.hourLabel.text = "${hour.toInt()+6}H"
            holder.hourLabel.rotation = 0F
            holder.hourLabel.setTextColor(Color.parseColor("#FF000000"))
            holder.hourLabel.setTextSize(1,14f)
        }
        //Position = 0 , est remplacer par un button dans ClendrierActivity et son xml
        else if(hour ==0 && weekDay==0){
            holder.hourLabel.text = "/" //Vide
            holder.hourLabel.setTextColor(Color.parseColor("#FF000000"))
            holder.hourLabel.setTextSize(1,14f)
        }
        else{
            if(terrain=="dispo"){
                setDispo("${daysForData[weekDay-1]}/${hour.toInt()+6}H/",holder)
            }
            else{
                setData(month,"${daysForData[weekDay-1]}/${hour.toInt()+6}H/", holder, weekDay, bonusWeekDay, hour, daysForData, terrain)
            }
        }
    }

     private fun setData(month : Int,path: String, holder: CellViewHolder, weekDay: Int, bonusWeekDay: Int, hour: Int, daysForData: List<String>, terrain: String){
         var xTrue = false
         var pathLastValue = path
         var resName = ""
         if(terrain =="T1"){
             pathLastValue = "planningT1/"+pathLastValue
         }
         else{
             pathLastValue = "planningT2/"+pathLastValue
         }
         val reference = Data.database.getReference("${pathLastValue}")

         reference.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 //Log.e("TAGAG","${dataSnapshot.getValue(String::class.java)}")
                 holder.hourLabel.text = dataSnapshot.child("reservStatut").getValue(String::class.java)
                 resName = dataSnapshot.child("identifiant").getValue(String::class.java).toString()
                 if(holder.hourLabel.text == "X"){
                      xTrue = true
                     if(resName==Data.theUserName){
                         holder.hourLabel.text = "V"
                         holder.hourLabel.setTextColor(Color.parseColor("#FFFF770E"))
                     }
                     else{
                         holder.hourLabel.setTextColor(Color.parseColor("#FFFF0000"))
                     }

                 }
                 else if(holder.hourLabel.text == "O"){
                     xTrue = false
                     holder.hourLabel.setTextColor(Color.parseColor("#FF00FF00"))
                 }
                 holder.hourLabel.rotation = 0F
                 holder.hourLabel.setTextSize(1,30f)

                 if(terrain=="T1"){
                     holder.hourLabel.setOnClickListener{
                         clickListener(month,weekDay+bonusWeekDay, hour,daysForData[weekDay-1],"planningT1",xTrue,resName)
                     }
                 }
                 else if(terrain=="T2"){
                     holder.hourLabel.setOnClickListener{
                         clickListener(month,weekDay+bonusWeekDay, hour,daysForData[weekDay-1],"planningT2",xTrue,resName)
                     }
                 }
             }
             override fun onCancelled(error: DatabaseError) {
                 // Handle errors
             }
         })

     }


    private fun setDispo(path: String, holder: CellViewHolder){

        var list: MutableList<String>
        val reference = Data.database.getReference("dispo/${path}")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                holder.hourLabel.text = dataSnapshot.child("reservStatut").getValue(String::class.java)
                list = dataSnapshot.child("identifiants").getValue() as MutableList<String>
                var found = "NOT"
                if (list != null) {
                    //On check toute la liste
                    for (i in 0 until list.size) {
                        //Si le nom de l'utilisaeur apparais on garde sa position
                        if (list[i] == Data.theUserName) {
                            found = i.toString()
                        }
                    }
                }
                //On set
                if(found!="NOT"){
                    holder.hourLabel.setTextColor(Color.parseColor("#FFFFFF00"))
                } else{
                    holder.hourLabel.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

                holder.hourLabel.rotation = 0F
                holder.hourLabel.setTextSize(1,30f)
                holder.hourLabel.setOnClickListener{
                    Log.e("La list est:","$list")
                    if(found!="NOT"){
                        //Dispo déjà présente il faut donc annuler la dispo
                        list?.removeAt(found.toInt()) //d'où l'interêt de ne pas avoir un boolean ici
                        Data.database.reference.child("dispo/${path}/identifiants").setValue(list)
                        holder.hourLabel.setTextColor(Color.parseColor("#FFA9A9A9"))

                    }
                    else{
                        //Pas de dispo set ici donc on l'ajoute et passe au jaune
                        list?.add(Data.theUserName)
                        Data.database.reference.child("dispo/${path}/identifiants").setValue(list)
                        holder.hourLabel.setTextColor(Color.parseColor("#FFFFFF00"))



                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

}
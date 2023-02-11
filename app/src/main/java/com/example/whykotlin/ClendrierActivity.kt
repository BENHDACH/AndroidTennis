package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import com.example.whykotlin.databinding.ActivityClendrierBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.snapshots
import java.time.DayOfWeek
import java.util.*

@IgnoreExtraProperties
data class Calendrier(val dayPlan: String? = null){

}
data class DateJour(val horaire: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
data class HeureJour(val reservStatut: String? = null, val identifiant : String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}



class ClendrierActivity : AppCompatActivity() {


    private lateinit var binding: ActivityClendrierBinding

    companion object {
        val extraKeyr = "extraKeyr"
    }

    private var fleche: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClendrierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generatorDataCal("planningT1")
        generatorDataCal("planningT2")

        binding.recyclerView.layoutManager = GridLayoutManager(this, 8)
        var terrain = intent.getStringExtra("Terrain")

        supportActionBar?.title = terrain

        binding.recyclerView.adapter = Adapter({ weekday, hour, dayForData, terrain, xTrue, resName ->
            if(xTrue == true){
                //si c'est un admin il peut y accedez
                if(Data.theUserRank=="0" || Data.theUserName==resName){
                    val intent = Intent(this, ReservationActivity::class.java)
                    intent.putExtra("Heure", "${hour + 6}")
                    intent.putExtra("Jour", "${weekday}")
                    intent.putExtra("CheminJour","${dayForData}")
                    intent.putExtra("terrain","${terrain}")
                    //On dit a reservation que c'est une annulation qui pourra être faite
                    intent.putExtra("Annul",true)
                    startActivity(intent)
                }
            }
            //Si ce n'est pas reservé on continue
            else{
                val intent = Intent(this, ReservationActivity::class.java)
                intent.putExtra("Heure", "${hour + 6}")
                intent.putExtra("Jour", "${weekday}")
                intent.putExtra("CheminJour","${dayForData}")
                intent.putExtra("terrain","${terrain}")
                intent.putExtra("Annul",false)
                startActivity(intent)
            }
        }, fleche, terrain.toString())

        binding.buttonFleche.setOnClickListener {
            fleche = !fleche
            //writeNewRes("10-2-2023", "10h", "X")
            if (fleche) {
                binding.buttonFleche.text = "<"
            } else {
                binding.buttonFleche.text = ">"
            }

            /* Permet de reset après l'action des flèches le planning */
            binding.recyclerView.adapter = Adapter({ weekday, hour, dayForData, terrain, xTrue,resName ->
                if(xTrue == true){
                    //si c'est un admin il peut y accedez
                    if(Data.theUserRank=="0" || Data.theUserName==resName){
                        val intent = Intent(this, ReservationActivity::class.java)
                        intent.putExtra("Heure", "${hour + 6}")
                        intent.putExtra("Jour", "${weekday}")
                        intent.putExtra("CheminJour","${dayForData}")
                        intent.putExtra("terrain","${terrain}")
                        //On dit a reservation que c'est une annulation qui pourra être faite
                        intent.putExtra("Annul",true)
                        startActivity(intent)
                    }
                }
                //Si ce n'est pas reservé on continue
                else{
                    val intent = Intent(this, ReservationActivity::class.java)
                    intent.putExtra("Heure", "${hour + 6}")
                    intent.putExtra("Jour", "${weekday}")
                    intent.putExtra("CheminJour","${dayForData}")
                    intent.putExtra("terrain","${terrain}")
                    intent.putExtra("Annul",false)
                    startActivity(intent)
                }

            }, fleche, terrain.toString())

        }


    }

    private fun generatorDataCal(planning: String) {
        var cal = Calendar.getInstance()
        var samedi = false
        for (j in 0..13) {
            //si nous ne somme pas Aujourd'hui on avance de 1 jour
            if(j!=0){
                cal.add(Calendar.DATE, 1)
            }

            val idDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
            val idMonth = cal.get(Calendar.MONTH) + 1
            val idYear = cal.get(Calendar.YEAR)
            val idDayPlan = "${idDayOfMonth}-${idMonth}-${idYear}"

            if (cal[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
                samedi = true
            }
            else{
                samedi = false
            }

            checkChild(idDayPlan, samedi, idDayPlan, planning)

        }

    }

    fun writeNewRes(dayPlan: String, heure: String, bouton: String, planning: String) {
        Calendrier(dayPlan)
        DateJour(heure)
        val name = "AUTO"
        val heureJour = HeureJour(bouton,name)


        Data.database.reference.child("${planning}").child(dayPlan).child(heure).setValue(heureJour)
    }

    fun checkChild(idChild: String, samedi: Boolean, idDayPlan: String, planning: String) {
        val reference = Data.database.getReference("${planning}/${idChild}/")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Child exists on ne fait rien (la valeur à peut être était modifié à la reservation)

                } else {
                    // Child n'existe pas donc on peut le créer
                    setDataCalendar(samedi, idDayPlan, planning)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setDataCalendar(samedi: Boolean, idDayPlan: String, planning: String) {

        //Verif if Samedi sinon all "0" except if idDayPlan already set
        //Log.e("Samedi","${cal[Calendar.DAY_OF_WEEK]}")
        for (i in 7..21) {

            //Si c'est un samedi de 10h à 17h reservé sachant qu'on réserve 1h minimum 10-11h à 17-18h
            if (samedi) {

                if (i in 10..17) {
                    writeNewRes("${idDayPlan}", "${i.toString()}H", "X", planning)
                }
                //Le reste est libre
                else {
                    writeNewRes("${idDayPlan}", "${i.toString()}H", "O", planning)
                }
            } else {
                writeNewRes("${idDayPlan}", "${i.toString()}H", "O", planning)
            }
        }

    }
}
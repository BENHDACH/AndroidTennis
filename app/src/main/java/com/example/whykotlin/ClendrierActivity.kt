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
data class HeureJour(val reservStatut: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}



class ClendrierActivity : AppCompatActivity() {


    private lateinit var binding: ActivityClendrierBinding

    companion object{
        val extraKeyr = "extraKeyr"
    }

    private var fleche: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClendrierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generatorDataCal()

        binding.recyclerView.layoutManager = GridLayoutManager(this, 8)
        var terrain = intent.getStringExtra("Terrain")
        var nameUserCurrent = intent.getStringExtra("nameUserT1")
        supportActionBar?.title = terrain

        binding.recyclerView.adapter = Adapter ({ weekday, hour ->
            val intent = Intent(this, ReservationActivity::class.java)
            intent.putExtra("Heure","${hour+6}")
            intent.putExtra("Jour","${weekday}")
            intent.putExtra("Nom","${nameUserCurrent.toString()}")
            startActivity(intent)
        },fleche,terrain.toString())

        binding.buttonFleche.setOnClickListener{
            fleche = !fleche
            writeNewRes("08-02-2023", "10h", "X")
            if(fleche){
                binding.buttonFleche.text = "<"
            }
            else{
                binding.buttonFleche.text = ">"
            }


            binding.recyclerView.adapter = Adapter ({ weekday, hour ->
                val intent = Intent(this, ReservationActivity::class.java)
                intent.putExtra("Heure","${hour+6}")
                intent.putExtra("Jour","${weekday}")
                intent.putExtra("Nom","${nameUserCurrent}")
                startActivity(intent)
            },fleche,terrain.toString())

        }



    }

    private fun generatorDataCal(){
        var cal = Calendar.getInstance()
        var samedi = false
        for(j in 1..13){
            cal.add(Calendar.DATE,1)
            val idDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
            val idMonth = cal.get(Calendar.MONTH )+1
            val idYear = cal.get(Calendar.YEAR)
            val idDayPlan = "${idDayOfMonth}-${idMonth}-${idYear}"

            if(cal[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY){
                samedi = true
            }
            else{
                samedi = false
            }

            checkChild(idDayPlan, samedi, idDayPlan)

        }

    }

    fun writeNewRes(dayPlan: String, heure: String, bouton: String) {
        Calendrier(dayPlan)
        DateJour(heure)
        val heureJour = HeureJour(bouton)

        Data.database.reference.child("planningT1").child(dayPlan).child(heure).setValue(heureJour)
    }
    fun checkChild(idChild: String, samedi : Boolean , idDayPlan: String){
        val reference = Data.database.getReference("planningT1/${idChild}/")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Child exists on ne fait rien (la valeur à peut être était modifié à la reservation)

                } else {
                    // Child n'existe pas donc on peut le créer
                    setDataCalendar(samedi,idDayPlan)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setDataCalendar(samedi: Boolean,idDayPlan: String){

        //Verif if Samedi sinon all "0" except if idDayPlan already set
        //Log.e("Samedi","${cal[Calendar.DAY_OF_WEEK]}")
        for(i in 7..21){

            //Si c'est un samedi de 10h à 17h reservé sachant qu'on réserve 1h minimum 10-11h à 17-18h
            if(samedi){

                if(i in 10..17){
                    writeNewRes("${idDayPlan}","${i.toString()}H","X")
                }
                //Le reste est libre
                else if(i>=18){
                    writeNewRes("${idDayPlan}","${i.toString()}H","O")
                }
                //i<=9 car besoin d'un 0 devant les unqiue unité (purement visuelle)
                else{
                    writeNewRes("${idDayPlan}","0${i.toString()}H","O")
                }
            }
            else{
                if(i<=9){
                    //Car un chiffre seul apparais en bas dans la firebase 10h -> 11h ... 21h -> 7h -> 8h -> 9h donc deux digit permet d'avoir une database ordonnée (oui c'est inutile et juste pour le visuelle)
                    writeNewRes("${idDayPlan}","0${i.toString()}H","O")
                }
                else{
                    writeNewRes("${idDayPlan}","${i.toString()}H","O")
                }
            }
        }
    }

}
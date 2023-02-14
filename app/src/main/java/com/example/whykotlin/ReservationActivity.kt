package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import layout.AdapterReserv
import java.util.Calendar
import java.util.Locale

class ReservationActivity : AppCompatActivity() {


    companion object {
        val extraKeys = "extrakeys"
    }

    lateinit var binding: ActivityReservationAvtivityBinding
    var hourReserved = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_avtivity)

        binding = ActivityReservationAvtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.currentRes.visibility = View.GONE
        binding.textRes.visibility = View.GONE

        supportActionBar?.title = "Réservation"
        ShowDate()
        var annulation = intent.getBooleanExtra("Annul",false)
        var clickPlusAdh = true

        binding.listAd.layoutManager = LinearLayoutManager(this)
        binding.plusadh.setOnClickListener {
            binding.listAd.adapter = AdapterReserv("${intent.getStringExtra("CheminJour")}/${intent.getStringExtra("Heure")}H")
            if(clickPlusAdh){
                binding.plusadh.text = "Cacher les disponnibilités"
                binding.listAd.visibility = View.VISIBLE
            }
            else{

                binding.plusadh.text = "Voir les disponnibilités"
                binding.listAd.visibility = View.GONE
            }
            clickPlusAdh = !clickPlusAdh

        }
        if(annulation){
            binding.enregistre.text = "Annulation"
            binding.currentRes.visibility = View.VISIBLE
            binding.textRes.visibility = View.VISIBLE
            binding.currentRes.text = intent.getStringExtra("resUser")
            checkResAdh("O")
        }
        else{
            binding.enregistre.text = "Enregistre"
            checkResAdh("X")
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "MenuActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "MenuActivity onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "MenuActivity onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle", "MenuActivity onStart")
    }
    private fun ShowDate() {

        binding.textTime.text = intent.getStringExtra("Heure")+"h"
        val date = intent.getStringExtra("Jour")
       // val intdate : Int? = date.toInt()
        val calendrier = Calendar.getInstance()

        if(date=="1") {
            val day = calendrier.get(Calendar.DAY_OF_MONTH)
            val month = calendrier.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault() )
            binding.textDate.text = "${day} ${month}"
        }
        else{
            calendrier.add(Calendar.DATE, (date?.toInt() ?:1)-1)
            val day = calendrier.get(Calendar.DAY_OF_MONTH)
            val month = calendrier.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault() )
            binding.textDate.text = "${day} ${month}"
        }
    }

    private fun ShowAdh() {
        var recupName = Data.theUserName
        //val user = Data.database.getReference("userName")
        Log.e("test", "${recupName}")
        binding.idReserv.text = "${recupName}"
        
    }

    private fun buttonListener(value:String) {
        val dayPlan = intent.getStringExtra("CheminJour")
        val heure = intent.getStringExtra("Heure")+"H"
        val planning = intent.getStringExtra("terrain")

        binding.enregistre.setOnClickListener {
            if(value=="X"){
                val reservConfirm = HeureJour("$value","${Data.theUserName}")
                Toast.makeText(this, "réservé", Toast.LENGTH_LONG).show()
                Data.database.reference.child(planning.toString()).child(dayPlan.toString()).child(heure).setValue(reservConfirm)
            }
            else if(value=="O"){
                val reservConfirm = HeureJour("$value","AUTO")
                Toast.makeText(this, "Annulation confirmé", Toast.LENGTH_LONG).show()
                Data.database.reference.child(planning.toString()).child(dayPlan.toString()).child(heure).setValue(reservConfirm)
            }

            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }

        if(value=="Toast"){
            Toast.makeText(this,"Nombre de réservation maximal atteinte pour ce jour",Toast.LENGTH_LONG).show()
        }
        binding.backhome.setOnClickListener {

            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkResAdh(value: String){

        //Creer à la base pour break la boucle mais
        // le dataSnapshot prend plus de temps que la boucle
        // (la boucle finit mais la verification est toujorus en cours) il servira a empecher trop d'affichage...
        var continuationBoucle: Boolean = true
        //De 7h à 21h aujourd'hui on verifie les reservations
        for(i in 7..21){
        //Si il y'a eu 2 reservation (on ne peux pas réserver encore)
            val reference = Data.database.getReference("${intent.getStringExtra("terrain").toString()}/${intent.getStringExtra("CheminJour").toString()}/${i.toString()+"H"}/identifiant")
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if(Data.theUserName == dataSnapshot.getValue(String::class.java).toString()){
                            hourReserved++
                        }
                        if(hourReserved>=2 && value=="X"){
                            //On affiche le message une seule fois
                            if(continuationBoucle){
                                buttonListener("Toast")
                                continuationBoucle = false
                            }

                        }
                        //Si il y'a moins de 2 reservation (après avoir verifié de 7h à 21h) ou que l'action et une annulation on continue
                        else if((hourReserved<2 && i==21) || value=="O"){
                            buttonListener(value)
                        }
                    }
                    else {

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        }

    }

}
package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding
import layout.AdapterReserv
import java.util.Calendar
import java.util.Locale

class ReservationActivity : AppCompatActivity() {


    companion object {
        val extraKeys = "extrakeys"
    }

    lateinit var binding: ActivityReservationAvtivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_avtivity)

        binding = ActivityReservationAvtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Réservation"
        ShowDate()
        ShowAdh()
        var participant = ""
        var clickCount = 0
        var annulation = intent.getBooleanExtra("Annul",false)

        binding.listAd.layoutManager = LinearLayoutManager(this)
        binding.plusadh.setOnClickListener {
            participant = binding.ajouterAdh.text.toString()
            clickCount++
            binding.listAd.adapter = AdapterReserv(participant,clickCount)
        }
        if(annulation){
            binding.enregistre.text = "Annulation"
            buttonListener("O")
        }
        else{
            binding.enregistre.text = "Enregistre"
            buttonListener("X")
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
        val reservConfirm = HeureJour("$value","${Data.theUserName}")
        binding.enregistre.setOnClickListener {
            Toast.makeText(this, "réservé", Toast.LENGTH_LONG).show()

            Data.database.reference.child(planning.toString()).child(dayPlan.toString()).child(heure).setValue(reservConfirm)

            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }
        binding.backhome.setOnClickListener {

            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }




    }

}
package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding
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
        buttonListener()
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
       // binding.idReserv.text = "ThierryH"
      //  binding.nomDate.text = getString(R.string.nomDate)

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
        var recupName = intent.getStringExtra("nameUser")
        //val user = Data.database.getReference("userName")
        Log.e("test", "${recupName}")
        binding.idReserv.text = "${recupName}"
        
    }

    private fun buttonListener() {
        binding.enregistre.setOnClickListener {
            Toast.makeText(this, "réservé", Toast.LENGTH_LONG).show()

            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }
        binding.backhome.setOnClickListener {

            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }

    }

}
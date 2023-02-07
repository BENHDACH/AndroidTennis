package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        ShowAdh()
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
    private fun ShowAdh() {
       // binding.idReserv.text = "ThierryH"
      //  binding.nomDate.text = getString(R.string.nomDate)

        binding.textTime.text = intent.getStringExtra("Heure")+"h"
        val date = intent.getStringExtra("Jour")
       // val intdate : Int? = date.toInt()

        if(date=="1") {
            val calendrier = Calendar.getInstance()
            val day = calendrier.get(Calendar.DAY_OF_MONTH)
            val month = calendrier.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault() )
            binding.textDate.text = "${day} ${month}"
        }
        else{
            val calendrier = Calendar.getInstance()
            calendrier.add(Calendar.DATE, (date?.toInt() ?:1)-1)
            val day = calendrier.get(Calendar.DAY_OF_MONTH)
            val month = calendrier.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault() )
            binding.textDate.text = "${day} ${month}"
        }






    }
}
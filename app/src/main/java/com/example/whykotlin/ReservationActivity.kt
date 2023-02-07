package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding

class ReservationActivity : AppCompatActivity() {

    companion object {
        val extraKeys = "extrakeys"
    }

    lateinit var binding: ActivityReservationAvtivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_avtivity)


        supportActionBar?.title = "Réservation"
       // ShowAdh()
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
     //   binding.nomTime.text = getString(R.string.nomTime)



    }
}
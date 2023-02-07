package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ReservationActivity : AppCompatActivity() {

    companion object {
        val extraKeys = "extrakeys"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_avtivity)
    }
}
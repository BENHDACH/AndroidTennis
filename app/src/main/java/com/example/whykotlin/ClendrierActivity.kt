package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.whykotlin.databinding.ActivityClendrierBinding

class ClendrierActivity : AppCompatActivity() {


    private lateinit var binding: ActivityClendrierBinding

    companion object{
        val extraKeyr = "extraKeyr"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClendrierBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = GridLayoutManager(this, 8)

        binding.recyclerView.adapter = Adapter() { weekday, hour ->
            val intent = Intent(this, ReservationActivity::class.java)
            intent.putExtra("Heure","${hour+6}")
            intent.putExtra("Jour","${weekday}")
            startActivity(intent)
        }

    }
}
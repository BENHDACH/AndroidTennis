package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import com.example.whykotlin.databinding.ActivityClendrierBinding

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
}
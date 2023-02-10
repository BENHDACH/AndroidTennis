package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import com.example.whykotlin.databinding.ActivityClendrierBinding
import com.google.firebase.database.IgnoreExtraProperties
import java.time.DayOfWeek

@IgnoreExtraProperties
data class DateJour(val dayPlan: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
data class HeureJour(val bouton: String? = null) {
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
            writeNewRes("06-02-2023", "7h", "O")
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

    fun writeNewRes(dayPlan: String, heure: String, bouton: String) {
        val dateJour = DateJour(heure)
        val heureJour = HeureJour(bouton)

        Data.database.reference.child("planningT1").child(dayPlan).setValue(dateJour)
        Data.database.reference.child("planningT1").child(bouton).setValue(heureJour)
    }
}
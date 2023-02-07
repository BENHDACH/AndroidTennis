package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityAccueilBinding
import com.example.whykotlin.databinding.ActivityAjoutadhBinding

class AjoutadhActivity : AppCompatActivity() {

    lateinit var binding: ActivityAjoutadhBinding

    companion object{
        val extraKeya = "extraKeya"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjoutadhBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_ajoutadh)

        //buttonsListener()
    }

    /*private fun buttonsListener() {

            binding.newname.setOnClickListener {
                Toast.makeText(this, "ajouter le nom d'un nel adhérant", Toast.LENGTH_LONG).show()
                binding.sname.visibility = View.GONE
            }
    }*/

}

//sname
package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityAccueilBinding


enum class Category {RESERVATIONT1, RESERVATIONT2, RESERVATIONS, TCHAT, AJOUTADH}
class AccueilActivity : AppCompatActivity() {

    lateinit var binding: ActivityAccueilBinding
    lateinit var currentCategory: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccueilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getSerializableExtra(ChatActivity.extraKey) as? Category
        currentCategory = category ?: Category.RESERVATIONT1

        buttonsListener()
    }

    private fun buttonsListener() {

        //quand admin -> rank 0
        var recupRk = Data.theUserRank

        // On vas dans la res du terrain1
        binding.resT1.setOnClickListener {
            Log.d("button", "Click sur button reservation1")
            Toast.makeText(this, "res1", Toast.LENGTH_LONG).show()
            var recupName = Data.theUserName
            val intent = Intent(this, ClendrierActivity::class.java)
            intent.putExtra("Terrain","T1")

            startActivity(intent)

        }

        // On vas dans la res terrain2
        binding.resT2.setOnClickListener {
            Toast.makeText(this, "res2", Toast.LENGTH_LONG).show()
            var recupName = Data.theUserName
            val intent = Intent(this, ClendrierActivity::class.java)
            intent.putExtra("Terrain","T2")
            startActivity(intent)

        }

        //On vas dans les disponnibilités
        binding.dejares.setOnClickListener {
            Toast.makeText(this, "Quand êtes vous libre pour jouer ?", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ClendrierActivity::class.java)
            intent.putExtra("Terrain","dispo")
            startActivity(intent)
        }

        //On vas dans le tchat
        binding.chat.setOnClickListener {
            Toast.makeText(this, "Bienvenue sur le Chat", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(ChatActivity.extraKey, Category.TCHAT)
            startActivity(intent)
        }



        Log.e("ra", "${recupRk}")
        //On va dans l'ajout d'adhérant SI ON EST Rang 0 (donc admin)
        if (recupRk == "0") {
            binding.ajoutadh.setOnClickListener {
                val intent = Intent(this, AjoutadhActivity::class.java)
                intent.putExtra(AjoutadhActivity.extraKeya, Category.AJOUTADH)
                startActivity(intent)
            }
        }
        else {
            binding.ajoutadh.visibility = View.GONE
        }
    }

}
package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        //supportActionBar?.title = categoryName()

        buttonsListener()
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "AccueilActivity on Create")
    }

    private fun categoryName(): String{
        return when (currentCategory){
            Category.RESERVATIONT1 -> getString(R.string.resT1)
            Category.RESERVATIONT2 -> getString(R.string.resT2)
            Category.RESERVATIONS -> getString(R.string.res)
            Category.TCHAT -> getString(R.string.chat)
            Category.AJOUTADH -> getString(R.string.ajoutadh)
        }

    }
    override fun onDestroy() {
        Log.d( "onDestroy", "AccueilActivity destroy")
        super.onDestroy()
    }

    private fun buttonsListener(){

        // On vas dans la resa du terrain1
        binding.resT1.setOnClickListener {
            Log.d( "button", "Click sur button reservation1")
            Toast.makeText(this, "res1", Toast.LENGTH_LONG ).show()

            val intent = Intent(this, ClendrierActivity::class.java)
            intent.putExtra(ClendrierActivity.extraKeyr, Category.RESERVATIONT1)
            startActivity(intent)

        }
        // On vas dans la resa terrain2
        binding.resT2.setOnClickListener {
            Toast.makeText(this, "res2", Toast.LENGTH_LONG ).show()
            val intent = Intent(this, ClendrierActivity::class.java)
            intent.putExtra(ClendrierActivity.extraKeyr, Category.RESERVATIONT2)
            startActivity(intent)

        }

        ////On vas dans nos réservations
        binding.dejares.setOnClickListener {
            Toast.makeText(this, "resS", Toast.LENGTH_LONG ).show()
            /*val intent = Intent(this, XXXXXXXXXActivity::class.java)
            intent.putExtra(XXXXXXActivity.extraKey, Category.RESERVATIONS)
            startActivity(intent)*/

        }

        //On vas dans le tchat
        binding.chat.setOnClickListener {
            Toast.makeText(this, "chat", Toast.LENGTH_LONG ).show()

            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(ChatActivity.extraKey, Category.TCHAT)
            startActivity(intent)
        }

        //if flag admin on ecoute
        binding.ajoutadh.setOnClickListener {
            Toast.makeText(this, "ajouter un adhérant", Toast.LENGTH_LONG ).show()
            /*val intent = Intent(this, XXXXXXXXXActivity::class.java)
            intent.putExtra(XXXXXXActivity.extraKey, Category.AJOUTADH)
            startActivity(intent)*/
        //else on cache le bouton
            binding.ajoutadh.visibility
        }
    }

}
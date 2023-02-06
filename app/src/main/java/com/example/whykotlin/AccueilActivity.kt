package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityAccueilBinding


enum class Category {RESERVATIONT1, RESERVATIONT2, RESERVATIONS, TCHAT}
class AccueilActivity : AppCompatActivity() {

    companion object{
        val extraKey = "extraKey"
    }
    lateinit var binding: ActivityAccueilBinding
    lateinit var currentCategory: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val category = intent.getSerializableExtra(extraKey) as? Category

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
        }

    }

    override fun onDestroy() {
        Log.d( "onDestroy", "AccueilActivity destroy")
        super.onDestroy()
    }

    private fun buttonsListener(){

        binding.resT1.setOnClickListener {
            Log.d( "button", "Click sur button reservation1")
            Toast.makeText(this, "res1", Toast.LENGTH_LONG ).show()
            showCategory(Category.RESERVATIONT1)
        }
        binding.resT2.setOnClickListener {
            Toast.makeText(this, "res2", Toast.LENGTH_LONG ).show()
            showCategory(Category.RESERVATIONT2)
        }
        binding.dejares.setOnClickListener {
            Toast.makeText(this, "resS", Toast.LENGTH_LONG ).show()
            showCategory(Category.RESERVATIONS)
        }

        binding.chat.setOnClickListener {
            //Log.d( "button", "Click sur button finish")
            Toast.makeText(this, "chat", Toast.LENGTH_LONG ).show()
            //val intent = Intent( this, MenuActivity::class.java)
            //startActivity(intent)
            showCategory(Category.TCHAT)
        }
    }
    private fun showCategory(category: Category){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(ChatActivity.extraKey, category)
        startActivity(intent)
    }


}
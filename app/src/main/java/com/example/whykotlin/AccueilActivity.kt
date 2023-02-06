package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

enum class Category {RESERVATIONT1, RESERVATIONT2, RESERVATIONS, TCHAT}
class AccueilActivity : AppCompatActivity() {

    companion object{
        val extraKey = "extraKey"
    }

    lateinit var currentCategory: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val category = intent.getSerializableExtra(extraKey) as? Category

        currentCategory = category ?: Category.RESERVATIONT1

        //supportActionBar?.title = categoryName()
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


}
package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ClendrierActivity : AppCompatActivity() {

    companion object{
        val extraKeyr = "extraKeyr"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clendrier)
    }
}
package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whykotlin.databinding.ActivityClendrierBinding
import com.example.whykotlin.databinding.ActivityMainBinding

class ClendrierActivity : AppCompatActivity() {

<<<<<<< HEAD
    private lateinit var binding: ActivityClendrierBinding
=======
    companion object{
        val extraKeyr = "extraKeyr"
    }
>>>>>>> 6a94a9077759f3e39d8d60f98acc1f2e831cf18c
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clendrier)

        binding = ActivityClendrierBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whykotlin.databinding.ActivityClendrierBinding
import com.example.whykotlin.databinding.ActivityMainBinding

class ClendrierActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClendrierBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clendrier)

        binding = ActivityClendrierBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
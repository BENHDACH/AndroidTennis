package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whykotlin.databinding.ActivityClendrierBinding
import com.example.whykotlin.databinding.ActivityMainBinding

class ClendrierActivity : AppCompatActivity() {


    private lateinit var binding: ActivityClendrierBinding

    companion object{
        val extraKeyr = "extraKeyr"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClendrierBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = GridLayoutManager(this, 8)
        binding.recyclerView.adapter = Adapter()

    }
}
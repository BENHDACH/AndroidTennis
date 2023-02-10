package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityChatBinding
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding

class ChatActivity : AppCompatActivity() {

    companion object{
        val extraKey = "extraKey"
    }
    lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "CHAT"
        buttonListener()
    }

    private fun buttonListener() {

        binding.backacceuil.setOnClickListener {
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }




    }
}
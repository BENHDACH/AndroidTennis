package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whykotlin.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val username: String? = null, val email: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Data.database.setValue("hello, world!")
        writeNewUser("M","MBS","J.hotmail")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonsListener()
    }

    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        Data.database.child("users").child(userId).setValue(user)
    }

    private fun buttonsListener() {
        binding.connexion.setOnClickListener{
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }
    }




}
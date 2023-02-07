package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityMainBinding
import com.google.firebase.database.*

@IgnoreExtraProperties
data class User(val userPsw: String? = null, val userRk: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        writeNewUser("Aliciane","alili","hello")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getUser()
        buttonsListener()
    }

    fun getUser() {

        Data.database.getReference("users")
            .orderByChild("userPsw")
            .equalTo(binding.caseName.text.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("dataBase", snapshot.toString())
                    if(snapshot.exists()) {
                        Log.d("d", "PEOEOEO")
                        val user = snapshot.children.first().getValue(User::class.java)
                        if(user?.userRk == binding.casePassword.text.toString()) {
                            Log.d("dataBase","connected")
                            // Connected
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("dataBase", error.toString())
                }

            })
    }
    fun writeNewUser(userId: String, userPsw: String, userRk: String) {
        val user = User(userPsw, userRk)

        Data.database.reference.child("users").child(userId).setValue(user)
    }

    private fun buttonsListener() {
        binding.connexion.setOnClickListener{
            getUser()
            val intent = Intent(this, AccueilActivity::class.java)
            Toast.makeText(this, "Bienvenue !", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
    }




}
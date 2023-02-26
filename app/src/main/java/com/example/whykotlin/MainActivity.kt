package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityMainBinding
import com.google.firebase.database.*
import kotlin.reflect.typeOf

@IgnoreExtraProperties
data class User(val userName: String? = null, val userPsw: String? = null, val userRk: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonsListener()
    }

    fun getUser(){

        Data.database.getReference("users")
            .orderByChild("userName")
            .equalTo(binding.caseName.text.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("dataBase", snapshot.toString())
                    if(snapshot.exists()) {
                        Log.d("d", "PEOEOEO")
                        val user = snapshot.children.first().getValue(User::class.java)
                        if(user?.userPsw == binding.casePassword.text.toString()) {
                            Log.d("dataBase","connected")
                             // Connected
                            Log.e("bla", "${user?.userName} ${user?.userPsw}")
                            accessHome(user?.userName,user?.userRk );
                        }
                        else {
                            falseCo();
                        }


                    }
                    if(!snapshot.exists()) {
                            falseCo();
                    }

                }


                override fun onCancelled(error: DatabaseError) {
                    Log.e("dataBase", error.toString())
                }


            })

    }

    private fun buttonsListener() {
        binding.connexion.setOnClickListener{
            getUser()
        }
    }
//
    private fun accessHome(userName: String?, userRk: String?){
        val intent = Intent(this, AccueilActivity::class.java)
        Toast.makeText(this, "Bienvenue !", Toast.LENGTH_LONG).show()
    //On init les valeur pour les récuperez n'importe où dans les activités ensuite
        Data.theUserName = userName.toString()
        Data.theUserRank = userRk.toString()
        Log.e("tai", "${userName.toString()}")

        startActivity(intent)
    }

    private fun falseCo(){
        Toast.makeText(this, "Veuillez entrer un identifiant et un mot de passe valides", Toast.LENGTH_LONG).show()
    }




}
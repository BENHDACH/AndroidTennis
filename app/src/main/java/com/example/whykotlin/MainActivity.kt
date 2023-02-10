package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityMainBinding
import com.google.firebase.database.*

@IgnoreExtraProperties
data class User(val userName: String? = null, val userPsw: String? = null, val userRk: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}





class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    //lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        writeNewUser("user2","admin","ad", "0")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getUser()
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
                            accessHome(user?.userName,user?.userPsw );
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
    fun writeNewUser(userId: String, userName: String, userPsw: String, userRk: String) {
        val user = User(userName, userPsw, userRk)

        Data.database.reference.child("users").child(userId).setValue(user)
    }


    private fun buttonsListener() {
        binding.connexion.setOnClickListener{
            getUser()
        }
    }

    private fun accessHome(userName: String?, userRk: String?){
        val intent = Intent(this, AccueilActivity::class.java)
        Toast.makeText(this, "Bienvenue !", Toast.LENGTH_LONG).show()
        intent.putExtra("rangUser", "${userRk}")
        intent.putExtra("nameUser", "${userName}")
        startActivity(intent)
    }

    private fun falseCo(){
        Toast.makeText(this, "Veuillez entrer un identifiant et un mot de passe valides", Toast.LENGTH_LONG).show()
    }




}
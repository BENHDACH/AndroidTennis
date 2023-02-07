package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.whykotlin.databinding.ActivityAccueilBinding
import com.example.whykotlin.databinding.ActivityAjoutadhBinding

class AjoutadhActivity : AppCompatActivity() {

    lateinit var binding: ActivityAjoutadhBinding

    companion object{
        val extraKeya = "extraKeya"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjoutadhBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_ajoutadh)

        buttonsListener()

        //modifier pour avoir les données rentrer au clavier

        writeNewUser("M","MBS","J.hotmail")
    }


    fun writeNewUser(userId: String, userPsw: String, userRk: String) {
        val user = User(userPsw, userRk)

        Data.database.child("users").child(userId).setValue(user)
    }


    private fun buttonsListener() {

            binding.newname.setOnClickListener {
                Log.d("textnewname", "Click sur newname")

            }

        /*if ( == null){
            //ne pas enregistrer et afficher un message
            Toast.makeText(this, "Données non valide", Toast.LENGTH_LONG).show()
        }*/

        //if enregistrement bon
        binding.enr.setOnClickListener{
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }
        //else adhérant déjà existant
        //else données rentrer invalide
    }

}

//sname
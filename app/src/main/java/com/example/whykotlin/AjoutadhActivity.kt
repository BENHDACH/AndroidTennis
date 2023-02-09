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
import com.google.android.gms.common.internal.Objects.ToStringHelper

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

        supportActionBar?.title = "Ajouter un adhérent"
    }


    fun writeNewUser(userId: String, userPsw: String, userRk: String) {
        val user = User(userPsw, userRk)

        Data.database.reference.child("users").child(userId).setValue(user)
    }


    private fun buttonsListener() {

        /*binding.newname2.setOnClickListener {
            Log.d("textnewname", "Click sur newname")
        }*/
        binding.supradh.visibility = View.GONE //on n'affiche pas le bouton supprimer

        binding.retourhome.setOnClickListener {
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }
        //if enregistrement bon
        binding.enr.setOnClickListener{
            //else données rentrer invalide

            //si nom invalide
            if (binding.newname2.text.toString() == ""){
                Log.d("textnewname", "VIDE")
                Toast.makeText(this, "nom de l'adhérant vide", Toast.LENGTH_LONG).show()
            }
            //si mot de passe vide
            if (binding.newpass2.text.toString() == ""){
                Log.d("textnewname", "PASSWORD VIDE")
                Toast.makeText(this, "mot de passe adhérant vide", Toast.LENGTH_LONG).show()
            }

            //si un nom et un mot de passe son entree
            //else adhérant déjà existant => effacer le bouton ajouter et propose un bouton suprimer
             /*if (nom utilisateur existe déja && mot de passe deja existant){
                Toast.makeText(this, "adhérant déjà existant vous pouvez le supprimer", Toast.LENGTH_LONG).show()

                binding.ajoutadh.visibility = View.GONE //efface le bouton enregistrer
                binding.supradh.visibility = View.VISIBLE //on affiche le bouton supprimer

                 binding.supradh.setOnClickListener { //on rend le bouton supprimer clicable
                     val intent = Intent(this, AccueilActivity::class.java)
                     startActivity(intent)
                 }


            }*/

            if ((binding.newname2.text.toString()).isNotEmpty()){ //il faut un nombre pair de caractère pour que ca marche
                Log.d("textnewname", "PLEIN")
                if ((binding.newpass2.text.toString()).isNotEmpty()){
                    val intent = Intent(this, AccueilActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

}

//sname
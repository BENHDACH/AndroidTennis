package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.whykotlin.databinding.ActivityAccueilBinding
import com.example.whykotlin.databinding.ActivityAjoutadhBinding
import com.google.android.gms.common.internal.Objects.ToStringHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import java.util.*

@IgnoreExtraProperties

class AjoutadhActivity : AppCompatActivity() {

    lateinit var binding: ActivityAjoutadhBinding

    companion object {
        val extraKeya = "extraKeya"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjoutadhBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var defautMois = 1
        binding.textMois.text = "$defautMois mois avant"

        binding.moinMois.setOnClickListener{
            if(defautMois>1){
                defautMois--
                binding.textMois.text = "$defautMois mois avant"
            }
        }

        binding.plusMois.setOnClickListener{
            defautMois++
            binding.textMois.text = "$defautMois mois avant"
        }

        binding.effaceMois.setOnClickListener{
            greenWash(defautMois)
        }

        buttonsListener()
        supportActionBar?.title = "Ajouter un adhérent"

    }

    fun greenWash(nbrMois : Int) {
        var cal = Calendar.getInstance()
        var dayId = 0
        var monthId = 0
        var yearId = 0
        var planId = ""
        for(i in 0..(nbrMois*31)){
            cal.add(Calendar.DATE, -1)
            dayId = cal.get(Calendar.DAY_OF_MONTH)
            monthId = cal.get(Calendar.MONTH) + 1
            yearId = cal.get(Calendar.YEAR)
            planId = "${dayId}-${monthId}-${yearId}"

            //On efface dans planningT1
            Data.database.reference.child("planningT1").child("${planId}").removeValue()
                .addOnSuccessListener {
                }.addOnFailureListener {
                    Toast.makeText(this, "ERROR on greenWash(nbrMois) =>T1", Toast.LENGTH_LONG).show()
                }

            //Pareil pour T2
            Data.database.reference.child("planningT2").child("${planId}").removeValue()
                .addOnSuccessListener {
                }.addOnFailureListener {
                    Toast.makeText(this, "ERROR on greenWash(nbrMois) =>T2", Toast.LENGTH_LONG).show()
                }

            //Pareil pour les disponnibilités
            Data.database.reference.child("dispo").child("${planId}").removeValue()
                .addOnSuccessListener {
                }.addOnFailureListener {
                    Toast.makeText(this, "ERROR on greenWash(nbrMois) =>Dispo", Toast.LENGTH_LONG).show()
                }
        }

    }

    fun writeNewAdh(adhId: String, userName: String, adhPsw: String, adhRk: String = "1") {
        val adhr = User(userName, adhPsw, adhRk)
        Data.database.reference.child("users").child(adhId).setValue(adhr)
    }


    fun getUser() {

        Data.database.getReference("users")
            .orderByChild("userName")
            .equalTo(binding.newname2.text.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.children.first().getValue(User::class.java)
                        binding.enr.visibility = View.GONE //efface le bouton enregistrer

                        //Si l'utilisateur entre son propre nom, il peut modifié son mot de passe
                        if(binding.newname2.text.toString()==Data.theUserName){

                            binding.modifadh.visibility = View.VISIBLE
                        }
                        //si c'est un autre nom il peut le supprimer
                        else{
                            binding.supradh.visibility = View.VISIBLE //on affiche le bouton supprimer
                        }

                        binding.supradh.setOnClickListener { //on rend le bouton supprimer clicable
                            suppClick(binding.newname2.text.toString());
                        }
                        binding.modifadh.setOnClickListener{
                            modifyClick("${Data.theUserName}",binding.newpass2.text.toString())
                        }
                    }
                    //Si il n'existe ou que le nom n'est pas "AUTO" (nom attribué à toute les réservations automatique), alors on le créer
                    else if (!snapshot.exists() && binding.newname2.text.toString()!="AUTO") {
                        writeNewAdh(
                            "${binding.newname2.text.toString()}-id",
                            "${binding.newname2.text.toString()}",
                            "${binding.newpass2.text.toString()}",
                            "1"
                        )
                        noExist();
                    }
                    else{
                        Toast.makeText(this@AjoutadhActivity,"Le nom AUTO est réservé par le système.",Toast.LENGTH_LONG).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("dataBase", error.toString())
                }

            })

    }


    private fun noExist() {
        Toast.makeText(this, "Nouvel adhérent enregistré", Toast.LENGTH_LONG).show()
        val intent = Intent(this, AccueilActivity::class.java)
        startActivity(intent)
    }

    private fun suppClick(userID: String) {

        //On supprime l'utilisateur se trouvant dans users---> SonNOM-id
        Data.database.reference.child("users").child("${userID}-id").removeValue().addOnSuccessListener {
            Toast.makeText(this,"${userID}-id is no more...",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this,"ERROR on suppClick(userID)",Toast.LENGTH_LONG).show()
        }
    }

    private fun modifyClick(userID: String, newPsw: String){
        Data.database.reference.child("users").child("${userID}-id").child("userPsw").setValue(newPsw)
    }

    private fun buttonsListener() {
        //on n'affiche pas le bouton supprimer et modifier immediatement
        binding.supradh.visibility = View.GONE
        binding.modifadh.visibility = View.GONE

        binding.retourhome.setOnClickListener {
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }

        binding.enr.setOnClickListener {
            getUser()
        }
    }
}


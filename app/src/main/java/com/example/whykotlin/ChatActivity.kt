package com.example.whykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whykotlin.databinding.ActivityChatBinding
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import java.util.*

data class Tchat(val idTchat: String? = null){

}
data class Conversation(val datation: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
data class DateConv(val msg: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
class ChatActivity : AppCompatActivity() {

    companion object {
        val extraKey = "extraKey"
    }

    lateinit var binding: ActivityChatBinding

    //Le lanceur est celui qui a lancer la conversation (si false) ,
    // sinon le lancer n'est pas le createur de la conversation
    var switchID = false

    // Afficher le tchat avec showAll (si true donc que l'utilisateur entré est existe)
    var showAll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //on set ces variables pour les switch si besoins (car on ne peut pas switch Data.theUserName sinon ça le switch partout)
        var user1 = Data.theUserName
        lateinit var user2 : String


        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "CHAT"


        binding.recyclerTchat.visibility = View.GONE
        binding.buttonSend.visibility = View.GONE
        binding.caseSend.visibility = View.GONE

        binding.recyclerTchat.layoutManager = LinearLayoutManager(this)

        binding.buttonLink.setOnClickListener {
            //On verifie que l'username existe et qu'une conversation ne soit pas déjà créer avec lui
            checkUsername()

            if(showAll){
                //selon la répartition de l'id on inverse user1 et user2 pour ne pas créer 2 conversation
                if(switchID){
                    user1 = binding.caseLink.text.toString()
                    user2 = Data.theUserName

                }
                else{
                    user2 = binding.caseLink.text.toString()
                }

                binding.recyclerTchat.visibility = View.VISIBLE
                binding.buttonSend.visibility = View.VISIBLE
                binding.caseSend.visibility = View.VISIBLE

                //On envoie un message avec le bouton SEND (le parametre click = false -> mode 'ecriture')
                binding.buttonSend.setOnClickListener {
                    binding.recyclerTchat.adapter = AdapterTchat("${user1}", "${user2}", "${binding.caseSend.text.toString()}", true)
                    binding.caseSend.text = null
                }

                //On re affiche le tchat avec le message (click = true -> mode affichage)
                binding.recyclerTchat.adapter = AdapterTchat("${user1}", "${user2}", "${binding.caseSend.text.toString()}", false)

            }
            else{
                binding.recyclerTchat.visibility = View.GONE
                binding.buttonSend.visibility = View.GONE
                binding.caseSend.visibility = View.GONE
            }
        }



        buttonListener()
    }

    private fun buttonListener() {

        binding.backacceuil.setOnClickListener {
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }

    }
    fun writeNewRes(userSend: String, user2: String, message: String) {

        Data.database.reference.child("Tchat").child("${userSend}-${user2}").child("msg").setValue(message)
    }

    fun checkChild(uCurrent: String, utilisateur: String) {
        var r1  = false
        var r2 = false
        var verif = false
        val reference = Data.database.getReference("Tchat/${uCurrent}-${utilisateur}/msg")
        val reference2 = Data.database.getReference("Tchat/${utilisateur}-${uCurrent}/msg")


        //Verif user1-user2 faire de même pour user2-user1 et ensuite on peut creer
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("datasnap","$dataSnapshot")
                if (dataSnapshot.exists()) {
                    //L'id existe
                    r1 = true
                    toastMaker("link")

                }
                else {
                    //L'id écrit de la façon 1 -> UserCurrent-UserContact n'existe pas (a voir l'inverse)
                    r1 = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        reference2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                verif = true
                Log.e("datasnap2","$dataSnapshot")
                if (dataSnapshot.exists()) {
                    //L'id inverse existe on l'utilise grâce à switchID (dans onCreate)

                    r2 = true
                    switchID = true
                    toastMaker("link")

                }
                else {
                    //L'id écrit de la façon 2 -> UserContact-UserCurrent n'existe pas
                    r2 = false
                    //Si r1 est aussi faux alors on créer une nouvelle conversation.
                    createNewConv(r1)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        //Les deux n'existe pas on créer un nouvelle identifiant de type 1 (UserCurrent-UserContact)




    }

    private fun createNewConv(ref:Boolean){
        if(!ref){
            toastMaker("create")
            writeNewRes("${Data.theUserName}", "${binding.caseLink.text.toString()}", "CHAT GO !")
        }
    }

    private fun checkUsername(){
        Data.database.getReference("users")
        .orderByChild("userName")
        .equalTo(binding.caseLink.text.toString())
        .addListenerForSingleValueEvent(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.e("exist1","Salut!")
                    showAll = true
                    checkChild(Data.theUserName,binding.caseLink.text.toString())
                } else {
                    showAll = false
                    toastMaker("user")
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun toastMaker(what : String){
        if(what=="user"){
            Toast.makeText(this,"L'utilisateur n'existe pas", Toast.LENGTH_LONG).show()
        }
        else if(what=="create"){
            Toast.makeText(this,"Création d'une nouvelle conversation", Toast.LENGTH_LONG).show()
        }
        else if(what=="link"){
            Toast.makeText(this,"Connexion établie !", Toast.LENGTH_LONG).show()
        }
    }


}
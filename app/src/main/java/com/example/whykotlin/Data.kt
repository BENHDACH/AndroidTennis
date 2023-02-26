package com.example.whykotlin

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Data {
    companion object{
        val database = Firebase.database("https://whykotlin-default-rtdb.europe-west1.firebasedatabase.app/")
        val flecheQ = false;//Utilité oublié <:(
        lateinit var theUserName : String;
        lateinit var theUserRank : String;
    }
}
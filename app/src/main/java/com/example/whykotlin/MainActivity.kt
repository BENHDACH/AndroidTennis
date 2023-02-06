package com.example.whykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val username: String? = null, val email: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Data.database.setValue("hello, world!")
        writeNewUser("M","MBS","J.hotmail")
    }

    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        Data.database.child("users").child(userId).setValue(user)
    }



}
package com.example.whykotlin

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.databinding.CellHourBinding
import com.example.whykotlin.databinding.CellTchatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import layout.AdapterReserv

class AdapterTchat( val user1Name: String,
                   val user2Name: String, val msg: String, val click :Boolean):RecyclerView.Adapter<AdapterTchat.CellViewHolder>() {

    class CellViewHolder(binding: CellTchatBinding) : RecyclerView.ViewHolder(binding.root) {
        val textU1 = binding.textUser1
    }

     var notSend = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = CellTchatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterTchat.CellViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return(1)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        setData("${user1Name}-${user2Name}/msg",holder)
        if(click){
            notSend = true
            addData("${user1Name}-${user2Name}/msg",holder)
        }

    }


    private fun setData(path: String, holder: AdapterTchat.CellViewHolder){

        val reference = Data.database.getReference("Tchat/${path}")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //On recup les message precedents
                holder.textU1.text = dataSnapshot.getValue(String::class.java)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun addData(path: String, holder: AdapterTchat.CellViewHolder){

        val reference = Data.database.getReference("Tchat/${path}")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //On recup les message precedents
                if(notSend){
                    Data.database.getReference("Tchat/${path}").setValue("${holder.textU1.text} \n\n ${Data.theUserName}: ${msg}")
                    notSend = !notSend
                }
                holder.textU1.text = dataSnapshot.getValue(String::class.java)

            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }
}
package layout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.Adapter
import com.example.whykotlin.Data
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding
import com.example.whykotlin.databinding.CellReservBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class AdapterReserv(val path: String):RecyclerView.Adapter<AdapterReserv.CellViewHolder>() {

    class CellViewHolder(binding:CellReservBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameA = binding.nomAdh
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterReserv.CellViewHolder {
        val binding = CellReservBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterReserv.CellViewHolder(binding)
    }


    override fun getItemCount(): Int {
        //Le nombre de text à afficherà définir...
        return (1)
    }
    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {

        val reference = Data.database.getReference("dispo/${path}")

        //On regarde les identifiants dispo à ce moment.
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var list = dataSnapshot.child("identifiants").getValue() as MutableList<String>
                var textDisplay = ""
                //On les ecrit en sautant une ligne à chaque fois.
                for(i in 1 until list.size){
                    textDisplay += "\n"+list[i]
                }
                holder.nameA.text = textDisplay
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })

    }
}
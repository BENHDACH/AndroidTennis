package layout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whykotlin.Adapter
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding
import com.example.whykotlin.databinding.CellReservBinding


class AdapterReserv(val nom: String, val compte: Int):RecyclerView.Adapter<AdapterReserv.CellViewHolder>() {

    class CellViewHolder(binding:CellReservBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameA = binding.nomAdh
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterReserv.CellViewHolder {
        val binding = CellReservBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterReserv.CellViewHolder(binding)
    }


    override fun getItemCount(): Int {
        //Le nombre de text à afficher à définir...
        return (4)
    }
    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {

        if(compte-1==position){
            holder.nameA.text = nom
        }



    }





}
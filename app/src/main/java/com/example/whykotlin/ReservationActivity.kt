package com.example.whykotlin

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.whykotlin.databinding.ActivityReservationAvtivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import layout.AdapterReserv
import java.util.Calendar
import java.util.Locale

class ReservationActivity : AppCompatActivity() {


    companion object {
        val extraKeys = "extrakeys"
    }

    lateinit var binding: ActivityReservationAvtivityBinding
    var hourReserved = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_avtivity)

        binding = ActivityReservationAvtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.currentRes.visibility = View.GONE
        binding.textRes.visibility = View.GONE

        binding.idReserv.text = Data.theUserName
        binding.hourNumb.setText("0")
        binding.minuteNumb.setText("0")


        supportActionBar?.title = "Réservation"
        ShowDate()
        var annulation = intent.getBooleanExtra("Annul",false)
        var clickPlusAdh = true

        //On creer le channel de notif
        createNotificationChannel()

        binding.listAd.layoutManager = LinearLayoutManager(this)
        binding.plusadh.setOnClickListener {
            binding.listAd.adapter = AdapterReserv("${intent.getStringExtra("CheminJour")}/${intent.getStringExtra("Heure")}H")
            if(clickPlusAdh){
                binding.plusadh.text = "Cacher les disponnibilités"
                binding.listAd.visibility = View.VISIBLE
            }
            else{

                binding.plusadh.text = "Voir les disponnibilités"
                binding.listAd.visibility = View.GONE
            }
            clickPlusAdh = !clickPlusAdh

        }
        if(annulation){
            binding.enregistre.text = "Annulation"
            binding.currentRes.visibility = View.VISIBLE
            binding.textRes.visibility = View.VISIBLE
            binding.currentRes.text = intent.getStringExtra("resUser")
            checkResAdh("O")
        }
        else{
            binding.enregistre.text = "Enregistre"
            checkResAdh("X")
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val descriptionText = "My Notification Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("my_channel_01", name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun createNotTime(): Boolean{
        val notificationIntent = Intent(this, MyNotif::class.java)
        notificationIntent.putExtra("message", "Vous avez réservé pour le ${intent.getStringExtra("CheminJour")} à ${intent.getStringExtra("Heure")}H")
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        //If same day or 1day enregistré => Now à l'heure choisie
        //Else notif arrive 1 jour avant la date à l'heure choisie par timepicker
        val calNow = Calendar.getInstance()
        var calendar = Calendar.getInstance()

        val date = intent.getStringExtra("Jour").toString().toInt()
        // val intdate : Int? = date.toInt()
        val calendrier = Calendar.getInstance()
        var booleanChecker = false

        var textVerifHour = binding.hourNumb.text.toString()
        var textVerifMinute = binding.minuteNumb.text.toString()
        var intHour = textVerifHour.toIntOrNull()
        var intMinute = textVerifMinute.toIntOrNull()

        //On verifie que l'horaire de notification est correct (un Int entre 0-23 H et 0-59 minutes)
        if(intHour != null && intMinute != null && intHour>=0 && intHour<=23 && intMinute>=0 && intMinute<=59){
            //Si l'enregistrement ce fait le jour même ou un jour avant on met la notif au jour actuelle
            booleanChecker = true
            if(date<=2){
                Log.e("AllInfo","Date:${date},TimePickH:${binding.hourNumb.text},TimePickM:${binding.minuteNumb.text}")
                calendar = Calendar.getInstance().apply {
                    //set(Calendar.YEAR, 2023)
                    //set(Calendar.MONTH, 2 - 1)
                    //set(Calendar.DAY_OF_MONTH, 24)
                    set(Calendar.HOUR_OF_DAY, binding.hourNumb.text.toString().toInt())
                    set(Calendar.MINUTE, binding.minuteNumb.text.toString().toInt())
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }
            //Sinon on met la notif 1 jour avant le jour de reservation
            else{
                calendrier.add(Calendar.DATE, (date?.toInt() ?:1)-2)
                val yearRes = calendrier.get(Calendar.YEAR)
                val day = calendrier.get(Calendar.DAY_OF_MONTH)
                val month = calendrier.get(Calendar.MONTH)
                Log.e("CheckRES","${day},${month},${yearRes}")

                calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, yearRes)
                    set(Calendar.MONTH,month)
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.HOUR_OF_DAY, binding.hourNumb.text.toString().toInt())
                    set(Calendar.MINUTE, binding.minuteNumb.text.toString().toInt())
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }

            Log.e("TimeMili","${calendar.timeInMillis}")

            Log.e("Et le curren","${calNow.timeInMillis}")

            //Si la notif est set avant l'heure actuelle alors on l'envoie maitenant
            if((calendar.timeInMillis-calNow.timeInMillis)<0){
                calendar = calNow
            }
            // On set l'alarme avec l'action , le temps et l'intent.
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

        }
        else{
            Toast.makeText(this, "Veuillez entrez des entiers H et minute ", Toast.LENGTH_LONG).show()
        }

        return(booleanChecker)
    }

    @SuppressLint("SetTextI18n")
    private fun ShowDate() {

        binding.textTime.text = intent.getStringExtra("Heure")+"h"
        val date = intent.getStringExtra("Jour")
       // val intdate : Int? = date.toInt()
        val calendrier = Calendar.getInstance()

        if(date=="1") {
            val day = calendrier.get(Calendar.DAY_OF_MONTH)
            val month = calendrier.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault() )
            binding.textDate.text = "${day} ${month}"
        }
        else{
            calendrier.add(Calendar.DATE, (date?.toInt() ?:1)-1)
            val day = calendrier.get(Calendar.DAY_OF_MONTH)
            val month = calendrier.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault() )
            binding.textDate.text = "${day} ${month}"
        }
    }

    private fun buttonListener(value:String) {
        val dayPlan = intent.getStringExtra("CheminJour")
        val heure = intent.getStringExtra("Heure")+"H"
        val planning = intent.getStringExtra("terrain")
        var checkHourMinute : Boolean = false

        binding.enregistre.setOnClickListener {
            //Si on veut réserver
            if(value=="X"){
                checkHourMinute = createNotTime()
                if(checkHourMinute){
                    val reservConfirm = HeureJour("$value","${Data.theUserName}")
                    Toast.makeText(this, "réservé", Toast.LENGTH_LONG).show()
                    Data.database.reference.child(planning.toString()).child(dayPlan.toString()).child(heure).setValue(reservConfirm)
                }


            }
            //Si on veut annuler
            else if(value=="O"){
                //Par defaut une annulation n'a pas besoin de notif donc peut importe ce qui est entrez en horaire
                checkHourMinute = true
                val reservConfirm = HeureJour("$value","AUTO")
                Toast.makeText(this, "Annulation confirmé", Toast.LENGTH_LONG).show()
                Data.database.reference.child(planning.toString()).child(dayPlan.toString()).child(heure).setValue(reservConfirm)
            }

            if(checkHourMinute){
                val intent = Intent(this, AccueilActivity::class.java)
                startActivity(intent)
            }

        }
        if(value=="Toast"){
            Toast.makeText(this,"Nombre de réservation maximal atteinte pour ce jour",Toast.LENGTH_LONG).show()
        }
        binding.backhome.setOnClickListener {

            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkResAdh(value: String){

        //Creer à la base pour break la boucle mais
        // le dataSnapshot prend plus de temps que la boucle
        // (la boucle finit mais la verification est toujorus en cours) il servira a empecher trop d'affichage...
        var continuationBoucle: Boolean = true
        //De 7h à 21h aujourd'hui on verifie les reservations
        for(i in 7..21){
        //Si il y'a eu 2 reservation (on ne peux pas réserver encore)
            val reference = Data.database.getReference("${intent.getStringExtra("terrain").toString()}/${intent.getStringExtra("CheminJour").toString()}/${i.toString()+"H"}/identifiant")
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if(Data.theUserName == dataSnapshot.getValue(String::class.java).toString()){
                            hourReserved++
                        }
                        //Si il y'a eu 2 reservation et qu'on veut encore réservé et que l'on est pas un admin, on ne peut pas.
                        if(hourReserved>=2 && value=="X" && Data.theUserRank != "0"){
                            //On affiche le message une seule fois
                            if(continuationBoucle){
                                buttonListener("Toast")
                                continuationBoucle = false
                            }

                        }
                        //Si il y'a moins de 2 reservation (après avoir verifié de 7h à 21h) ou que l'action et une annulation on continue
                        else if((hourReserved<2 && i==21) || value=="O" || Data.theUserRank == "0"){
                            buttonListener(value)
                        }
                    }
                    else {

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

}

package com.example.recharge

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_map_task.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PatientHomeActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database : DatabaseReference
    var userTaskList= ArrayList<UserTask>()
    var firebaseUser=""
    private var currWeek=5
    private var currDay=5
    val datestring = "2021-07-10"
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDate = LocalDate.parse(datestring, DateTimeFormatter.ISO_DATE)
    var isOnPeriod:Boolean=false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_home)
        val welcomeUser:TextView= findViewById<TextView>(R.id.ttwelcomeUser)
        val welcomeUser2:TextView= findViewById<TextView>(R.id.ttwelcomeUser2)
        val goTaskBtn:Button=findViewById(R.id.btnGotask)
        val intent = Intent(this@PatientHomeActivity,TodaytaskActivity::class.java)

        firebaseUser= auth.currentUser?.displayName.toString()
        database= FirebaseDatabase.getInstance().reference.child("UserTask")
        var query= database.orderByKey().equalTo(firebaseUser)
        var firstWeekTask=""


        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(checkCurrentPeriod())
                {
                    updateDateWeekProps()
                }
                for (ss in snapshot.children)
                {
                    val user= ss.getValue(UserTask::class.java)
                    if(user!=null)
                    {
                        userTaskList?.add(user)
                        when (currWeek) {
                            1 -> { firstWeekTask=user.selWeek1Act.toString() }
                            2 -> { firstWeekTask=user.selWeek2Act.toString() }
                            3 -> { firstWeekTask=user.selWeek3Act.toString() }
                            4 -> { firstWeekTask=user.selWeek4Act.toString() }
                        }
                    }
                }

                welcomeUser.text="Welcome "+firebaseUser+"\n\nAre Your Ready For Today's Task?"
                welcomeUser2.text="You are on day $currDay of week $currWeek ! \n\nYour first week task is -> '$firstWeekTask'"

                intent.putExtra("Username",firebaseUser)
                intent.putExtra("CurrentWeek",currWeek.toString())
                intent.putExtra("CurrentDay",currDay.toString())
                intent.putExtra("Task",firstWeekTask)
                goTaskBtn.setOnClickListener(){
                    startActivity(intent)
                }

            }
        })





        
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkCurrentPeriod():Boolean {
        val currDate= LocalDate.now()

        if(date!=null)
        {
            if(currDate>=date && currDate<= date.plusDays(28))
            {
                isOnPeriod=true
            }
        }
        return isOnPeriod
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDateWeekProps(){
        val currDate= LocalDate.now()

        when {
            currDate<date.plusDays(7) -> {
                currDay=8-date.plusDays(7).compareTo(currDate)
                currWeek=1
            }
            currDate<date.plusDays(14) -> {
                currDay=8-date.plusDays(14).compareTo(currDate)
                currWeek=2
            }
            currDate<date.plusDays(21) -> {
                currDay=date.plusDays(21).compareTo(currDate)
                currDay=8-currDay
                currWeek=3
            }
            currDate<date.plusDays(28) -> {
                currDay=8-date.plusDays(28).compareTo(currDate)
                currWeek=4
            }
        }

    }
}
package com.example.recharge

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_doc_dash.*
import kotlinx.android.synthetic.main.activity_doc_dash.docDashDrawerLayout
import kotlinx.android.synthetic.main.activity_doc_dash.navViewDD
import kotlinx.android.synthetic.main.activity_map_task.*
import kotlinx.android.synthetic.main.activity_patient_home.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class PatientHomeActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database : DatabaseReference
    var userTaskList= ArrayList<UserTask>()
    var firebaseUser=""
    private var currWeek=5
    private var currDay=5
    var datestring = "2020-07-10"
    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var date: LocalDate
    var isOnPeriod:Boolean=false
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_home)

        //logout toogle operations
        toggle= ActionBarDrawerToggle(this, patientDrawerLayout,R.string.open,R.string.close)
        patientDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true )
        val firebaseAuth: FirebaseAuth= FirebaseAuth.getInstance()

        val user=firebaseAuth.currentUser

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,gso)


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
                if(snapshot.value!=null) {
                    for (ss in snapshot.children) {
                        val user = ss.getValue(UserTask::class.java)
                        datestring = user?.startDate.toString()
                        date = LocalDate.parse(datestring, DateTimeFormatter.ISO_DATE)
                        if (checkCurrentPeriod()) {
                            updateDateWeekProps()
                            if (user != null) {
                                userTaskList?.add(user)
                                when (currWeek) {
                                    1 -> {
                                        firstWeekTask = user.selWeek1Act.toString()
                                    }
                                    2 -> {
                                        firstWeekTask = user.selWeek2Act.toString()
                                    }
                                    3 -> {
                                        firstWeekTask = user.selWeek3Act.toString()
                                    }
                                    4 -> {
                                        firstWeekTask = user.selWeek4Act.toString()
                                    }
                                }
                            }

                            welcomeUser.text =
                                "Welcome " + firebaseUser + "\n\nAre Your Ready For Today's Task?"
                            welcomeUser2.text =
                                "You are on day $currDay of week $currWeek ! \n\nYour first week task is -> '$firstWeekTask'"

                            intent.putExtra("Username", firebaseUser)
                            intent.putExtra("CurrentWeek", currWeek.toString())
                            intent.putExtra("CurrentDay", currDay.toString())
                            intent.putExtra("Task", firstWeekTask)
                            goTaskBtn.visibility = View.VISIBLE
                            goTaskBtn.setOnClickListener() {
                                startActivity(intent)
                            }
                        } else {
                            welcomeUser.text =
                                "Welcome " + firebaseUser + "\n\nYou don't have any activity for this week"
                            welcomeUser2.text = "Please contact your doctor for further details !"
                            goTaskBtn.visibility = View.GONE

                        }

                    }
                }
                else{
                    welcomeUser.text =
                        "Welcome " + firebaseUser + "\n\nYou don't have any activity for this week"
                    welcomeUser2.text = "Please contact your doctor for further details !"
                    goTaskBtn.visibility = View.GONE
                }
            }
        })


        navViewDD.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mItem1 -> Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()
                R.id.mItem2 -> Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()
                R.id.mLogout -> {
                    Toast.makeText(applicationContext, "Logged out from Home", Toast.LENGTH_SHORT).show()
                    firebaseAuth.signOut()
                    showlogoutConfirm()
//                    startActivity(Intent(this@PatientHomeActivity, MainActivity::class.java))
                    if(user==null){
                        startActivity(Intent(this@PatientHomeActivity, MainActivity::class.java))
                        Log.w("TAG", "logged out from DocDash")
                        Toast.makeText(this, "DocDash Logout", Toast.LENGTH_SHORT).show()
                    }

                }


            }
            true
        }





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
                currDay=8-Period.between(currDate,date.plusDays(7)).days
                currWeek=1
            }
            currDate<date.plusDays(14) -> {
//                currDay=8-date.plusDays(14).compareTo(currDate)
                currDay=8-Period.between(currDate,date.plusDays(14)).days
                currWeek=2
            }
            currDate<date.plusDays(21) -> {
//                currDay=date.plusDays(21).compareTo(currDate)
                currDay=8-Period.between(currDate,date.plusDays(21)).days
                currDay=8-currDay
                currWeek=3
            }
            currDate<date.plusDays(28) -> {
//                currDay=8-date.plusDays(28).compareTo(currDate)
                currDay=8-Period.between(currDate,date.plusDays(28)).days
                currWeek=4
            }
        }

    }


    private fun showlogoutConfirm(){
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to logout from this application ?")
            // if the dialog is cancelable
            .setCancelable(true)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener
            {
                    dialog, id -> googleSignInClient.signOut()
                startActivity(Intent(this@PatientHomeActivity, MainActivity::class.java))
                Log.w("TAG", "logged out from DocDash")
                Toast.makeText(this, "DocDash Logout", Toast.LENGTH_SHORT).show()


            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Logout")
        // show alert dialog
        alert.show()
    }
}
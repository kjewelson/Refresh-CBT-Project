package com.example.recharge

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_doc_dash.*


class DocDash : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_dash)
        Log.w("TAG", "DocDash:Just Entered")
//        val newTask: Button = findViewById(R.id.newTask)
//        val mapTask: Button = findViewById(R.id.mapActivity)
        toggle= ActionBarDrawerToggle(this, docDashDrawerLayout,R.string.open,R.string.close)
        docDashDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true )
        val firebaseAuth: FirebaseAuth= FirebaseAuth.getInstance()

        val user=firebaseAuth.currentUser

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()



        googleSignInClient= GoogleSignIn.getClient(this,gso)


        navViewDD.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mItem1 -> Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()
                R.id.mItem2 -> Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()
                R.id.mLogout -> {Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()

//                    clearAppData()
                    firebaseAuth.signOut()
                    showlogoutConfirm()
//                    startActivity(Intent(this@DocDash, MainActivity::class.java))
                    if(user==null){
                        startActivity(Intent(this@DocDash, MainActivity::class.java))
                        Log.w("TAG", "logged out from DocDash")
                        Toast.makeText(this, "DocDash Logout", Toast.LENGTH_SHORT).show()
                    }

                }


            }
            true
        }

        newTask.setOnClickListener {
            openTaskCreation()
        }

        mapTask.setOnClickListener {
            openMapActivity()
        }

        cardAnalytics.setOnClickListener {
            openAnalyticsPage()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
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
                    startActivity(Intent(this@DocDash, MainActivity::class.java))
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


    private fun clearAppData() {
//        try {
//            // clearing app data
//            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//                (getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData() // note: it has a return value!
//            } else {
//                val packageName = applicationContext.packageName
//                val runtime = Runtime.getRuntime()
//                runtime.exec("pm clear $packageName")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }
    private fun openTaskCreation() {
        startActivity(Intent(this@DocDash, NewtaskActivity::class.java))
        Log.w("TAG", "openTaskCreation:Clicked")
        Toast.makeText(this, "Create New Task", Toast.LENGTH_SHORT).show()
    }

    private fun openMapActivity() {
        startActivity(Intent(this@DocDash, MapTaskActivity::class.java))
        Log.w("TAG", "MapTask:Clicked")
        Toast.makeText(this, "Map User Tasks", Toast.LENGTH_SHORT).show()
    }

    private fun openAnalyticsPage(){
        startActivity(Intent(this@DocDash, AnalyticsActivity::class.java))
        Log.w("TAG", "AnalyticBtn:Clicked")
        Toast.makeText(this, "Explore patient Analytics", Toast.LENGTH_SHORT).show()
    }


}
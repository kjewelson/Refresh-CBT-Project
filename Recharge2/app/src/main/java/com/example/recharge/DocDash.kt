package com.example.recharge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_doc_dash.*
import kotlin.system.exitProcess

class DocDash : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_dash)
        Log.w("TAG", "DocDash:Just Entered")
        val newTask: Button = findViewById(R.id.newTask)
        val mapTask: Button = findViewById(R.id.mapActivity)
        toggle= ActionBarDrawerToggle(this, docDashDrawerLayout,R.string.open,R.string.close)
        docDashDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true )
        val firebaseAuth: FirebaseAuth= FirebaseAuth.getInstance()
        val user=firebaseAuth.currentUser


        navViewDD.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mItem1 -> Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()
                R.id.mItem2 -> Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()
                R.id.mLogout -> {Toast.makeText(applicationContext, "Clicked item 1 ", Toast.LENGTH_SHORT).show()
                    firebaseAuth.signOut()
                    startActivity(Intent(this@DocDash, MainActivity::class.java))
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

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
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


}
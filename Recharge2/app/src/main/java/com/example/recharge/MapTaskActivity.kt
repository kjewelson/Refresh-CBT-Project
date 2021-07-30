package com.example.recharge

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_map_task.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MapTaskActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    var list = ArrayList<String>()
    var Actlist = ArrayList<String>()
    var selUser = ""
    var selWeek1Act= ""
    var selWeek2Act= ""
    var selWeek3Act= ""
    var selWeek4Act= ""
    var selStartDate=""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_task)
        val mapSubmit : Button= findViewById(R.id.btnMapAct)
        database= FirebaseDatabase.getInstance().getReference("UserProfile")
        loadUserDD()
        loadCourseDD()
        mapSubmit.setOnClickListener(){
            saveUserActivity()
        }

    }

    private fun loadUserDD(){
        database.addValueEventListener(object :ValueEventListener{

        override fun onCancelled(error: DatabaseError) {
            Log.e("Cancel", error.toString())
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            for (data in snapshot.children){
                val model=data.getValue((UserProfile::class.java))
                list.add(model?.userName.toString())
            }
            if (list.size>0){
//                Toast.makeText(this@MapTaskActivity,"You select please"
//                    ,Toast.LENGTH_LONG).show()

                val adapter = ArrayAdapter<String>(this@MapTaskActivity,R.layout.support_simple_spinner_dropdown_item,list.toList())
                UserProfileSP.adapter=adapter
                UserProfileSP.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(adapterView: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        Toast.makeText(this@MapTaskActivity,"You selected ${adapterView?.getItemAtPosition(position)}"
                            ,Toast.LENGTH_LONG).show()
                        selUser= adapterView?.getItemAtPosition(position).toString()
                    }
                }
            }
        }
    })
    }

    private fun loadCourseDD(){
        database= FirebaseDatabase.getInstance().getReference("Task")
        database.addValueEventListener(object :ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel", error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val model=data.getValue((Task::class.java))
                    Actlist.add(model?.name.toString())
                }
                if (Actlist.size>0){
                    Toast.makeText(this@MapTaskActivity,"You select tasks"
                        ,Toast.LENGTH_LONG).show()

                    val adapter = ArrayAdapter<String>(this@MapTaskActivity,R.layout.support_simple_spinner_dropdown_item,Actlist.toList())
                    week1Act.adapter=adapter
                    week2Act.adapter=adapter
                    week3Act.adapter=adapter
                    week4Act.adapter=adapter

                    week1Act.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                        override fun onItemSelected(adapterView: AdapterView<*>?,view: View?,position: Int,id: Long) {
                            Toast.makeText(this@MapTaskActivity,"You selected ${adapterView?.getItemAtPosition(position)}"
                                ,Toast.LENGTH_LONG).show()
                            selWeek1Act=adapterView?.getItemAtPosition(position).toString()
                        }
                    }

                    week2Act.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(adapterView: AdapterView<*>?,view: View?,position: Int,id: Long) {
                            selWeek2Act=adapterView?.getItemAtPosition(position).toString()
                        }
                    }
                    week3Act.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(adapterView: AdapterView<*>?,view: View?,position: Int,id: Long) {
                            selWeek3Act=adapterView?.getItemAtPosition(position).toString()
                        }
                    }
                    week4Act.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(adapterView: AdapterView<*>?,view: View?,position: Int,id: Long) {
                            selWeek4Act=adapterView?.getItemAtPosition(position).toString()
                        }
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveUserActivity(){
        database= FirebaseDatabase.getInstance().getReference("UserTask")
        val selmonth  = findViewById<DatePicker>(R.id.dpStartDate).month.toString()
        val selDay = findViewById<DatePicker>(R.id.dpStartDate).dayOfMonth.toString()
        val selYear = findViewById<DatePicker>(R.id.dpStartDate).year.toString()

        selStartDate = selYear+"-"+selmonth+"-"+selDay

        val userTask=UserTask(selUser,selWeek1Act,selWeek2Act,selWeek3Act,selWeek4Act, LocalDate.now().toString(),selStartDate)

        Log.w("TAG", "$selUser create profile start")
        database.child(selUser).setValue(userTask).addOnSuccessListener {
            Toast.makeText(this, "Submitting Map Task",Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Map Task Failed",Toast.LENGTH_SHORT).show()
        }

    }

}
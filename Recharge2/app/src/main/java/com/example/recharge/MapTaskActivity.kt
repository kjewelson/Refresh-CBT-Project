package com.example.recharge

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.thecode.aestheticdialogs.*
import kotlinx.android.synthetic.main.activity_analytics.*
import kotlinx.android.synthetic.main.activity_map_task.*
import java.time.LocalDate
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
        loadUserDD()
        loadCourseDD()
        mapSubmit.setOnClickListener(){
            saveUserActivity()
        }

    }

    private fun loadUserDD(){
        database= FirebaseDatabase.getInstance().getReference("UserProfile")
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
        val selmonth  = when (dpStartDate.month) { in 1..9 -> "0"+((dpStartDate.month)+1).toString() else -> ((dpStartDate.month)+1).toString()}
        val selDay = when (dpStartDate.dayOfMonth) { in 1..9 -> "0"+(dpStartDate.dayOfMonth).toString() else -> (dpStartDate.dayOfMonth).toString()}
        val selYear = findViewById<DatePicker>(R.id.dpStartDate).year.toString()

        selStartDate = selYear+"-"+selmonth+"-"+selDay

        val userTask=UserTask(selUser,selWeek1Act,selWeek2Act,selWeek3Act,selWeek4Act, LocalDate.now().toString(),selStartDate)

        Log.w("TAG", "$selUser create profile start")
        database.child(selUser).setValue(userTask).addOnSuccessListener {
//            Toast.makeText(this, "Submitting Map Task",Toast.LENGTH_SHORT).show()

            showDialog("Success","Tasks has been assigned to the selected user")
        }.addOnFailureListener {
            Toast.makeText(this, "Map Task Failed",Toast.LENGTH_SHORT).show()
        }

    }

    public fun showDialog(title: String , message:String) {
        AestheticDialog.Builder(this, DialogStyle.FLAT, DialogType.SUCCESS)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setDarkMode(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    finish()
                    //actions...
                }
            })
            .show()
    }

}
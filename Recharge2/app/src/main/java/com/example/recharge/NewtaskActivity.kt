package com.example.recharge

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_newtask.*


class NewtaskActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    var dat1Type:String="1"
    var dat2Type:String="1"
    var dat3Type:String="1"
    var dat4Type:String="1"
    var dat5Type:String="1"
    var dat6Type:String="1"
    var dat7Type:String="1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtask)
        Log.w("TAG", "Newtask:Just Entered")

        val buttonNewAct:Button = findViewById(R.id.btnNewAct)
        buttonNewAct.setOnClickListener {
            createTask()
        }

        chipGroup_1.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(chipGroup_1,1)
            }
        }
        chipGroup_2.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(chipGroup_2,2)
            }
        }
        chipGroup_3.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(chipGroup_3,3)
            }
        }
        chipGroup_4.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(chipGroup_4,4)
            }
        }
        chipGroup_5.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(chipGroup_5,5)
            }
        }
        chipGroup_6.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(chipGroup_6,6)
            }
        }
        chipGroup_7.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                handleSelection(chipGroup_7,7)
            }
        }
    }


    // function to get chip group checked chips
    private fun handleSelection(chipGroup: ChipGroup, selDay: Int) {
        chipGroup.checkedChipIds.forEach{
            val chip = findViewById<Chip>(it)
            when(chip.text.toString()){
            (chipVideo_1.text)-> {
                when(selDay){
                    1->{videoUrl_1.visibility=View.VISIBLE
                        dat1Type = "3" }
                    2->{videoUrl_2.visibility=View.VISIBLE
                        dat2Type = "3" }
                    3->{videoUrl_3.visibility=View.VISIBLE
                        dat3Type = "3" }
                    4->{videoUrl_4.visibility=View.VISIBLE
                        dat4Type = "3" }
                    5->{videoUrl_5.visibility=View.VISIBLE
                        dat5Type = "3" }
                    6->{videoUrl_6.visibility=View.VISIBLE
                        dat6Type = "3" }
                    7->{videoUrl_7.visibility=View.VISIBLE
                        dat7Type = "3" }
                }
            }
            (chipTask_1.text)->
            {
                when(selDay){
                    1->{videoUrl_1.visibility=View.GONE
                        dat1Type = "1" }
                    2->{videoUrl_2.visibility=View.GONE
                        dat2Type = "1" }
                        3->{videoUrl_3.visibility=View.GONE
                        dat3Type = "1" }
                    4->{videoUrl_4.visibility=View.GONE
                        dat4Type = "1" }
                    5->{videoUrl_5.visibility=View.GONE
                        dat5Type = "1" }
                    6->{videoUrl_6.visibility=View.GONE
                        dat6Type = "1" }
                    7->{videoUrl_7.visibility=View.GONE
                        dat7Type = "1" }
                }
            }
            (chipScore_1.text)->
            {
                when(selDay){
                    1->{videoUrl_1.visibility=View.GONE
                        dat1Type = "2" }
                    2->{videoUrl_2.visibility=View.GONE
                        dat2Type = "2" }
                    3->{videoUrl_3.visibility=View.GONE
                        dat3Type = "2" }
                    4->{videoUrl_4.visibility=View.GONE
                        dat4Type = "2" }
                    5->{videoUrl_5.visibility=View.GONE
                        dat5Type = "2" }
                    6->{videoUrl_6.visibility=View.GONE
                        dat6Type = "2" }
                    7->{videoUrl_7.visibility=View.GONE
                        dat7Type = "2" }
                }
            }
            }
        }
    }

    private fun createTask(){

        database= FirebaseDatabase.getInstance().getReference("Task")

        val name = (findViewById(R.id.editTTActivityName) as EditText).text.toString()

        val desc = (findViewById(R.id.editTTActDesc) as EditText).text.toString()
        val day1at = (findViewById(R.id.editTTDay1Act) as EditText).text.toString()
        val day2at = (findViewById(R.id.editTTDay2Act) as EditText).text.toString()
        val day3at = (findViewById(R.id.editTTDay3Act) as EditText).text.toString()
        val day4at = (findViewById(R.id.editTTDay4Act) as EditText).text.toString()
        val day5at = (findViewById(R.id.editTTDay5Act) as EditText).text.toString()
        val day6at = (findViewById(R.id.editTTDay6Act) as EditText).text.toString()
        val day7at = (findViewById(R.id.editTTDay7Act) as EditText).text.toString()

        val day1wt = (findViewById(R.id.editTNDay1wt) as EditText).text.toString()
        val day2wt = (findViewById(R.id.editTNDay2wt) as EditText).text.toString()
        val day3wt = (findViewById(R.id.editTNDay3wt) as EditText).text.toString()
        val day4wt = (findViewById(R.id.editTNDay4wt) as EditText).text.toString()
        val day5wt = (findViewById(R.id.editTNDay5wt) as EditText).text.toString()
        val day6wt = (findViewById(R.id.editTNDay6wt) as EditText).text.toString()
        val day7wt = (findViewById(R.id.editTNDay7wt) as EditText).text.toString()

        val dailyFeatures1=DailyFeatures(day1at, day1wt,dat1Type,videoUrl_1.text.toString())
        val dailyFeatures2=DailyFeatures(day2at, day2wt,dat2Type,videoUrl_2.text.toString())
        val dailyFeatures3=DailyFeatures(day3at, day3wt,dat3Type,videoUrl_3.text.toString())
        val dailyFeatures4=DailyFeatures(day4at, day4wt,dat4Type,videoUrl_4.text.toString())
        val dailyFeatures5=DailyFeatures(day5at, day5wt,dat5Type,videoUrl_5.text.toString())
        val dailyFeatures6=DailyFeatures(day6at, day6wt,dat6Type,videoUrl_6.text.toString())
        val dailyFeatures7=DailyFeatures(day7at, day7wt,dat7Type,videoUrl_7.text.toString())

        val task=Task(name,dailyFeatures1,dailyFeatures2,dailyFeatures3
            ,dailyFeatures4,dailyFeatures5,dailyFeatures6,dailyFeatures7,desc)

        Log.w("TAG", desc)
        database.child(name).setValue(task).addOnSuccessListener {
            Toast.makeText(this, "Submitting new Activity $name",Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()
        }


    }
}
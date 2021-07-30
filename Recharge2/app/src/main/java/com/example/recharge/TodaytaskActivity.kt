package com.example.recharge

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.firebase.database.*
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_todaytask.*
import java.util.regex.Pattern
import com.google.android.youtube.player.YouTubeBaseActivity


class TodaytaskActivity : YouTubeBaseActivity() {

    private lateinit var database : DatabaseReference
    var todayTask=""
    var todayTaskwt=""
    lateinit var dailyFeatures: DailyFeatures
    lateinit var npScore: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todaytask)
        val name:String = intent.getStringExtra("Username").toString()
        val week:String = intent.getStringExtra("CurrentWeek").toString()
        val day:String = intent.getStringExtra("CurrentDay").toString()
        val task:String = intent.getStringExtra("Task").toString()
        val ttTaskQn:TextView= findViewById(R.id.ttTodayQuestion)
        val cbComplete:CheckBox= findViewById(R.id.cbComplete)
        npScore= findViewById<NumberPicker>(R.id.npScore)
        npScore.minValue=1
        npScore.maxValue=10
        npScore.setOnValueChangedListener{numberPicker,oldVal,newVal ->
            //Toast.makeText(this,"selected $newVal",Toast.LENGTH_LONG).show()
        }
//        var fragmentytqn =  supportFragmentManager.findFragmentById(R.id.fragmentYT) as YouTubePlayerSupportFragment
//        fragmentytqn.initialize("AIzaSyBbOLbrr2fZDq3fbFptJaWTD7WUUHx8VXw",object :YouTubePlayer.OnInitializedListener{
//
//            override fun onInitializationFailure(p0: YouTubePlayer.Provider?,p1: YouTubeInitializationResult?) {
//                TODO("Not yet implemented")
//            }
//            override fun onInitializationSuccess(provider: YouTubePlayer.Provider?,player: YouTubePlayer?,wasRestored: Boolean) {
//                if(player==null) return
//                if(wasRestored)
//                    player.play()
//                else
//                    player.cueVideo("mz1Bey6KjsQ")
//                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
//            }
//
//
//        })


//        Toast.makeText(this, "Hi $name $week $day $task welcome ",Toast.LENGTH_SHORT).show()
//        database= FirebaseDatabase.getInstance().getReference("Task")
        database= FirebaseDatabase.getInstance().reference.child("Task")
        var query= database.orderByKey().equalTo(task)

        query.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(ssElement in snapshot.children)
                {
                    val ttask= ssElement.getValue(Task::class.java)
                    if(ttask!=null)
                    {
                        when(day){
                            "1"-> {todayTask=ttask.dailyFeatures1?.dayAct.toString()
                            todayTaskwt= ttask.dailyFeatures1?.dayActWt.toString()
                                dailyFeatures= ttask.dailyFeatures1?.copy()!!}

                            "2"-> {todayTask=ttask.dailyFeatures2?.dayAct.toString()
                            todayTaskwt= ttask.dailyFeatures1?.dayActWt.toString()
                                dailyFeatures= ttask.dailyFeatures2?.copy()!!}

                            "3"-> {todayTask=ttask.dailyFeatures3?.dayAct.toString()
                            todayTaskwt= ttask.dailyFeatures1?.dayActWt.toString()
                                dailyFeatures= ttask.dailyFeatures3?.copy()!!}

                            "4"-> {todayTask=ttask.dailyFeatures4?.dayAct.toString()
                            todayTaskwt= ttask.dailyFeatures1?.dayActWt.toString()
                                dailyFeatures= ttask.dailyFeatures4?.copy()!!}

                            "5"-> {todayTask=ttask.dailyFeatures5?.dayAct.toString()
                                todayTaskwt= ttask.dailyFeatures5?.dayActWt.toString()
                                dailyFeatures= ttask.dailyFeatures5?.copy()!!
                            }

                            "6"-> {todayTask=ttask.dailyFeatures6?.dayAct.toString()
                                todayTaskwt= ttask.dailyFeatures6?.dayActWt.toString()
                                dailyFeatures= ttask.dailyFeatures6?.copy()!!
                            }
                            "7"-> {todayTask=ttask.dailyFeatures7?.dayAct.toString()
                                todayTaskwt= ttask.dailyFeatures7?.dayActWt.toString()
                                dailyFeatures= ttask.dailyFeatures7?.copy()!!
                            }


                        }

                    }
                }
                when(dailyFeatures.activityType)
                {
                    "1"-> {ttTaskQn.text=dailyFeatures.dayAct.toString()
                        //fragVideo.visibility= View.VISIBLE
                        youtubeplayer.visibility= View.GONE
                        taskCheckboxLayout.visibility= View.VISIBLE
                        scoreLayout.visibility=View.GONE}
                    "2"-> {ttTaskQn.text=dailyFeatures.dayAct.toString()
                        //fragVideo.visibility= View.VISIBLE
                        youtubeplayer.visibility= View.GONE
                        taskCheckboxLayout.visibility= View.GONE
                        scoreLayout.visibility=View.VISIBLE}
                    "3"-> {//ttTaskQn.text=dailyFeatures.videoUrl.toString() + " Please click"
                        //fragVideo.visibility= View.VISIBLE
                        taskCheckboxLayout.visibility= View.VISIBLE
                        scoreLayout.visibility=View.GONE
                        getYoutubeVideoIdFromUrl(dailyFeatures.videoUrl.toString())?.let {
                            initializePlayer(it)
                        }
                    }

                }


            }

        })

    }

    fun getYoutubeVideoIdFromUrl(inUrl:String):String? {
        if(inUrl.lowercase().contains("youtube.be")){
            return inUrl.substring(inUrl.lastIndexOf("/")+1)
        }
        val pattern ="(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern= Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(inUrl)
        return if (matcher.find()){
            matcher.group()
        }
        else null
    }

    private fun initializePlayer(videoId:String){
        youtubeplayer.initialize(getString(R.string.api_key),object: YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean,
            ) {
                p1!!.loadVideo(videoId)
                p1.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?,
            ) {
                Toast.makeText(applicationContext,"error occured", Toast.LENGTH_LONG).show()
            }

        })
    }
}
package com.example.recharge

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.firebase.database.*
import com.google.gson.Gson
import com.thecode.aestheticdialogs.*
import kotlinx.android.synthetic.main.activity_todaytask.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.regex.Pattern
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class TodaytaskActivity : YouTubeBaseActivity() {

    private lateinit var database : DatabaseReference
    var todayTask=""
    var todayTaskwt=""
    private var week=""
    private var weekString=""
    private var name=""
    private var day=""
    private var task=""
    lateinit var ttask:Task
    lateinit var dailyFeatures: DailyFeatures
    lateinit var npScore: NumberPicker
    var data_tag=""
    var data_con=""
    var mLSubmission = MLSubmission()

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todaytask)
        name= intent.getStringExtra("Username").toString()
        week= intent.getStringExtra("CurrentWeek").toString()
        day = intent.getStringExtra("CurrentDay").toString()
        task = intent.getStringExtra("Task").toString()
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

        btnComplete.setOnClickListener(){
                submitTodayActivity()
        }

        database= FirebaseDatabase.getInstance().reference.child("Task")
        var query= database.orderByKey().equalTo(task)

        query.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(ssElement in snapshot.children)
                {
                    ttask= ssElement.getValue(Task::class.java)!!
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
                    "3"-> {ttTaskQn.text=dailyFeatures.dayAct.toString()
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

    private fun submitTodayActivity() {

        val weightMul=npScore.value
        var userWeeklyData=UserWeeklyData()
        var userTaskSubmit=UserTaskSubmit()
        userWeeklyData.activityName=task
        userTaskSubmit.selUser=name
        database= FirebaseDatabase.getInstance().getReference("UserTaskSubmit")

        when(day){
            "1"->{userWeeklyData.dat1point= if(ttask.dailyFeatures1?.activityType.toString()=="2")
                    ((ttask.dailyFeatures1?.dayActWt?.toInt())?.times(weightMul)).toString()
            else ((ttask.dailyFeatures1?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.isWeekComplete=false
                userWeeklyData.maxPoint=ttask.totalWeight.toString()
                userWeeklyData.dat1MaxPoint=((ttask.dailyFeatures1?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.hmd1Text=ttDailyQnAns.text.toString()}
            "2"->{userWeeklyData.dat2point= if(ttask.dailyFeatures2?.activityType.toString()=="2")
                ((ttask.dailyFeatures2?.dayActWt?.toInt())?.times(weightMul)).toString()
            else ((ttask.dailyFeatures2?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.isWeekComplete=false
                userWeeklyData.dat2MaxPoint=((ttask.dailyFeatures2?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.hmd2Text=ttDailyQnAns.text.toString()}
            "3"->{userWeeklyData.dat3point= if(ttask.dailyFeatures3?.activityType.toString()=="2")
                ((ttask.dailyFeatures3?.dayActWt?.toInt())?.times(weightMul)).toString()
            else ((ttask.dailyFeatures3?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.isWeekComplete=false
                userWeeklyData.dat3MaxPoint=((ttask.dailyFeatures3?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.hmd3Text=ttDailyQnAns.text.toString()}
            "4"->{userWeeklyData.dat4point= if(ttask.dailyFeatures4?.activityType.toString()=="2")
                ((ttask.dailyFeatures4?.dayActWt?.toInt())?.times(weightMul)).toString()
            else ((ttask.dailyFeatures4?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.isWeekComplete=false
                userWeeklyData.dat4MaxPoint=((ttask.dailyFeatures4?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.hmd4Text=ttDailyQnAns.text.toString()}
            "5"->{userWeeklyData.dat5point= if(ttask.dailyFeatures5?.activityType.toString()=="2")
                ((ttask.dailyFeatures5?.dayActWt?.toInt())?.times(weightMul)).toString()
            else ((ttask.dailyFeatures5?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.isWeekComplete=false
                userWeeklyData.dat5MaxPoint=((ttask.dailyFeatures5?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.hmd5Text=ttDailyQnAns.text.toString()}
            "6"->{userWeeklyData.dat6point= if(ttask.dailyFeatures6?.activityType.toString()=="2")
                ((ttask.dailyFeatures6?.dayActWt?.toInt())?.times(weightMul)).toString()
            else ((ttask.dailyFeatures6?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.isWeekComplete=false
                userWeeklyData.dat6MaxPoint=((ttask.dailyFeatures6?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.hmd6Text=ttDailyQnAns.text.toString()}
            "7"->{userWeeklyData.dat7point= if(ttask.dailyFeatures7?.activityType.toString()=="2")
                ((ttask.dailyFeatures7?.dayActWt?.toInt())?.times(weightMul)).toString()
            else ((ttask.dailyFeatures7?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.isWeekComplete=true
                userWeeklyData.dat7MaxPoint=((ttask.dailyFeatures7?.dayActWt?.toInt())?.times(10)).toString()
                userWeeklyData.hmd7Text=ttDailyQnAns.text.toString()}
        }
        when(week){
            "1"-> {userTaskSubmit.weeklyData=userWeeklyData
                    weekString="weeklyData1"
                saveUserDailyData(userTaskSubmit)}
            "2"-> {userTaskSubmit.weeklyData=userWeeklyData
                    weekString="weeklyData2"
                saveUserDailyData(userTaskSubmit)}
            "3"-> {userTaskSubmit.weeklyData=userWeeklyData
                    weekString="weeklyData3"
                saveUserDailyData(userTaskSubmit)}
            "4"-> {userTaskSubmit.weeklyData=userWeeklyData
                    weekString="weeklyData4"
                saveUserDailyData(userTaskSubmit)}
        }

        Log.w("TAG", "Call save database")



    }

    fun saveUserDailyData(userTaskSubmit:UserTaskSubmit){
        when(day) {
            "1"-> {
                database.child(name).child(weekString).setValue(userTaskSubmit.weeklyData).addOnSuccessListener {
//                    Toast.makeText(this, "Submitting weekly data for Day - $day",Toast.LENGTH_SHORT).show()
//                    finish()
                    postVolley(userTaskSubmit.weeklyData?.hmd1Text)
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
            }
            "2"-> {
                database.child(name).child("$weekString/hmd2Text").setValue(userTaskSubmit.weeklyData?.hmd2Text).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat2MaxPoint").setValue(userTaskSubmit.weeklyData?.dat2MaxPoint).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat2point").setValue(userTaskSubmit.weeklyData?.dat2point).addOnSuccessListener {
//                    Toast.makeText(this, "Submitting weekly data for Day - $day",Toast.LENGTH_SHORT).show()
//                    finish()
                    postVolley(userTaskSubmit.weeklyData?.hmd2Text)
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
            }
            "3"-> {
                database.child(name).child("$weekString/hmd3Text").setValue(userTaskSubmit.weeklyData?.hmd3Text).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat3MaxPoint").setValue(userTaskSubmit.weeklyData?.dat3MaxPoint).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat3point").setValue(userTaskSubmit.weeklyData?.dat3point).addOnSuccessListener {
//                    Toast.makeText(this, "Submitting weekly data for Day - $day",Toast.LENGTH_SHORT).show()
//                    finish()
                    postVolley(userTaskSubmit.weeklyData?.hmd3Text)
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
            }
            "4"-> {
                database.child(name).child("$weekString/hmd4Text").setValue(userTaskSubmit.weeklyData?.hmd4Text).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat4MaxPoint").setValue(userTaskSubmit.weeklyData?.dat4MaxPoint).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat4point").setValue(userTaskSubmit.weeklyData?.dat4point).addOnSuccessListener {
//                    Toast.makeText(this, "Submitting weekly data for Day - $day",Toast.LENGTH_SHORT).show()
//                    finish()
                    postVolley(userTaskSubmit.weeklyData?.hmd4Text)
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
            }
            "5"-> {
                database.child(name).child("$weekString/hmd5Text").setValue(userTaskSubmit.weeklyData?.hmd5Text).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat5MaxPoint").setValue(userTaskSubmit.weeklyData?.dat5MaxPoint).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat5point").setValue(userTaskSubmit.weeklyData?.dat5point).addOnSuccessListener {
//                    Toast.makeText(this, "Submitting weekly data for Day - $day",Toast.LENGTH_SHORT).show()
//                    finish()
                    postVolley(userTaskSubmit.weeklyData?.hmd5Text)
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
            }
            "6"-> {
                database.child(name).child("$weekString/hmd6Text").setValue(userTaskSubmit.weeklyData?.hmd6Text).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat6MaxPoint").setValue(userTaskSubmit.weeklyData?.dat6MaxPoint).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat6point").setValue(userTaskSubmit.weeklyData?.dat6point).addOnSuccessListener {
//                    Toast.makeText(this, "Submitting weekly data for Day - $day",Toast.LENGTH_SHORT).show()
//                    finish()
                    postVolley(userTaskSubmit.weeklyData?.hmd6Text)
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
            }
            "7"-> {
                database.child(name).child("$weekString/hmd7Text").setValue(userTaskSubmit.weeklyData?.hmd7Text).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat7MaxPoint").setValue(userTaskSubmit.weeklyData?.dat7MaxPoint).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/weekComplete").setValue(userTaskSubmit.weeklyData?.isWeekComplete).addOnSuccessListener {
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                database.child(name).child("$weekString/dat7point").setValue(userTaskSubmit.weeklyData?.dat7point).addOnSuccessListener {
//                    Toast.makeText(this, "Submitting weekly data for Day - $day",Toast.LENGTH_SHORT).show()
//                    finish()
                    postVolley(userTaskSubmit.weeklyData?.hmd7Text)
                }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
            }
        }

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


    private fun postVolley (hmdText: String?) {

        val queue = Volley.newRequestQueue(this)
        var moodmsg=""
        val url = "https://api.monkeylearn.com/v3/classifiers/cl_pi3C7JiL/classify/"
        val jsonArray = JSONArray(listOf(hmdText))

        val jsonBody = JSONObject()
        jsonBody.put("data", jsonArray)
        val mRequestBody = jsonBody.toString()
        val stringReq : StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    // response
                    var strResp = response.toString()
                    Log.d("API-Response", strResp)
                    val jsonArray = JSONArray(strResp)
                    val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                    val data_classification= jsonObject.get("classifications").toString()

                    val innerJsonArray=JSONArray(data_classification)
                    val innerJsonObject: JSONObject = innerJsonArray.getJSONObject(0)
                    mLSubmission.sent_Tag = innerJsonObject.get("tag_name") as String
                    when(mLSubmission.sent_Tag){
                        "Positive"-> {mLSubmission.sent_score="5"
                            moodmsg="Cheers Keep Going"}
                        "Neutral"-> {mLSubmission.sent_score="3"
                            moodmsg="Good Do More"}
                        "Negative"->{mLSubmission.sent_score="1"
                            moodmsg="You Can Improve"}
                    }
//                    mLSubmission.confidence = innerJsonObject.get("confidence") as String
//                    val jsonObjectInner: JSONObject = data_classification.getJSONObject(0)
//                    val tag= jsonObjectInner.get("tag_name")
//                    var mlResponsevar = gson?.fromJson(strResp, MLResponse::class.java)
//                    Log.d("API-Confidence", mlResponsevar.classifications[0].tag_name)
                    when(day){
                        "1"->{
                            database.child(name).child("$weekString/dat1sent").setValue(mLSubmission.sent_Tag).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                            database.child(name).child("$weekString/dat1Mood").setValue(mLSubmission.sent_score).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                        }
                        "2"->{
                            database.child(name).child("$weekString/dat2sent").setValue(mLSubmission.sent_Tag).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                            database.child(name).child("$weekString/dat2Mood").setValue(mLSubmission.sent_score).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}

                        }
                        "3"->{
                            database.child(name).child("$weekString/dat3sent").setValue(mLSubmission.sent_Tag).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                            database.child(name).child("$weekString/dat3Mood").setValue(mLSubmission.sent_score).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                        }
                        "4"->{
                            database.child(name).child("$weekString/dat4sent").setValue(mLSubmission.sent_Tag).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                            database.child(name).child("$weekString/dat4Mood").setValue(mLSubmission.sent_score).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                        }
                        "5"->{
                            database.child(name).child("$weekString/dat5sent").setValue(mLSubmission.sent_Tag).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                            database.child(name).child("$weekString/dat5Mood").setValue(mLSubmission.sent_score).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                        }
                        "6"->{
                            database.child(name).child("$weekString/dat6sent").setValue(mLSubmission.sent_Tag).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                            database.child(name).child("$weekString/dat6Mood").setValue(mLSubmission.sent_score).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                        }
                        "7"->{
                            database.child(name).child("$weekString/dat7sent").setValue(mLSubmission.sent_Tag).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                            database.child(name).child("$weekString/dat7Mood").setValue(mLSubmission.sent_score).addOnSuccessListener {
                            }.addOnFailureListener {Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()}
                        }

                    }
                    showDialog(moodmsg, "You have successfully completed the Day - $day task. Nice Day")
                    Log.d("API-Response", strResp)
                },
                Response.ErrorListener { error ->
                    Log.d("API", "error => $error")
                }
            ){
                override fun getBodyContentType(): String? {
                    return "application/json; charset=utf-8"
                }
//                override fun getBody(): ByteArray {
//                    return requestBody.toByteArray(Charset.defaultCharset())
//                }
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        if (mRequestBody == null) null else return mRequestBody.toByteArray(Charsets.UTF_8)
                    } catch (uee: UnsupportedEncodingException) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody,
                            "utf-8")
                        null
                    }
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params["Content-Type"] = "application/json; charset=UTF-8"
                    params["Authorization"] = "Token 5f38bc00b7c4023ed8140fe0dc426792a071e4ef"
                    return params
                }
            }
        queue.add(stringReq)
    }


}
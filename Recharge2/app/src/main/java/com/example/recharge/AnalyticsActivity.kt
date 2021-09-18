package com.example.recharge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_analytics.*
import kotlinx.android.synthetic.main.activity_map_task.*

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    var list = ArrayList<String>()
    val listofWeeklyDate= ArrayList<UserWeeklyData>()
    var selUser = ""

    private lateinit var lineChart: LineChart
    private lateinit var lineChart1: BarChart
    private var scoreList = ArrayList<Score>()
    private var scoreList1 = ArrayList<Score>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)
        database= FirebaseDatabase.getInstance().getReference("UserTaskSubmit")
        loadUserDD()

        lineChart = findViewById(R.id.lineChart)
        lineChart1 = findViewById(R.id.lineChart1)
//        val selUser1 = spinUserSelect.toString()
        getActivityPoints("Xcelerate C")

//        initLineChart()

    }

    private fun loadUserDD(){
        database.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel", error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
//                    val model=data.getValue((UserTaskDetail::class.java))
                    list.add(data?.key.toString())
                }
                if (list.size>0){
//                Toast.makeText(this@MapTaskActivity,"You select please"
//                    ,Toast.LENGTH_LONG).show()

                    val adapter = ArrayAdapter<String>(this@AnalyticsActivity,R.layout.support_simple_spinner_dropdown_item,list.toList())
                    spinUserSelect.adapter=adapter
                    spinUserSelect.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selUser= adapterView?.getItemAtPosition(position).toString()

                            Toast.makeText(this@AnalyticsActivity,"You selected ${adapterView?.getItemAtPosition(position)}"
                                , Toast.LENGTH_LONG).show()
                            getActivityPoints(selUser)
                        }
                    }
                }
            }
        })
    }


    private fun setDataToLineChart() {

        //now draw bar chart with dynamic data
        val lineEntry = ArrayList<Entry>()
        val lineEntry1 = ArrayList<Entry>()

        val barEntry = ArrayList<BarEntry>()


        var daysinit=-1
        for (i in listofWeeklyDate)
        {   daysinit++
            i.dat1point?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry.add(it) }
            i.dat1Mood?.let { BarEntry(it.toFloat(),daysinit) }?.let { barEntry.add(it) }
            i.dat1MaxPoint?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry1.add(it) }
            daysinit++
            i.dat2point?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry.add(it) }
            i.dat2Mood?.let { BarEntry(it.toFloat(),daysinit) }?.let { barEntry.add(it) }
            i.dat2MaxPoint?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry1.add(it) }
            daysinit++
            i.dat3point?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry.add(it) }
            i.dat3Mood?.let { BarEntry(it.toFloat(),daysinit) }?.let { barEntry.add(it) }
            i.dat3MaxPoint?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry1.add(it) }
            daysinit++
            i.dat4point?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry.add(it) }
            i.dat4Mood?.let { BarEntry(it.toFloat(),daysinit) }?.let { barEntry.add(it) }
            i.dat4MaxPoint?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry1.add(it) }
            daysinit++
            i.dat5point?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry.add(it) }
            i.dat5Mood?.let { BarEntry(it.toFloat(),daysinit) }?.let { barEntry.add(it) }
            i.dat5MaxPoint?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry1.add(it) }
            daysinit++
            i.dat6point?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry.add(it) }
            i.dat6Mood?.let { BarEntry(it.toFloat(),daysinit) }?.let { barEntry.add(it) }
            i.dat6MaxPoint?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry1.add(it) }
            daysinit++
            i.dat7point?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry.add(it) }
            i.dat7Mood?.let { BarEntry(it.toFloat(),daysinit) }?.let { barEntry.add(it) }
            i.dat7MaxPoint?.let { Entry(it.toFloat(),daysinit) }?.let { lineEntry1.add(it) }

        }
        if(listofWeeklyDate.isNotEmpty())
            listofWeeklyDate.clear()



        val length= lineEntry.size
        val length1= lineEntry1.size

        val xvalue= ArrayList<String>()
        var daycnt=0
        for (eachVal in lineEntry){
            daycnt++
            xvalue.add("Day$daycnt")
        }

        val xvallength2= xvalue.size


        val lineDataSets = ArrayList<LineDataSet>()
        val barDataSets = ArrayList<BarDataSet>()

//        scoreList = getScoreList()
//        scoreList1 = getScoreList1()

//        //you can replace this data object with  your custom object
//        for (i in scoreList.indices) {
//            val score = scoreList[i]
//            entries.add(Entry(i.toFloat(), score.score.toFloat()))
//        }
//        for (i in scoreList1.indices) {
//            val score = scoreList1[i]
//            entries1.add(Entry(i.toFloat(), score.score.toFloat()))
//        }

        val lineDataSet = LineDataSet(lineEntry, "Max score")
        lineDataSet.color=resources.getColor(R.color.green_connectify)
        val lineDataSet1 = LineDataSet(lineEntry1, "Obtained score")
        lineDataSet1.color=resources.getColor(R.color.red_connectify)

        lineDataSets.add(lineDataSet)
        lineDataSets.add(lineDataSet1)
        val data =LineData(xvalue,lineDataSets as List<ILineDataSet>)

        val barDataSet = BarDataSet(barEntry, "Daily Mood")
        barDataSet.color=resources.getColor(R.color.md_blue_grey_500)

        barDataSets.add(barDataSet)
        val data1 =BarData(xvalue,barDataSets as List<IBarDataSet>)


        lineChart.data= data
        lineChart1.data= data1

    }

    // simulate api call
    // we are initialising it directly
    private fun getScoreList(): ArrayList<Score> {
        scoreList.add(Score("John", 56))
        scoreList.add(Score("Rey", 75))
        scoreList.add(Score("Steve", 85))
        scoreList.add(Score("Kevin", 45))
        scoreList.add(Score("Jeff", 63))

        return scoreList
    }
    private fun getScoreList1(): ArrayList<Score> {
        scoreList.add(Score("John", 66))
        scoreList.add(Score("Rey", 85))
        scoreList.add(Score("Steve", 95))
        scoreList.add(Score("Kevin", 55))
        scoreList.add(Score("Jeff", 73))

        return scoreList
    }
    private fun getActivityPoints(selUser: String) {

        database= FirebaseDatabase.getInstance().getReference("UserTaskSubmit")
        var query= database.child(selUser)

        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value!=null) {
                    for (ss in snapshot.children) {
                        var user = ss.getValue(UserWeeklyData::class.java)
                        if (user != null) {
                            listofWeeklyDate.add(user)
                        }
                    }
                    setDataToLineChart()
                }
            }

        })


    }

}
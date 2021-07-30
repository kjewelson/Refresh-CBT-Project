package com.example.recharge

import java.text.DateFormat
import java.time.LocalDateTime

data class UserTask(
    var selUser: String? = null, var selWeek1Act: String? = null, var selWeek2Act: String? = null,
    var selWeek3Act: String? = null, var selWeek4Act: String? = null, var dateSaved:String ? =null,
    var startDate: String? = null
){}

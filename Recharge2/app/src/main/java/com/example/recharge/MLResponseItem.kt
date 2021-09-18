package com.example.recharge

data class MLResponseItem(
    val classifications: List<Classification>,
    val error: Boolean,
    val external_id: Any,
    val text: String
)
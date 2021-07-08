package com.dxn.noiseandpollutiontracker.network

import com.google.gson.annotations.SerializedName

data class FeedDto(
    @SerializedName("created_at")
    val date:String,

    @SerializedName("entry_id")
    val id : Int,

    @SerializedName("field1")
    val field1 : Double,

    @SerializedName("field2")
    val field2:Double
)
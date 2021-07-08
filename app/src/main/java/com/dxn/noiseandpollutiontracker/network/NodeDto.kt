package com.dxn.noiseandpollutiontracker.network

import com.google.gson.annotations.SerializedName

data class NodeDto(

    @SerializedName("latitude")
    val latitude : String,

    @SerializedName("longitude")
    val longitude : String,

)
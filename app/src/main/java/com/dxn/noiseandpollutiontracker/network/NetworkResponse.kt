package com.dxn.noiseandpollutiontracker.network

import com.dxn.noiseandpollutiontracker.network.FeedDto
import com.google.gson.annotations.SerializedName

data class NetworkResponse(
    @SerializedName("feeds")
    val feeds: List<FeedDto>,

    @SerializedName("channel")
    val node : NodeDto
)
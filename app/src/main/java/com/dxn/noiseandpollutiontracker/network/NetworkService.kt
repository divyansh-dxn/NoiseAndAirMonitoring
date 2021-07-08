package com.dxn.noiseandpollutiontracker.network

import com.dxn.noiseandpollutiontracker.network.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("/channels/1429721/feeds.json")
    suspend fun get(@Query("api_key") apiKey:String, @Query("results") results:Int): NetworkResponse
}
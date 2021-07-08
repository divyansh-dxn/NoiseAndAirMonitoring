package com.dxn.noiseandpollutiontracker.repository

import com.dxn.noiseandpollutiontracker.models.Feed
import com.dxn.noiseandpollutiontracker.network.NetworkService
import com.dxn.noiseandpollutiontracker.util.DataMapper
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeedsRepository {
//    Todo : Replace api key
    private val API_KEY = "<YOUR_API_KEY>"

    private var feeds = listOf<Feed>()
    val networkService: NetworkService = Retrofit.Builder()
        .baseUrl("https://api.thingspeak.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(NetworkService::class.java)

    suspend fun fetchData() : HashMap<String,Any> {
        val data = HashMap<String,Any>()
        val response = networkService.get(apiKey = API_KEY,results = 90)
        val node = DataMapper.NodeDtoToNode(response.node)
        feeds = DataMapper.feedsDtoToFeeds(response.feeds)
        data["feeds"] = feeds
        data["node"] = node
        return data
    }
}
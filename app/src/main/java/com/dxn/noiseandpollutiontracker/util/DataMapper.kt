package com.dxn.noiseandpollutiontracker.util

import com.dxn.noiseandpollutiontracker.models.Feed
import com.dxn.noiseandpollutiontracker.models.Node
import com.dxn.noiseandpollutiontracker.network.FeedDto
import com.dxn.noiseandpollutiontracker.network.NodeDto


object DataMapper {
    fun feedDtoToFeed(dto: FeedDto): Feed {
        return Feed(
            createdAt = dto.date,
            id = dto.id,
            noiseLevel = dto.field1,
            airConcentration = dto.field2
        )
    }

    fun feedsDtoToFeeds(feedsDto: List<FeedDto>): List<Feed> {
        val feeds = mutableListOf<Feed>()
        feedsDto.forEach { dto ->
            feeds.add(feedDtoToFeed(dto))
        }
        return feeds
    }

    fun NodeDtoToNode(nodeDto: NodeDto): Node {
        return Node(
            latitude = nodeDto.latitude.toFloat(),
            longitude = nodeDto.longitude.toFloat(),
        )
    }
}
package com.dxn.noiseandpollutiontracker.models

data class Feed(
    val createdAt: String,
    val id: Int,
    val noiseLevel: Double,
    val airConcentration: Double
)
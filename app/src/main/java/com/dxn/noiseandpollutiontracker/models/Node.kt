package com.dxn.noiseandpollutiontracker.models

data class Node(
    val latitude : Float,
    val longitude : Float,
    val avgNoise : Float = 0f,
    val avgPPM : Float = 0f
)
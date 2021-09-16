package com.demo.aggregationpipelines.movie

data class MovieRatingsAvg(
    val title: String,
    val avg: Double,
)

data class MoviesCount(
    val title: String,
    val count: Int,
)

package com.demo.aggregationpipelines.movie

import com.demo.aggregationpipelines.config.KlaxonDate
import java.time.Instant

data class Movie(
    val title: String,
    val genres: List<String>,
    val ratings: List<Rating>,
    val id: String? = null,
)

data class Rating(
    val rating: Double,
    @KlaxonDate
    val submissionDate: Instant,
)

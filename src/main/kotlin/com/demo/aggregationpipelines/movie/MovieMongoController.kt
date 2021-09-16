package com.demo.aggregationpipelines.movie

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/mongo")
class MovieMongoController(
    private val movieRepository: MovieRepository,
) {

    @GetMapping("/1")
    fun findAllComediesWithAvgRatingGte3(): List<MovieRatingsAvg> {
        var movies: List<MovieRatingsAvg>
        val elapsed = measureTimeMillis {
            movies = movieRepository.findAllComediesWithAvgRatingGte3()
        }

        println("$elapsed")
        return movies
    }

    @GetMapping("/2")
    fun findAllRatedBetweenDatesWithAvgRatingGte4(): List<MovieRatingsAvg> {
        var movies: List<MovieRatingsAvg>
        val elapsed = measureTimeMillis {
            movies = movieRepository.findAllRatedBetweenDatesWithAvgRatingGte4()
        }

        println("$elapsed")
        return movies
    }

    @GetMapping("/3")
    fun find10MoviesMostRatedBetweenDates(): List<MoviesCount> {
        var movies: List<MoviesCount>
        val elapsed = measureTimeMillis {
            movies = movieRepository.find10MoviesMostRatedBetweenDates()
        }

        println("$elapsed")
        return movies
    }
}

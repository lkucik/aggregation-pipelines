package com.demo.aggregationpipelines.movie

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/app")
class MovieController(private val movieService: MovieService) {

    @GetMapping("/1")
    fun findAllComediesWithAvgRatingGte3(): List<MovieRatingsAvg> {
        var movies: List<MovieRatingsAvg>
        val elapsed = measureTimeMillis {
            movies = movieService.findAllComediesWithAvgRatingGte3().collectList().block()!!
        }

        println("$elapsed")
        return movies
    }

    @GetMapping("/1-filtered")
    fun findAllFilteredComediesWithAvgRatingGte3(): List<MovieRatingsAvg> {
        var movies: List<MovieRatingsAvg>
        val elapsed = measureTimeMillis {
            movies = movieService.findAllFilteredComediesWithAvgRatingGte3().collectList().block()!!
        }

        println("$elapsed")
        return movies
    }

    @GetMapping("/2")
    fun findAllRatedBetweenDatesWithAvgRatingGte4(): List<MovieRatingsAvg> {
        var movies: List<MovieRatingsAvg>
        val elapsed = measureTimeMillis {
            movies = movieService.findAllRatedBetweenDatesWithAvgRatingGte4().collectList().block()!!
        }

        println("$elapsed")
        return movies
    }

    @GetMapping("/3")
    fun find10ComediesMostRatedBetweenDates(): List<MoviesCount> {
        var movies: List<MoviesCount>
        val elapsed = measureTimeMillis {
            movies = movieService.find10ComediesMostRatedBetweenDates().collectList().block()!!
        }

        println("$elapsed")
        return movies
    }

    @GetMapping("/3-filtered")
    fun find10FilteredComediesMostRatedBetweenDates(): List<MoviesCount> {
        var movies: List<MoviesCount>
        val elapsed = measureTimeMillis {
            movies = movieService.find10FilteredComediesMostRatedBetweenDates().collectList().block()!!
        }

        println("$elapsed")
        return movies
    }
}

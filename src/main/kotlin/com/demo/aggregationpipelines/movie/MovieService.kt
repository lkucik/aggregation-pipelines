package com.demo.aggregationpipelines.movie

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.Comparator.comparingInt

@Service
class MovieService(private val movieRepository: MovieRepository) {

    fun findAllComediesWithAvgRatingGte3(): Flux<MovieRatingsAvg> =
        findAllWithAvgRatingGte3(
            movieRepository.findAll()
                .filter { movie -> movie.genres.contains("Comedy") }
        )

    fun findAllFilteredComediesWithAvgRatingGte3(): Flux<MovieRatingsAvg> =
        findAllWithAvgRatingGte3(
            movieRepository.findAllByGenresContaining("Comedy")
        )

    fun findAllRatedBetweenDatesWithAvgRatingGte4(): Flux<MovieRatingsAvg> {
        val start = Instant.parse("1970-01-10T00:00:01Z")
        val end = Instant.parse("1970-01-11T00:00:01Z")
        return movieRepository.findAll()
            .map { movie ->
                MovieRatingsAvg(
                    title = movie.title,
                    avg = movie.ratings.filter { it.submissionDate in start..end }.map { it.rating }.average()
                )
            }
            .filter { movie -> movie.avg >= 4 }
    }

    fun find10ComediesMostRatedBetweenDates(): Flux<MoviesCount> =
        find10MostRatedBetweenDates(
            movieRepository.findAll()
                .filter { movie -> movie.genres.contains("Comedy") }
        )

    fun find10FilteredComediesMostRatedBetweenDates(): Flux<MoviesCount> =
        find10MostRatedBetweenDates(
            movieRepository.findAllByGenresContaining("Comedy")
        )

    private fun findAllWithAvgRatingGte3(movies: Flux<Movie>): Flux<MovieRatingsAvg> =
        movies
            .map { movie ->
                MovieRatingsAvg(
                    title = movie.title,
                    avg = movie.ratings.map { it.rating }.average()
                )
            }
            .filter { movie -> movie.avg >= 3 }

    private fun find10MostRatedBetweenDates(movies: Flux<Movie>): Flux<MoviesCount> {
        val start = Instant.parse("1970-01-10T00:00:01Z")
        val end = Instant.parse("1970-01-11T00:00:01Z")
        return movies
            .map { movie ->
                MoviesCount(
                    title = movie.title,
                    count = movie.ratings.count { it.submissionDate in start..end }
                )
            }
            .sort(comparingInt<MoviesCount?> { it.count }.reversed())
            .take(10)
    }
}

package com.demo.aggregationpipelines.movie

import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.aggregation.AggregationOptions
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Instant

@Repository
interface MovieRepository : ReactiveMongoRepository<Movie, String>, CustomMovieRepository {
    fun findAllByGenresContaining(genre: String): Flux<Movie>
}

interface CustomMovieRepository {
    fun findAllComediesWithAvgRatingGte3(): List<MovieRatingsAvg>
    fun findAllRatedBetweenDatesWithAvgRatingGte4(): List<MovieRatingsAvg>
    fun find10MoviesMostRatedBetweenDates(): List<MoviesCount>
}

@Component
class CustomMovieRepositoryImpl(private val mongoTemplate: MongoTemplate) : CustomMovieRepository {
    override fun findAllComediesWithAvgRatingGte3(): List<MovieRatingsAvg> {
        val aggregation = newAggregation(
            match(Criteria.where("genres").`is`("Comedy")),
            unwind("ratings"),
            group("_id", "title").avg("ratings.rating").`as`("avg"),
            project("_id.title", "avg"),
            match(Criteria.where("avg").gte(3)),
        )

        // --------AggregationUpdate--------------
//        val update = newUpdate()
//            .set("average").toValue(ArithmeticOperators.valueOf("ratings").avg())
//            .set("opinion").toValue(
//                ConditionalOperators.switchCases(
//                    `when`(valueOf("average").greaterThanEqualToValue(4)).then("Very good"),
//                    `when`(valueOf("average").greaterThanEqualToValue(3)).then("Good"),
//                    `when`(valueOf("average").greaterThanEqualToValue(2)).then("Bad")
//                )
//                    .defaultTo("-")
//            )
//
//        mongoTemplate.update(Movie::class.java)
//            .apply(update)
//            .all()
        // -----------------------------------------

        return mongoTemplate.aggregate(aggregation, "movie", MovieRatingsAvg::class.java).mappedResults
    }

    override fun findAllRatedBetweenDatesWithAvgRatingGte4(): List<MovieRatingsAvg> {
        val aggregation = newAggregation(
            unwind("ratings"),
            match(
                Criteria.where("ratings.submissionDate")
                    .gte(Instant.parse("1970-01-10T00:00:01Z"))
                    .lte(Instant.parse("1970-01-11T00:00:01Z"))
            ),
            group("_id", "title").avg("ratings.rating").`as`("avg"),
            project("_id.title", "avg"),
            match(Criteria.where("avg").gte(4)),
        )
        return mongoTemplate.aggregate(aggregation, "movie", MovieRatingsAvg::class.java).mappedResults
    }

    override fun find10MoviesMostRatedBetweenDates(): List<MoviesCount> {
        val aggregation = newAggregation(
            match(Criteria.where("genres").`is`("Comedy")),
            unwind("ratings"),
            match(
                Criteria.where("ratings.submissionDate")
                    .gte(Instant.parse("1970-01-10T00:00:01Z"))
                    .lte(Instant.parse("1970-01-11T00:00:01Z"))
            ),
            group("_id", "title").count().`as`("count"),
            project("_id.title", "count"),
            sort(DESC, "count"),
            limit(10),
        )
        return mongoTemplate.aggregate(aggregation, "movie", MoviesCount::class.java).mappedResults
    }
}

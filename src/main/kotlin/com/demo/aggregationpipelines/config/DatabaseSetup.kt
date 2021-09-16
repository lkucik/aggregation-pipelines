package com.demo.aggregationpipelines.config

import com.beust.klaxon.Klaxon
import com.demo.aggregationpipelines.movie.Movie
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.Index
import org.springframework.stereotype.Component

@Component
class DatabaseSetup(private val mongoTemplate: MongoTemplate) {

    @EventListener(ApplicationStartedEvent::class)
    fun init() {
        initDatabase()
//        initIndices()
    }

    private fun initDatabase() {
        if (!mongoTemplate.collectionExists("movie")) {
            val movies = Klaxon()
                .fieldConverter(KlaxonDate::class, dateConverter)
                .parseArray<Movie>(javaClass.classLoader.getResourceAsStream("movies.json")!!)!!.toList()

            mongoTemplate.createCollection("movie")
            mongoTemplate.insertAll(movies)
        }
    }

    private fun initIndices() {
        mongoTemplate.indexOps("movie").ensureIndex(Index().on("title", Sort.Direction.ASC).unique())
    }
}

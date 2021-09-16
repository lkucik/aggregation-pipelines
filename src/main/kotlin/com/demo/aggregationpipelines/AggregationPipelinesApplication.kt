package com.demo.aggregationpipelines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
//        scanBasePackages = [
//            "com.demo.aggregationpipelines",
//        ]
)
class AggregationPipelinesApplication

fun main(args: Array<String>) {
    runApplication<AggregationPipelinesApplication>(*args)
}

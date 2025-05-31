package com.outfitgo.store.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random

object ReviewUtils {
    const val MIN_RATING = 1.0
    const val MAX_RATING = 5.0

    fun generateRandomRating(): Double {
        return Random.Default.nextDouble(from = MIN_RATING, until = MAX_RATING)
    }

    /**
     * Generates a random list of Review objects.
     *
     * @param count The number of reviews to generate.
     * @return A list of randomly generated Review objects.
     */
    fun generateRandomReviews(count: Int = 6): List<Review> {
        val reviewerNames = listOf(
            "Alice Smith", "Bob Johnson", "Charlie Brown", "Diana Prince", "Eve Adams",
            "Frank White", "Grace Lee", "Harry Wilson", "Ivy King", "Jack Taylor"
        )
        val reviewComments = listOf(
            "Absolutely love this product! Highly recommend.",
            "Great quality for the price. Very satisfied.",
            "It's good, but could be better. Met expectations.",
            "Not quite what I expected, but it does the job.",
            "Excellent! Exceeded my expectations.",
            "Very comfortable and stylish.",
            "Works perfectly, no issues at all.",
            "A bit disappointed with the durability.",
            "Fantastic value, couldn't be happier.",
            "Good product, happy with the purchase. Connects easily.",
            "The best purchase I've made this year!",
            "Could use some improvements, but decent overall.",
            "Exactly as described. Fast shipping.",
            "I'm impressed with the features.",
            "Fairly good, but I've seen better."
        )

        val reviews = mutableListOf<Review>()
        val today = LocalDate.now()

        for (i in 0 until count) {
            val id = UUID.randomUUID().toString()
            val reviewerName = reviewerNames.random()
            val rating = generateRandomRating()
            val comment = reviewComments.random()

            // Generate a random date within the last year
            val daysAgo = Random.Default.nextLong(0, 365)
            val reviewDate = today.minusDays(daysAgo)
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dateString = reviewDate.format(dateFormatter)

            reviews.add(Review(id, reviewerName, rating, comment, dateString))
        }
        return reviews
    }
}
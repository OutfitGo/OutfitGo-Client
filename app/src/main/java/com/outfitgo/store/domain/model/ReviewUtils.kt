package com.outfitgo.store.domain.model

import kotlin.random.Random

object ReviewUtils {
    const val MIN_RATING = 1
    const val MAX_RATING = 5

    private val reviews by lazy {
        listOf(
            Review(
                id = "rev001",
                reviewerName = "Alice Wonderland",
                rating = 5,
                comment = "Absolutely fantastic! This app has everything I need and runs super smoothly.",
                dateString = "2024-05-10"
            ),
            Review(
                id = "rev002",
                reviewerName = "Bob The Builder",
                rating = 4,
                comment = "Pretty good overall. There are a few minor bugs, but nothing that breaks the experience.",
                dateString = "2024-05-12"
            ),
            Review(
                id = "rev003",
                reviewerName = "Charlie Chaplin",
                rating = 2,
                comment = "Disappointed with the latest update. It keeps crashing on my device.",
                dateString = "2024-05-15"
            ),
            Review(
                id = "rev004",
                reviewerName = "Diana Prince",
                rating = 5,
                comment = "Couldn't ask for more! A truly indispensable tool for my daily tasks.",
                dateString = "2024-05-18"
            ),
            Review(
                id = "rev005",
                reviewerName = "Eve Harrington",
                rating = 3,
                comment = "It's okay. Some features are great, but others feel a bit clunky.",
                dateString = "2024-05-20"
            ),
            Review(
                id = "rev006",
                reviewerName = "Frankenstein's Monster",
                rating = 1,
                comment = "Terrible. The interface is confusing and it's constantly freezing.",
                dateString = "2024-05-22"
            ),
            Review(
                id = "rev007",
                reviewerName = "Grace Kelly",
                rating = 4,
                comment = "Almost perfect! A few more customization options would make it a 5-star.",
                dateString = "2024-05-25"
            ),
            Review(
                id = "rev008",
                reviewerName = "Harry Potter",
                rating = 5,
                comment = "Magical! This app makes my life so much easier. Highly recommend it.",
                dateString = "2024-05-28"
            ),
            Review(
                id = "rev009",
                reviewerName = "Ivy Poison",
                rating = 2,
                comment = "Expected more. It's functional, but lacks polish and frequently lags.",
                dateString = "2024-05-30"
            ),
            Review(
                id = "rev010",
                reviewerName = "Jack Sparrow",
                rating = 4,
                comment = "A solid app for what it does. Could use some UI improvements.",
                dateString = "2024-06-01"
            ),
            Review(
                id = "rev011",
                reviewerName = "Katniss Everdeen",
                rating = 5,
                comment = "Incredible performance and extremely user-friendly. My go-to app now!",
                dateString = "2024-06-03"
            ),
            Review(
                id = "rev012",
                reviewerName = "Lex Luthor",
                rating = 1,
                comment = "Completely unusable. Crashes on startup every single time.",
                dateString = "2024-06-05"
            ),
            Review(
                id = "rev013",
                reviewerName = "Marge Simpson",
                rating = 3,
                comment = "It does the job, but there are better alternatives out there.",
                dateString = "2024-06-07"
            ),
            Review(
                id = "rev014",
                reviewerName = "Ned Flanders",
                rating = 4,
                comment = "Homer-ific! This app is truly blessed. Very stable and useful.",
                dateString = "2024-06-09"
            ),
            Review(
                id = "rev015",
                reviewerName = "Olive Oyl",
                rating = 3,
                comment = "Decent app, but the ads are a bit too intrusive.",
                dateString = "2024-06-10"
            ),
            Review(
                id = "rev016",
                reviewerName = "Peter Pan",
                rating = 5,
                comment = "Neverland-worthy! So much fun to use and incredibly intuitive.",
                dateString = "2024-06-11"
            ),
            Review(
                id = "rev017",
                reviewerName = "Queen Elizabeth",
                rating = 4,
                comment = "Quite satisfactory. A few minor enhancements would make it perfect for my needs.",
                dateString = "2024-06-12"
            ),
            Review(
                id = "rev018",
                reviewerName = "Ron Weasley",
                rating = 3,
                comment = "It's alright. A bit slow sometimes, but gets the job done eventually.",
                dateString = "2024-06-13"
            ),
            Review(
                id = "rev019",
                reviewerName = "Sherlock Holmes",
                rating = 4,
                comment = "Elementary, my dear app! Solves all my problems with ease and precision.",
                dateString = "2024-06-13"
            ),
            Review(
                id = "rev020",
                reviewerName = "Tina Turner",
                rating = 5,
                comment = "Simply the best! This app is a rockstar. Flawless execution.",
                dateString = "2024-06-13"
            )
        )
    }

    fun generateRandomRating(): Int {
        return Random.Default.nextInt(from = MIN_RATING, until = MAX_RATING)
    }

    fun generateRandomReviews(count: Int = 6): List<Review> {
        return reviews.shuffled().take(count)
    }
}
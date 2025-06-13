package com.outfitgo.store

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.google.android.libraries.places.api.Places

@HiltAndroidApp
class OutfitGoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, BuildConfig.GOOGLE_MAPS_API_KEY)
    }
}
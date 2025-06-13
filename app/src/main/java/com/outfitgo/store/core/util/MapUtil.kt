package com.outfitgo.store.core.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.Locale

object MapUtil {
    fun getLocationAddressLine(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): Address? {
        return try {
            val addresses = Geocoder(context, Locale.getDefault()).getFromLocation(
                latitude,
                longitude,
                1
            )
            addresses?.firstOrNull()
        }catch (_: Exception){
            null
        }
    }
}
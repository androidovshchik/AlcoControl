package com.alcocontrol

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
@Suppress("unused")
class MainApp : Application(), ImageLoaderFactory {

    @Inject
    lateinit var gson: Gson

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(applicationContext)
        Kotpref.gson = gson
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .availableMemoryPercentage(0.5)
            .bitmapPoolPercentage(0.5)
            .componentRegistry {
                add(SvgDecoder(applicationContext))
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(null)
                    .build()
            }
            .build()
    }
}
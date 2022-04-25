package com.alcocontrol.inject

import android.content.Context
import android.content.res.AssetManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alcocontrol.data.Database
import com.alcocontrol.data.DrinkDao
import com.alcocontrol.data.Preferences
import com.alcocontrol.util.LocalDateDeserializer
import com.alcocontrol.util.LocalDateSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.threeten.bp.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()
    }

    @Provides
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

    @Provides
    fun providePreferences(@ApplicationContext context: Context): Preferences {
        return Preferences(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "app.db")
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .createFromAsset("app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDrinkDao(db: Database): DrinkDao {
        return db.drinkDao()
    }
}
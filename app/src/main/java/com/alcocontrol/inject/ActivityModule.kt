package com.alcocontrol.inject

import android.content.Context
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Executor

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideExecutor(@ApplicationContext context: Context): Executor {
        return ContextCompat.getMainExecutor(context)
    }
}
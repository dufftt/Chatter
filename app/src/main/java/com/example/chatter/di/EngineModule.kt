package com.example.chatter.di

import com.example.chatter.data.RealMlcEngineImpl
import com.example.chatter.domain.MlcEngineWrapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EngineModule {

    @Binds
    @Singleton
    abstract fun bindMlcEngineWrapper(
        realMlcEngineImpl: RealMlcEngineImpl
    ): MlcEngineWrapper
}

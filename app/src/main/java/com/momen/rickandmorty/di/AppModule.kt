package com.momen.rickandmorty.di

import com.momen.rickandmorty.data.api.RickMortyApi
import com.momen.rickandmorty.data.repository.CharacterRepository
import com.momen.rickandmorty.data.repository.CharacterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRickMortyApi(client: OkHttpClient): RickMortyApi {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickMortyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(api: RickMortyApi): CharacterRepository {
        return CharacterRepositoryImpl(api)
    }

}
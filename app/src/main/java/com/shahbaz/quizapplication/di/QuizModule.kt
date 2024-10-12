package com.shahbaz.quizapplication.di

import com.shahbaz.quizapplication.repo.CategoryRepo
import com.shahbaz.quizapplication.retrofit.QuizApiService
import com.shahbaz.quizapplication.util.Constant.QUIZ_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class QuizModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(QUIZ_BASE_URL) // Replace with your base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): QuizApiService {
        return retrofit.create(QuizApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryRepo(apiService: QuizApiService): CategoryRepo {
        return CategoryRepo(apiService)
    }
}
package com.shahbaz.quizapplication.retrofit

import com.shahbaz.quizapplication.datamodel.CategoryList
import com.shahbaz.quizapplication.util.Constant.QUIZ_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApiService {
    @GET("categories")
    fun getAllCategory(
        @Query("apiKey") apiKey: String = QUIZ_API,
    ):Call<CategoryList>
}
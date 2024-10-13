package com.shahbaz.quizapplication.retrofit

import com.shahbaz.quizapplication.datamodel.category.CategoryList
import com.shahbaz.quizapplication.datamodel.questions.QuestionsList
import com.shahbaz.quizapplication.util.Constant.QUIZ_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface QuizApiService {
    @GET("categories")
    fun getAllCategory(
        @Query("apiKey") apiKey: String = QUIZ_API,
    ): Call<CategoryList>


    @GET("questions")
    fun getQuestions(
        @Query("apiKey") apiKey: String = QUIZ_API,
        @Query("category") category: String,
        @Query("limit") limit: Int,
    ): Call<QuestionsList>
}
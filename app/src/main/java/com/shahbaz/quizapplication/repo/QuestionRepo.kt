package com.shahbaz.quizapplication.repo

import com.shahbaz.quizapplication.datamodel.questions.QuestionsList
import com.shahbaz.quizapplication.datamodel.questions.QuestionsListItem
import com.shahbaz.quizapplication.retrofit.QuizApiService
import com.shahbaz.quizapplication.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionRepo(
    private val quizApiService: QuizApiService,
) {
    private val _questionStatus =
        MutableStateFlow<Resources<List<QuestionsListItem>>>(Resources.Unspecified())

    val questionStatus = _questionStatus.asStateFlow()

    fun getQuestions(category: String, limit: Int) {
        _questionStatus.value = Resources.Loading()
        quizApiService.getQuestions(
            category = category,
            limit = limit
        )
            .enqueue(object : Callback<QuestionsList> {
                override fun onResponse(
                    call: Call<QuestionsList>,
                    response: Response<QuestionsList>,
                ) {
                    if (response.isSuccessful) {
                        _questionStatus.value = Resources.Success(response.body()!!)
                    } else {
                        _questionStatus.value = Resources.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<QuestionsList>, t: Throwable) {
                    _questionStatus.value = Resources.Error(t.message.toString())
                }
            })
    }
}
package com.shahbaz.quizapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.quizapplication.repo.QuestionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionViewmodel @Inject constructor(
    private val questionRepo: QuestionRepo,
) : ViewModel() {
    val questionStatus = questionRepo.questionStatus

    fun getQuestion(category: String, limit: Int) {
        questionRepo.getQuestions(category, limit)
    }
}
package com.shahbaz.quizapplication.datamodel.questions

data class QuestionsListItem(
    val answers: Answers,
    val category: String,
    val correct_answer: String,
    val correct_answers: CorrectAnswers,
    val description: Any,
    val difficulty: String,
    val explanation: String,
    val id: Int,
    val multiple_correct_answers: String,
    val question: String,
    val tags: List<Tag>,
    val tip: Any
)
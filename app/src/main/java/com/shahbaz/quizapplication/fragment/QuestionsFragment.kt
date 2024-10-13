package com.shahbaz.quizapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.quizapplication.adapter.OptionsAdapter
import com.shahbaz.quizapplication.databinding.FragmentQuestionsBinding
import com.shahbaz.quizapplication.datamodel.questions.Answers
import com.shahbaz.quizapplication.datamodel.questions.CorrectAnswers
import com.shahbaz.quizapplication.datamodel.questions.QuestionsListItem
import com.shahbaz.quizapplication.util.Resources
import com.shahbaz.quizapplication.viewmodel.QuestionViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding
    private val questionViewmodel by viewModels<QuestionViewmodel>()
    private val args by navArgs<QuestionsFragmentArgs>()
    private var questionList = listOf<QuestionsListItem>()
    private var currentQuestionIndex = 0
    private var isMultipleAnswer: Boolean = false
    private var correctAnswerList = listOf<CorrectAnswers>()
    private var userAnswers = mutableMapOf<Int, List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = args.category
        questionViewmodel.getQuestion(category, 10)
        observeQuestionData()

        binding.nextQuestions.setOnClickListener {
            if (currentQuestionIndex < questionList.size - 1) {
                currentQuestionIndex++
                updateUIWithCurrentQuestion()
            }else{
                evaluateAnswers()
            }
        }

        binding.previousQuestion.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                updateUIWithCurrentQuestion()
            } else {
                Toast.makeText(requireContext(), "This is first question", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun observeQuestionData() {
        lifecycleScope.launch {
            questionViewmodel.questionStatus.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("TAG", "observeQuestionData: ${it.data}")
                        it.data?.let { questions ->
                            questionList = questions // Save the question list
                            currentQuestionIndex = 0 // Start from the first question
                            updateUIWithCurrentQuestion()
                        }
                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun updateUIWithCurrentQuestion() {
        val currentQuestion = questionList[currentQuestionIndex]
        isMultipleAnswer = currentQuestion.multiple_correct_answers == "true"
        correctAnswerList = listOf(currentQuestion.correct_answers)
        Log.d("CorrectanswerList",correctAnswerList.toString())
        val correctAnswers = mutableMapOf<String, String>()
        correctAnswers["answer_a"] = currentQuestion.correct_answers.answer_a_correct
        correctAnswers["answer_b"] = currentQuestion.correct_answers.answer_b_correct
        correctAnswers["answer_c"] = currentQuestion.correct_answers.answer_c_correct
        correctAnswers["answer_d"] = currentQuestion.correct_answers.answer_d_correct
        correctAnswers["answer_e"] = currentQuestion.correct_answers.answer_e_correct
        correctAnswers["answer_f"] = currentQuestion.correct_answers.answer_f_correct
        binding.apply {
            question.text = currentQuestion.question // Set the current question text
            val adapter =
                OptionsAdapter(
                    currentQuestion.answers as Answers,
                    isMultipleAnswer,
                    correctAnswers = correctAnswers,
                    onAnswerSelected = {selectedAnswers ->
                        userAnswers[currentQuestion.id] = selectedAnswers
                    }
                  )
            optionsRecyclerView.adapter = adapter
            optionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun evaluateAnswers() {
        var correctCount = 0
        var incorrectCount = 0
        var unattemptedCount = 0

        // Iterate through all questions to evaluate answers
        for (question in questionList) {
            // Collect correct answers based on the correct_answers properties
            val correctAnswers = mutableSetOf<String>()

            if (question.correct_answers.answer_a_correct == "true") correctAnswers.add("answer_a")
            if (question.correct_answers.answer_b_correct == "true") correctAnswers.add("answer_b")
            if (question.correct_answers.answer_c_correct == "true") correctAnswers.add("answer_c")
            if (question.correct_answers.answer_d_correct == "true") correctAnswers.add("answer_d")
            if (question.correct_answers.answer_e_correct == "true") correctAnswers.add("answer_e")
            if (question.correct_answers.answer_f_correct == "true") correctAnswers.add("answer_f")

            val userSelectedAnswers = userAnswers[question.id]?.toSet() ?: emptySet() // User's selected answers

            if (userSelectedAnswers.isEmpty()) {
                unattemptedCount++
                Log.d("Unattempted", "Question ${question.id} was not attempted.")
            } else if (userSelectedAnswers == correctAnswers) {
                correctCount++
                Log.d("Correct", "Question ${question.id}: Correct!")
            } else {
                incorrectCount++
                Log.d("Wrong", "Question ${question.id}: Wrong answers selected. User: $userSelectedAnswers, Correct: $correctAnswers")
            }
        }

        // Prepare result message
        val totalQuestions = questionList.size
        val resultMessage = """
        Results:
        Total Questions: $totalQuestions
        Correct Answers: $correctCount
        Incorrect Answers: $incorrectCount
        Unattempted Questions: $unattemptedCount
        Percentage: ${((correctCount.toDouble() / totalQuestions) * 100).toInt()}%
    """.trimIndent()

        // Display results in a Toast message or Log
        Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show()
        Log.d("QuizResults", resultMessage)
    }


}
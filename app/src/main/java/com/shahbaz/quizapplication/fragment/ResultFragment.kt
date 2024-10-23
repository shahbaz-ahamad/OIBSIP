package com.shahbaz.quizapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shahbaz.quizapplication.R
import com.shahbaz.quizapplication.databinding.FragmentResultBinding


class ResultFragment : Fragment() {


    private lateinit var binding: FragmentResultBinding
    private val args by navArgs<ResultFragmentArgs>()
    var correctCount = 0
    var incorrectCount = 0
    var unattemptedCount = 0
    var totalQuestion = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correctCount = it.getString("correctAnswerCount").toString().toInt()
            incorrectCount = it.getString("inCorrectAnswerCount").toString().toInt()
            unattemptedCount = it.getString("unAttempetedCount").toString().toInt()
            totalQuestion = it.getString("totalQuestion").toString().toInt()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUi()
    }

    private fun updateUi() {
        binding.apply {
            correctProgressBar.progress = (correctCount * 100) / totalQuestion
            incorrectProgressBar.progress = (incorrectCount * 100) / totalQuestion
            unattemptedProgressBar.progress = (unattemptedCount * 100) / totalQuestion
            summaryTextView.text =
                "You have answered $correctCount out of $totalQuestion questions correctly!"
            correctTextView.text = "Correct Answers: $correctCount"
            incorrectTextView.text = "Incorrect Answers: $incorrectCount"
            unattemptedTextView.text = "Unattempted Questions: $unattemptedCount"
            retakeQuizButton.setOnClickListener {
                //navigate to home framgent by removing every thing from backstack
                findNavController().popBackStack(R.id.homeFragment, true)
            }
        }
    }

}
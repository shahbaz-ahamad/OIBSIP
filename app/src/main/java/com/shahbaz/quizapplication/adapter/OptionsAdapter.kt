package com.shahbaz.quizapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.quizapplication.databinding.OptionsItemBinding
import com.shahbaz.quizapplication.datamodel.questions.Answers

class OptionsAdapter(
    private val answers: Answers, // Each question has one Answers object
    private val isMultipleChoice: Boolean,
    private val correctAnswers: Map<String, String>, // Correct answers as a map
    private val onAnswerSelected: (List<String>) -> Unit // Callback to pass user-selected answers
) : RecyclerView.Adapter<OptionsAdapter.OptionViewholder>() {

    // List to track user-selected answers
    private val selectedAnswers = mutableSetOf<String>()

    // Collect non-null answers in a list
    private val answerList: List<String> = listOfNotNull(
        answers.answer_a,
        answers.answer_b,
        answers.answer_c,
        answers.answer_d,
        answers.answer_e as? String, // Safe cast Any to String, ignore if not a String
        answers.answer_f as? String
    )

    private val answerMap: Map<String, String?> = mapOf(
        "answer_a" to answers.answer_a,
        "answer_b" to answers.answer_b,
        "answer_c" to answers.answer_c,
        "answer_d" to answers.answer_d,
        "answer_e" to answers.answer_e as? String,
        "answer_f" to answers.answer_f as? String
    )

    class OptionViewholder(
        val binding: OptionsItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            answer: String?,
            answerKey: String,
            isMultipleChoice: Boolean,
            isSelected: Boolean,
            isCorrect: Boolean,
            onMultipleOptionSelected: (String, Boolean) -> Unit,
            onSingleOptionSelected: (String) -> Unit
        ) {
            // Set the answer text
            binding.options.text = answer

            if (isMultipleChoice) {
                binding.checkBox.visibility = View.VISIBLE
                binding.radioButton.visibility = View.GONE



                // Handle CheckBox selection
                binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onMultipleOptionSelected(answerKey, isChecked)
                }

            } else {
                binding.checkBox.visibility = View.GONE
                binding.radioButton.visibility = View.VISIBLE

                // Highlight correct or wrong answers
                binding.radioButton.isChecked = isSelected

                // Handle RadioButton selection
                binding.radioButton.setOnClickListener {
                    onSingleOptionSelected(answerKey)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewholder {
        return OptionViewholder(
            OptionsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    override fun onBindViewHolder(holder: OptionViewholder, position: Int) {
        val currentAnswer = answerList[position]
        val answerKey = answerMap.keys.elementAt(position)
        val isSelected = selectedAnswers.contains(answerKey)
        val isCorrect = correctAnswers[answerKey] == "true"

        holder.bind(
            answer = currentAnswer,
            answerKey = answerKey,
            isMultipleChoice = isMultipleChoice,
            isSelected = isSelected,
            isCorrect = isCorrect,
            onMultipleOptionSelected = { answer, isChecked ->
                if (isChecked) {
                    selectedAnswers.add(answer) // Add to selected answers
                } else {
                    selectedAnswers.remove(answer) // Remove from selected answers
                }
                onAnswerSelected(selectedAnswers.toList()) // Notify the parent of the selected answers
            },
            onSingleOptionSelected = { selectedAnswer ->
                selectedAnswers.clear() // Clear previously selected answers
                selectedAnswers.add(selectedAnswer) // Add new selection
                onAnswerSelected(selectedAnswers.toList()) // Notify the parent of the selected answers
                notifyDataSetChanged() // Refresh the adapter to reflect the selected answer
            }
        )
    }
}

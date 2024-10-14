package com.shahbaz.calculator

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shahbaz.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentInput = ""
    private var isNewOperation = true
    private var firstOperand = 0.0
    private var secondOperand = 0.0
    private var lastoperation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListeners()
    }

    private fun setListeners() {

        binding.apply {

            //for the number button
            oneButton.setOnClickListener { appendNumber("1") }
            twoButton.setOnClickListener { appendNumber("2") }
            threeButton.setOnClickListener { appendNumber("3") }
            fourButton.setOnClickListener { appendNumber("4") }
            fiveButton.setOnClickListener { appendNumber("5") }
            sixButton.setOnClickListener { appendNumber("6") }
            sevenButton.setOnClickListener { appendNumber("7") }
            eightButton.setOnClickListener { appendNumber("8") }
            nineButton.setOnClickListener { appendNumber("9") }
            zeroButton.setOnClickListener { appendNumber("0") }
            doubleZeroButton.setOnClickListener { appendNumber("00") }


            acButton.setOnClickListener {
                clear()
            }

            //for the operation button
            addButton.setOnClickListener { handleOperation("+") }
            minusButton.setOnClickListener { handleOperation("-") }
            multiplyButton.setOnClickListener { handleOperation("*") }
            divideButton.setOnClickListener { handleOperation("/") }

            //for the equal button
            equalButton.setOnClickListener {
                calculateResult()
            }
            //for the back button
            backbutton.setOnClickListener {
                backbuttonClick()
            }

            //for the percent button
            percentButton.setOnClickListener {
                percentOperation()
            }

            decimalButton.setOnClickListener {
                addDecimal()
            }
        }


    }

    private fun backbuttonClick() {
        if (currentInput.isNotEmpty()) {
            if (currentInput.endsWith(" ")) {
                // If the current input ends with a space, remove the last 3 characters (operator and spaces)
                currentInput = currentInput.substring(0, currentInput.length - 3)
            } else {
                // Otherwise, just remove the last character
                currentInput = currentInput.substring(0, currentInput.length - 1)
            }
            binding.screen.text = currentInput
        }
    }


    private fun percentOperation() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDouble() / 100
            currentInput = value.toString()
            binding.screen.text = currentInput
        }
    }

    private fun handleOperation(operator: String) {
        if (currentInput.isNotEmpty()) {
            val tokens = currentInput.split(" ")

            // Only parse the first operand if it's a valid number
            if (tokens.size == 1) {
                firstOperand = currentInput.toDoubleOrNull() ?: return // Parse only if it's a valid number
                lastoperation = operator
                currentInput += " $operator "
                binding.screen.text = currentInput
                isNewOperation = false
            } else if (tokens.size == 3) {
                // If already in the form "number operator number", calculate the result first
                calculateResult()
                lastoperation = operator
                currentInput += " $operator "
                binding.screen.text = currentInput
                isNewOperation = false
            }
        }
    }


    private fun addDecimal() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            binding.screen.text = currentInput
        }

    }

    private fun calculateResult() {
        if (currentInput.isNotEmpty()) {
            // Split the current input by spaces
            val tokens = currentInput.split(" ")

            if (tokens.size < 3) {
                // Not enough elements to perform a calculation
                return
            }

            var result = tokens[0].toDoubleOrNull() ?: return // Start with the first number

            // Iterate through the tokens and perform calculations
            var index = 1
            while (index < tokens.size - 1) {
                val operator = tokens[index]
                val nextOperand = tokens[index + 1].toDoubleOrNull() ?: return

                // Perform the operation
                when (operator) {
                    "+" -> result += nextOperand
                    "-" -> result -= nextOperand
                    "*" -> result *= nextOperand
                    "/" -> {
                        if (nextOperand == 0.0) {
                            // Handle division by zero
                            binding.screen.text = "Error"
                            return
                        }
                        result /= nextOperand
                    }
                    else -> {
                        // Unknown operator
                        return
                    }
                }
                index += 2 // Move to the next operator
            }

            // Update the result
            currentInput = result.toString()
            binding.screen.text = currentInput
            firstOperand = result
            isNewOperation = true
        }
    }


    private fun fetchSecondOperand(): Double? {
        val parts = currentInput.split(" ")
        return if (parts.size == 3 && parts[2].isNotEmpty()) {
            parts[2].toDoubleOrNull() // Convert the second operand to a Double if it exists
        } else {
            null // Return null if the second operand is not available
        }
    }

    private fun clear() {
        currentInput = ""
        firstOperand = 0.0
        secondOperand = 0.0
        lastoperation = ""
        isNewOperation = true
        binding.screen.text = ""
    }

    private fun appendNumber(number: String) {
        if (isNewOperation) {
            currentInput = number
            binding.screen.text = currentInput
            isNewOperation = false
        } else {
            currentInput += number
            binding.screen.text = currentInput
        }
    }
}
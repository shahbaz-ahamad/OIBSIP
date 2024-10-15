package com.shahbaz.unitconverter.util

import android.view.View
import com.shahbaz.unitconverter.R

object CalculatorUtils {
    fun setClickListenerForCalculatorButton(
        view: View,   // Generalize to any View instead of specific fragment binding
        appendNumber: (String) -> Unit,
        clearAll: () -> Unit,
        backButton: () -> Unit,
        calculateResult: () -> Unit
    ) {
        // Find buttons by their IDs in the provided view
        val acButton = view.findViewById<View>(R.id.acButton)
        val backButtonView = view.findViewById<View>(R.id.backbutton)
        val equalButton = view.findViewById<View>(R.id.equalButton)
        val zeroButton = view.findViewById<View>(R.id.zeroButton)
        val oneButton = view.findViewById<View>(R.id.oneButton)
        val twoButton = view.findViewById<View>(R.id.twoButton)
        val threeButton = view.findViewById<View>(R.id.threeButton)
        val fourButton = view.findViewById<View>(R.id.fourButton)
        val fiveButton = view.findViewById<View>(R.id.fiveButton)
        val sixButton = view.findViewById<View>(R.id.sixButton)
        val sevenButton = view.findViewById<View>(R.id.sevenButton)
        val eightButton = view.findViewById<View>(R.id.eightButton)
        val nineButton = view.findViewById<View>(R.id.nineButton)
        val doubleZeroButton = view.findViewById<View>(R.id.doubleZeroButton)

        // Set click listeners for each button
        acButton.setOnClickListener { clearAll() }
        backButtonView.setOnClickListener { backButton() }
        equalButton.setOnClickListener { calculateResult() }

        zeroButton.setOnClickListener { appendNumber("0") }
        oneButton.setOnClickListener { appendNumber("1") }
        twoButton.setOnClickListener { appendNumber("2") }
        threeButton.setOnClickListener { appendNumber("3") }
        fourButton.setOnClickListener { appendNumber("4") }
        fiveButton.setOnClickListener { appendNumber("5") }
        sixButton.setOnClickListener { appendNumber("6") }
        sevenButton.setOnClickListener { appendNumber("7") }
        eightButton.setOnClickListener { appendNumber("8") }
        nineButton.setOnClickListener { appendNumber("9") }
        doubleZeroButton.setOnClickListener { appendNumber("00") }
    }
}

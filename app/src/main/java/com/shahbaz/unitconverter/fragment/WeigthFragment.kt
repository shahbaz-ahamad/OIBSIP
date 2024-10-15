package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.databinding.FragmentWeigthBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant

class WeigthFragment : Fragment() {

    private lateinit var binding: FragmentWeigthBinding
    private var selectedInputUnit: String? = Constant.listOfWeightUnit.get(0)
    private var selectedOutputUnit: String? = Constant.listOfWeightUnit.get(1)
    private var currentInputValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeigthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvInput.text = selectedInputUnit
        binding.tvOutput.text = selectedOutputUnit

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.selectInputUnit.setOnClickListener {
            showUnitSelectionDialgooue { selectedUnit ->
                binding.tvInput.text = selectedUnit
                selectedInputUnit = selectedUnit
            }
        }
        binding.selectOutputUnit.setOnClickListener {
            showUnitSelectionDialgooue { selectedUnit ->
                binding.tvOutput.text = selectedUnit
                selectedOutputUnit = selectedUnit
            }
        }

        CalculatorUtils.setClickListenerForCalculatorButton(
            binding.root,
            appendNumber = { number ->
                appendNumber(number)
            },
            clearAll = {
                clearAll()
            },
            backButton = {
                backButton()
            },
            calculateResult = {
                calculateResult()
            })

    }
    private fun appendNumber(number: String) {
        currentInputValue += number
        binding.inputValue.text = currentInputValue
    }

    private fun backButton() {
        if (currentInputValue.isNotEmpty()) {
            currentInputValue = currentInputValue.substring(0, currentInputValue.length - 1)
            binding.inputValue.text = currentInputValue
        }
    }

    private fun clearAll() {
        currentInputValue = ""
        binding.inputValue.text = currentInputValue
        binding.outputValue.text = ""
    }

    private fun calculateResult() {
        if (currentInputValue.isNotEmpty()) {
            var inputValue = currentInputValue.toDouble()
            val inputUnit = selectedInputUnit
            val outputUnit = selectedOutputUnit
            if (inputUnit != null && outputUnit != null) {
                val result = convertWeight(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }
    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfWeightUnit)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }

    fun convertWeight(inputValue: Double, inputUnit: String, outputUnit: String): Double? {
        // Conversion factors to kilograms (kg)
        val conversionTable = mapOf(
            "kilogram (kg)" to 1.0,
            "gram (g)" to 0.001,
            "milligram (mg)" to 1e-6,
            "microgram (µg)" to 1e-9,
            "ounce (oz)" to 0.0283495,
            "pound (lb)" to 0.453592,
            "ton (t)" to 1000.0,
            "Carat (ct)" to 0.0002,
            "Quintal (qt)" to 100.0
        )

        // Convert input value to kilograms (kg)
        val inputInKilograms = inputValue * (conversionTable[inputUnit] ?: return null)

        // Convert from kilograms (kg) to the target output unit
        return inputInKilograms / (conversionTable[outputUnit] ?: return null)
    }

}
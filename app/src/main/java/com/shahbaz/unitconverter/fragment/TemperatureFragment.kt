package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.databinding.FragmentTemperatureBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant

class TemperatureFragment : Fragment() {

    private lateinit var binding: FragmentTemperatureBinding
    private var selectedInputUnit: String? = Constant.listOfTemperatureUnit.get(0)
    private var selectedOutputUnit: String? = Constant.listOfTemperatureUnit.get(1)
    private var currentInputValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTemperatureBinding.inflate(inflater, container, false)
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
                val result = convertTemperature(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }
    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfTemperatureUnit)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }

    fun convertTemperature(inputValue: Double, inputUnit: String, outputUnit: String): Double? {
        return when (inputUnit) {
            "Celsius (°C)" -> when (outputUnit) {
                "Celsius (°C)" -> inputValue
                "Fahrenheit (°F)" -> (inputValue * 9/5) + 32
                "Kelvin (K)" -> inputValue + 273.15
                else -> null
            }
            "Fahrenheit (°F)" -> when (outputUnit) {
                "Celsius (°C)" -> (inputValue - 32) * 5/9
                "Fahrenheit (°F)" -> inputValue
                "Kelvin (K)" -> (inputValue - 32) * 5/9 + 273.15
                else -> null
            }
            "Kelvin (K)" -> when (outputUnit) {
                "Celsius (°C)" -> inputValue - 273.15
                "Fahrenheit (°F)" -> (inputValue - 273.15) * 9/5 + 32
                "Kelvin (K)" -> inputValue
                else -> null
            }
            else -> null
        }
    }

}
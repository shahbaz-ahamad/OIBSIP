package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.databinding.FragmentPowerBinding
import com.shahbaz.unitconverter.databinding.FragmentPressureBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant

class PressureFragment : Fragment() {

    private lateinit var binding: FragmentPressureBinding
    private var selectedInputUnit: String? = Constant.listOfPressureUnit.get(0)
    private var selectedOutputUnit: String? = Constant.listOfPressureUnit.get(1)
    private var currentInputValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPressureBinding.inflate(inflater, container, false)
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
            }
        )
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
                val result = convertPressure(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }

    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfPressureUnit)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }

    fun convertPressure(inputValue: Double, inputUnit: String, outputUnit: String): Double? {
        // Conversion factors to pascals (Pa)
        val conversionTable = mapOf(
            "kilopascal (kPa)" to 1000.0,
            "pascal (Pa)" to 1.0,
            "atmosphere (atm)" to 101325.0,
            "bar (bar)" to 100000.0,
            "torr (torr)" to 133.322
        )

        // Convert input value to pascals (Pa)
        val inputInPascals = inputValue * (conversionTable[inputUnit] ?: return null)

        // Convert from pascals (Pa) to the target output unit
        return inputInPascals / (conversionTable[outputUnit] ?: return null)
    }

}
package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.databinding.FragmentLengthBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant


class LengthFragment : Fragment() {

    private lateinit var binding: FragmentLengthBinding
    private var selectedInputUnit: String? = Constant.listOfMetricUnits.get(0)
    private var selectedOutputUnit: String? = Constant.listOfMetricUnits.get(1)
    private var currentInputValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLengthBinding.inflate(inflater, container, false)
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
                val result = convertLength(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }

    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfMetricUnits)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }

    private fun convertLength(inputValue: Double, inputUnit: String, outputUnit: String): Double {
        // Conversion factors to meters (m)
        val conversionTable = mapOf(
            "kilometer (km)" to 1000.0,
            "meter (m)" to 1.0,
            "centimeter (cm)" to 0.01,
            "millimeter (mm)" to 0.001,
            "micrometer (µm)" to 1e-6,
            "nanometer (nm)" to 1e-9,
            "mile (mi)" to 1609.34,
            "yard (yd)" to 0.9144,
            "foot (ft)" to 0.3048,
            "inch (in)" to 0.0254,
            "nautical mile (nmi)" to 1852.0
        )
        Log.d("inputValue", "inputValue: $inputValue")
        Log.d("inputUnit", "inputUnit: $inputUnit")
        Log.d("outputUnit", "outputUnit: $outputUnit")

        // Convert input value to meters
        val inputInMeters = inputValue * (conversionTable[inputUnit] ?: 1.0)

        // Convert from meters to the target output unit
        return inputInMeters / (conversionTable[outputUnit] ?: 1.0)
    }

}
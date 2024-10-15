package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.databinding.FragmentSpeedBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant


class SpeedFragment : Fragment() {
    private lateinit var binding: FragmentSpeedBinding
    private var selectedInputUnit: String? = Constant.listOfSpeedUnit.get(0)
    private var selectedOutputUnit: String? = Constant.listOfSpeedUnit.get(1)
    private var currentInputValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSpeedBinding.inflate(inflater, container, false)
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
                val result = convertSpeed(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }
    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfSpeedUnit)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }

    fun convertSpeed(inputValue: Double, inputUnit: String, outputUnit: String): Double? {
        // Conversion factors to meters per second (m/s)
        val conversionTable = mapOf(
            "meter per second (m/s)" to 1.0,
            "kilometer per hour (km/h)" to 0.277778,
            "mile per hour (mph)" to 0.44704,
            "knot (kn)" to 0.514444,
            "speed of light (c)" to 299792458.0,
            "speed of sound (c)" to 343.0,
            "inch per second (in/s)" to 0.0254
        )

        // Convert input value to meters per second (m/s)
        val inputInMetersPerSecond = inputValue * (conversionTable[inputUnit] ?: return null)

        // Convert from meters per second (m/s) to the target output unit
        return inputInMetersPerSecond / (conversionTable[outputUnit] ?: return null)
    }

}
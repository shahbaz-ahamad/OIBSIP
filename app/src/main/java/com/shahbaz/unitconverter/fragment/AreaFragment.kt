package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.databinding.FragmentAreaBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant


class AreaFragment : Fragment() {

    private lateinit var binding: FragmentAreaBinding
    private var selectedInputUnit: String? = Constant.listOfAreaUnit.get(0)
    private var selectedOutputUnit: String? = Constant.listOfAreaUnit.get(1)
    private var currentInputValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAreaBinding.inflate(inflater, container, false)
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
                val result = convertArea(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }

    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfAreaUnit)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }
    fun convertArea(inputValue: Double, inputUnit: String, outputUnit: String): Double? {
        // Conversion factors to square meters (m²)
        val conversionTable = mapOf(
            "square kilometer (km²)" to 1e6,          // 1 km² = 1,000,000 m²
            "square meter (m²)" to 1.0,               // 1 m² = 1 m²
            "square centimeter (cm²)" to 1e-4,        // 1 cm² = 0.0001 m²
            "square millimeter (mm²)" to 1e-6,        // 1 mm² = 0.000001 m²
            "square micrometer (µm²)" to 1e-12,       // 1 µm² = 1e-12 m²
            "square nanometer (nm²)" to 1e-18,        // 1 nm² = 1e-18 m²
            "acre (ac)" to 4046.86,                   // 1 acre = 4046.86 m²
            "hectare (ha)" to 10000.0,                // 1 hectare = 10,000 m²
            "square yard (yd²)" to 0.836127,          // 1 yd² = 0.836127 m²
            "square foot (ft²)" to 0.092903,          // 1 ft² = 0.092903 m²
            "square inch (in²)" to 0.00064516         // 1 in² = 0.00064516 m²
        )

        // Convert input value to square meters (m²)
        val inputInSquareMeters = inputValue * (conversionTable[inputUnit] ?: return null)

        // Convert from square meters (m²) to the target output unit
        return inputInSquareMeters / (conversionTable[outputUnit] ?: return null)
    }

}
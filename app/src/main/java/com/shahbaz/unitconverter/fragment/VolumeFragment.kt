package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.databinding.FragmentVolumeBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant

class VolumeFragment : Fragment() {
    private lateinit var binding: FragmentVolumeBinding
    private var selectedInputUnit: String? = Constant.listOfVolumeUnit.get(0)
    private var selectedOutputUnit: String? = Constant.listOfVolumeUnit.get(1)
    private var currentInputValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVolumeBinding.inflate(inflater, container, false)
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
                val result = convertVolume(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }

    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfVolumeUnit)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }

    fun convertVolume(inputValue: Double, inputUnit: String, outputUnit: String): Double? {
        // Conversion factors to cubic meters (m³)
        val conversionTable = mapOf(
            "cubic kilometer (km³)" to 1e9,
            "cubic meter (m³)" to 1.0,
            "cubic centimeter (cm³)" to 1e-6,
            "cubic millimeter (mm³)" to 1e-9,
            "cubic micrometer (µm³)" to 1e-18,
            "cubic nanometer (nm³)" to 1e-27,
            "fluid ounce (fl oz)" to 0.0000295735,
            "gill (gal)" to 0.0001182941,
            "pint (pt)" to 0.000473176,
            "quart (qt)" to 0.000946353,
            "cubic inch (in³)" to 0.0000163871
        )

        // Convert input value to cubic meters (m³)
        val inputInCubicMeters = inputValue * (conversionTable[inputUnit] ?: return null)

        // Convert from cubic meters (m³) to the target output unit
        return inputInCubicMeters / (conversionTable[outputUnit] ?: return null)
    }

}
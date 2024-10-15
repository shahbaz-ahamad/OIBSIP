package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.databinding.FragmentPowerBinding
import com.shahbaz.unitconverter.util.CalculatorUtils
import com.shahbaz.unitconverter.util.Constant

class PowerFragment : Fragment() {

    private lateinit var binding: FragmentPowerBinding
    private var selectedInputUnit: String? = Constant.listOfPowerUnit.get(0)
    private var selectedOutputUnit: String? = Constant.listOfPowerUnit.get(1)
    private var currentInputValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPowerBinding.inflate(inflater, container, false)
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
                val result = convertPower(inputValue, inputUnit, outputUnit)
                binding.outputValue.text = result.toString()
            }
        }
    }

    fun showUnitSelectionDialgooue(onUnitSelected: (String) -> Unit) {
        val dialogFrament = DialgoueFragment(onUnitSelected, Constant.listOfPowerUnit)
        dialogFrament.show(childFragmentManager, "UnitSelectionDialog")
    }

    fun convertPower(inputValue: Double, inputUnit: String, outputUnit: String): Double? {
        // Conversion factors to watts (W)
        val conversionTable = mapOf(
            "watt (W)" to 1.0,
            "kilowatt (kW)" to 1000.0,
            "megawatt (MW)" to 1e6,
            "gigawatt (GW)" to 1e9,
            "horsepower (hp)" to 745.7
        )

        // Convert input value to watts (W)
        val inputInWatts = inputValue * (conversionTable[inputUnit] ?: return null)

        // Convert from watts (W) to the target output unit
        return inputInWatts / (conversionTable[outputUnit] ?: return null)
    }

}
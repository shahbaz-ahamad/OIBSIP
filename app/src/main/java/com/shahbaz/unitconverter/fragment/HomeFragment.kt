package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.adapter.ConverterItemAdapter
import com.shahbaz.unitconverter.databinding.FragmentHomeBinding
import com.shahbaz.unitconverter.datamodel.UnitConverterItem


class HomeFragment : Fragment() {

    private lateinit var binidng: FragmentHomeBinding
    private val converterItemAdapter by lazy { ConverterItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binidng = FragmentHomeBinding.inflate(inflater, container, false)
        return binidng.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        converterItemAdapter.onClick = { unitConverterItem ->
            when (unitConverterItem.name) {
                "Length" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_lengthFragment)
                }

                "Area" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_areaFragment)
                }

                "Volume" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_volumeFragment)
                }

                "Weight" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_weigthFragment)
                }

                "Temperature" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_temperatureFragment)
                }

                "Speed" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_speedFragment)
                }

                "Pressure" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_pressureFragment)
                }

                "Power" -> {
                    findNavController().navigate(R.id.action_homeFragment_to_powerFragment)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binidng.recyclerview.apply {
            adapter = converterItemAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            converterItemAdapter.differ.submitList(listOfItem)
        }
    }

    val listOfItem = listOf(
        UnitConverterItem(1, "Length", R.drawable.length),
        UnitConverterItem(2, "Area", R.drawable.area),
        UnitConverterItem(3, "Volume", R.drawable.volume),
        UnitConverterItem(4, "Weight", R.drawable.weight),
        UnitConverterItem(5, "Temperature", R.drawable.temperature),
        UnitConverterItem(6, "Speed", R.drawable.speed),
        UnitConverterItem(7, "Pressure", R.drawable.pressure),
        UnitConverterItem(8, "Power", R.drawable.power),
    )
}
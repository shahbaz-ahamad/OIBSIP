package com.shahbaz.unitconverter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.unitconverter.R
import com.shahbaz.unitconverter.adapter.UnitAdapter
import com.shahbaz.unitconverter.databinding.FragmentDialgoueBinding
import com.shahbaz.unitconverter.util.Constant

class DialgoueFragment(
    private val onItemClick: (String) -> Unit,
    private val listOfUnit: List<String>,
) : DialogFragment() {

    private lateinit var binding: FragmentDialgoueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialgoueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            //close the fragment
            dismiss()
        }

        setupRecyclerView()


    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = UnitAdapter(listOfUnit) { selectedUnit ->
                onItemClick(selectedUnit)
                dismiss()
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onStart() {
        super.onStart()
        // Make the dialog cover the whole screen
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

}
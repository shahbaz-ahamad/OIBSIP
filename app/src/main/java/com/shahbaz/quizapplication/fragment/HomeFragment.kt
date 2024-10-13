package com.shahbaz.quizapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.shahbaz.quizapplication.R
import com.shahbaz.quizapplication.adapter.CategoryAdapter
import com.shahbaz.quizapplication.databinding.FragmentHomeBinding
import com.shahbaz.quizapplication.util.Resources
import com.shahbaz.quizapplication.viewmodel.CatgegoryViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val categoryViewmodel by viewModels<CatgegoryViewmodel>()

    private val categoryAdapter by lazy {
        CategoryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerview()
        observeCatgegoryData()
        categoryAdapter.onClick = {
            val bundle = Bundle().apply {
                putString("category", it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_questionsFragment, bundle)
        }
    }

    private fun setupRecyclerview() {
        binding.recyclerView.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    private fun observeCatgegoryData() {
        lifecycleScope.launch {
            categoryViewmodel.categoryStatus.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("TAG", "observeCatgegoryData: ${it.data}")
                        categoryAdapter.differ.submitList(it.data)
                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }
}
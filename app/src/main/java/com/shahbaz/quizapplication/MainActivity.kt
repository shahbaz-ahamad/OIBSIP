package com.shahbaz.quizapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.shahbaz.quizapplication.util.Resources
import com.shahbaz.quizapplication.viewmodel.CatgegoryViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val categoryViewmodel by viewModels<CatgegoryViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeCatgegoryData()
    }

    private fun observeCatgegoryData() {
        lifecycleScope.launch {
            categoryViewmodel.categoryStatus.collect {
                when (it) {
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        Log.d("TAG", "observeCatgegoryData: ${it.data}")
                    }
                    is Resources.Error -> {}
                    else -> Unit
                }
            }
        }
    }
}
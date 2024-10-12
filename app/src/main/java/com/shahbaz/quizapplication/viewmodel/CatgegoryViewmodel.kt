package com.shahbaz.quizapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.quizapplication.repo.CategoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CatgegoryViewmodel @Inject constructor(
    private val categoryRepo: CategoryRepo,
) : ViewModel() {
    val categoryStatus = categoryRepo.categoryStatus

    init {
        getCategory()
    }

    private fun getCategory() {
        categoryRepo.getCategory()
    }
}
package com.shahbaz.quizapplication.repo

import com.shahbaz.quizapplication.datamodel.CategoryList
import com.shahbaz.quizapplication.datamodel.CategoryListItem
import com.shahbaz.quizapplication.retrofit.QuizApiService
import com.shahbaz.quizapplication.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepo(
    private val quizApiService: QuizApiService,
) {

    private val _catgoryStatus =
        MutableStateFlow<Resources<List<CategoryListItem>>>(Resources.Unspecified())
    val categoryStatus = _catgoryStatus.asStateFlow()

    fun getCategory() {
        _catgoryStatus.value = Resources.Loading()
        quizApiService.getAllCategory()
            .enqueue(object : Callback<CategoryList> {
                override fun onResponse(
                    call: Call<CategoryList>,
                    response: Response<CategoryList>,
                ) {
                    if (response.isSuccessful) {
                        _catgoryStatus.value = Resources.Success(response.body()!!)
                    } else {
                        _catgoryStatus.value = Resources.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                    _catgoryStatus.value = Resources.Error(t.message.toString())
                }
            })
    }
}
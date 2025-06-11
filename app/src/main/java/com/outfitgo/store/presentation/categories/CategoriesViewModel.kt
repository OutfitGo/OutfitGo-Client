package com.outfitgo.store.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.usecase.collections.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesState())
    val uiState = _uiState.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = getCategoriesUseCase.execute()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = categories
                    )
                }
            }catch (exception: Exception){
                //TODO Handle Error
            }
        }
    }
}
package com.poscodx.contract_ai_partner.ui.upload

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poscodx.contract_ai_partner.domain.model.Category
import com.poscodx.contract_ai_partner.domain.usecase.GetCategoriesUseCase
import com.poscodx.contract_ai_partner.domain.usecase.UploadAndAnalyzeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val uploadAndAnalyze: UploadAndAnalyzeUseCase
) : ViewModel() {

    /* ───── 카테고리 목록 로드 ───── */
    var categories by mutableStateOf<List<Category>>(emptyList())
        private set
    var isLoadingCategories by mutableStateOf(false)
        private set
    var categoryLoadError: Throwable? by mutableStateOf(null)
        private set

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            isLoadingCategories = true
            categoryLoadError = null
            runCatching { getCategories() }
                .onSuccess { categories = it }
                .onFailure { categoryLoadError = it }
            isLoadingCategories = false
        }
    }

    /* ───── 선택된 카테고리 ───── */
    var selectedCategory by mutableStateOf<Category?>(null)
        private set

    fun onCategorySelected(name: String) {
        selectedCategory = categories.firstOrNull { it.name == name }
    }

    /* ───── 이미지 파일 ───── */
    private var imageFile by mutableStateOf<File?>(null)
    fun onImageSelected(file: File) {
        imageFile = file
    }

    val hasImage: Boolean
        get() = imageFile != null

    /* ───── 업로드 진행 플래그 ───── */
    var isUploading by mutableStateOf(false)
        private set

    /* ───── 업로드 + AI 분석 ───── */
    fun upload(
        onSuccess: (Long) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val category = selectedCategory ?: return
        val file = imageFile ?: return

        viewModelScope.launch {
            isUploading = true
            uploadAndAnalyze(category.id, file)
                .onSuccess { id ->
                    onSuccess(id)
                    runCatching { file.delete() }
                }
                .onFailure(onError)
            isUploading = false
        }
    }
}

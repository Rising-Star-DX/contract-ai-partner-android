package com.poscodx.contract_ai_partner.feature.standardlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poscodx.contract_ai_partner.domain.model.Standard
import com.poscodx.contract_ai_partner.domain.usecase.GetStandardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StandardListViewModel @Inject constructor(
    private val getStandards: GetStandardsUseCase
) : ViewModel() {

    // ─── ContractListViewModel 과 동일 구조 ──────────────────────────────
    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        uiState = try {
            UiState.Success(getStandards())   // Domain 모델 그대로 반환
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }

    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: List<Standard>) : UiState
        data class Error(val throwable: Throwable) : UiState
    }
}
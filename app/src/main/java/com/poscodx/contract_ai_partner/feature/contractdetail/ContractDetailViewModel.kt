package com.poscodx.contract_ai_partner.feature.contractdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poscodx.contract_ai_partner.domain.model.ContractDetail
import com.poscodx.contract_ai_partner.domain.usecase.GetContractDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractDetailViewModel @Inject constructor(
    private val getDetail: GetContractDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set

    private val id: Long = checkNotNull(savedStateHandle["id"]).toString().toLong()

    init { load() }

    private fun load() = viewModelScope.launch {
        uiState = try {
            UiState.Success(getDetail(id))
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }

    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: ContractDetail) : UiState
        data class Error(val throwable: Throwable) : UiState
    }

}
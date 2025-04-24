package com.poscodx.contract_ai_partner.feature.contractlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poscodx.contract_ai_partner.domain.model.Contract
import com.poscodx.contract_ai_partner.domain.usecase.GetContractsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractListViewModel @Inject constructor(
    private val getContracts: GetContractsUseCase
) : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set

    init { load() }

    private fun load() = viewModelScope.launch {
        uiState = try {
            UiState.Success(getContracts())
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }

    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: List<Contract>) : UiState
        data class Error(val throwable: Throwable) : UiState
    }
}
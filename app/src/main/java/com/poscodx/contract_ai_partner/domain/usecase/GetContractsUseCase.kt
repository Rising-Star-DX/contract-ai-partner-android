package com.poscodx.contract_ai_partner.domain.usecase

import com.poscodx.contract_ai_partner.domain.repository.ContractRepository
import javax.inject.Inject

class GetContractsUseCase @Inject constructor(
    private val repository: ContractRepository
) {
    suspend operator fun invoke() = repository.fetchContracts()
}
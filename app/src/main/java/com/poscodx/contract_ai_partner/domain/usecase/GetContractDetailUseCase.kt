package com.poscodx.contract_ai_partner.domain.usecase

import com.poscodx.contract_ai_partner.domain.repository.ContractRepository
import javax.inject.Inject

class GetContractDetailUseCase @Inject constructor(
    private val repo: ContractRepository
) {
    suspend operator fun invoke(id: Long) = repo.fetchContractDetail(id)
}
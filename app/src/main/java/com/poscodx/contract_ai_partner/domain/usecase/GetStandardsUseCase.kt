package com.poscodx.contract_ai_partner.domain.usecase

import com.poscodx.contract_ai_partner.domain.repository.StandardRepository
import javax.inject.Inject

class GetStandardsUseCase @Inject constructor(
    private val repository: StandardRepository
) {
    suspend operator fun invoke() = repository.fetchStandards()
}
package com.poscodx.contract_ai_partner.domain.usecase

import com.poscodx.contract_ai_partner.domain.repository.StandardRepository
import javax.inject.Inject

class GetStandardDetailUseCase @Inject constructor(
    private val repository: StandardRepository
) {
    suspend operator fun invoke(id: Long) = repository.fetchDetail(id)
}
package com.poscodx.contract_ai_partner.domain.usecase

import com.poscodx.contract_ai_partner.domain.repository.ContractRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadAndAnalyzeUseCase @Inject constructor(
    private val contractRepo: ContractRepository
) {
    /** 업로드 → id 반환 → AI 분석 PATCH 호출 */
    suspend operator fun invoke(
        categoryId: Long,
        file: File
    ): Result<Long> = runCatching {
        contractRepo.uploadAndAnalyze(categoryId, file)
    }
}
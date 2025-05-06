package com.poscodx.contract_ai_partner.domain.repository

import com.poscodx.contract_ai_partner.domain.model.Contract
import com.poscodx.contract_ai_partner.domain.model.ContractDetail
import java.io.File

interface ContractRepository {
    suspend fun fetchContracts(): List<Contract>
    suspend fun fetchContractDetail(id: Long): ContractDetail
    suspend fun uploadAndAnalyze(
        categoryId: Long,
        file: File
    ): Long
}
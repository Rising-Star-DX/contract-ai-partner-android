package com.poscodx.contract_ai_partner.data.remote.api

import com.poscodx.contract_ai_partner.data.remote.dto.ContractDetailDto
import com.poscodx.contract_ai_partner.data.remote.dto.ContractDto
import com.poscodx.contract_ai_partner.data.remote.dto.UploadResponse
import com.poscodx.contract_ai_partner.data.remote.dto.UploadResultDto
import java.io.File

interface ContractApi {
    suspend fun getContractList(): List<ContractDto>
    suspend fun getContract(id: Long): ContractDetailDto
    suspend fun uploadContract(
        categoryId: Long,
        file: File
    ): UploadResultDto
    suspend fun requestAnalysis(id: Long)
}
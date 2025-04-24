package com.poscodx.contract_ai_partner.data.remote.api

import com.poscodx.contract_ai_partner.data.remote.dto.ContractDto

interface ContractApi {
    suspend fun getContractList(): List<ContractDto>
//    suspend fun getContract(id: Long): ContractDetailDto
//    suspend fun uploadContract(multipartData: ByteArray): UploadResponse
}
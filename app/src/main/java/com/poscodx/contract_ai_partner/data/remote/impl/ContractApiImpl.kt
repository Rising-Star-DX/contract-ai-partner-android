package com.poscodx.contract_ai_partner.data.remote.impl

import com.poscodx.contract_ai_partner.data.remote.api.ContractApi
import com.poscodx.contract_ai_partner.data.remote.dto.ApiResponse
import com.poscodx.contract_ai_partner.data.remote.dto.ContractDetailDto
import com.poscodx.contract_ai_partner.data.remote.dto.ContractDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class ContractApiImpl @Inject constructor(
    private val client: HttpClient
) : ContractApi {
    override suspend fun getContractList(): List<ContractDto> =
        client.get("/agreements/android").body<ApiResponse<List<ContractDto>>>().data

    override suspend fun getContract(id: Long): ContractDetailDto =
        client.get("/agreements/$id").body<ApiResponse<ContractDetailDto>>().data
//
//    override suspend fun uploadContract(multipartData: ByteArray): UploadResponse =
//        client.submitFormWithBinaryData(
//            url = "/agreements",
//            formData = formData {
//                append("file", multipartData, Headers.build {
//                    append(HttpHeaders.ContentDisposition, "filename=\"contract.pdf\"")
//                })
//            }
//        ).body()
}
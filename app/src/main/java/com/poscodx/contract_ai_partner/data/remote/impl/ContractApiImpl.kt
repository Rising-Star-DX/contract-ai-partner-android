package com.poscodx.contract_ai_partner.data.remote.impl

import com.poscodx.contract_ai_partner.data.remote.api.ContractApi
import com.poscodx.contract_ai_partner.data.remote.dto.ApiResponse
import com.poscodx.contract_ai_partner.data.remote.dto.ContractDetailDto
import com.poscodx.contract_ai_partner.data.remote.dto.ContractDto
import com.poscodx.contract_ai_partner.data.remote.dto.UploadResponse
import com.poscodx.contract_ai_partner.data.remote.dto.UploadResultDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import java.io.File
import javax.inject.Inject

class ContractApiImpl @Inject constructor(
    private val client: HttpClient
) : ContractApi {
    override suspend fun getContractList(): List<ContractDto> =
        client.get("/agreements/android").body<ApiResponse<List<ContractDto>>>().data

    override suspend fun getContract(id: Long): ContractDetailDto =
        client.get("/agreements/$id").body<ApiResponse<ContractDetailDto>>().data

    @OptIn(InternalAPI::class)
    override suspend fun uploadContract(
        categoryId: Long,
        file: File
    ): UploadResultDto {

        val partHeaders = Headers.build {
            append(
                HttpHeaders.ContentDisposition,
                """form-data; name="file"; filename="${file.name}""""
            )
            append(HttpHeaders.ContentType, "application/octet-stream")
        }

        val multipart = MultiPartFormDataContent(
            formData {
                append(
                    key     = "file",                        // ← OkHttp와 동일
                    value   = file.readBytes(),
                    headers = partHeaders
                )
            }
        )

        return client.post("/agreements/upload/$categoryId") {
            setBody(multipart)
        }.body<UploadResponse>().data
    }

    override suspend fun requestAnalysis(id: Long) {
        client.patch("/agreements/analysis") {
            url { parameters.append("id", id.toString()) }
        }
    }
}
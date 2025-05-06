package com.poscodx.contract_ai_partner.data.remote.impl

import com.poscodx.contract_ai_partner.data.remote.api.ContractApi
import com.poscodx.contract_ai_partner.data.remote.dto.ApiResponse
import com.poscodx.contract_ai_partner.data.remote.dto.ContractDetailDto
import com.poscodx.contract_ai_partner.data.remote.dto.ContractDto
import com.poscodx.contract_ai_partner.data.remote.dto.UploadResultDto
import com.poscodx.contract_ai_partner.data.remote.dto.requireData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject

class ContractApiImpl @Inject constructor(
    private val client: HttpClient
) : ContractApi {
    override suspend fun getContractList(): List<ContractDto> =
        client.get("/agreements/android").body<ApiResponse<List<ContractDto>>>().requireData()

    override suspend fun getContract(id: Long): ContractDetailDto =
        client.get("/agreements/$id").body<ApiResponse<ContractDetailDto>>().requireData()

    override suspend fun uploadContract(
        categoryId: Long,
        file: File
    ): UploadResultDto {

        val ext = file.extension
        require(ext.isNotBlank()) { "파일 확장자를 확인할 수 없습니다." }
        val upperName = file.nameWithoutExtension + "." + ext.uppercase()

        val partHeaders = Headers.build {
            append(
                HttpHeaders.ContentDisposition,
                """filename="$upperName""""
            )
            append(HttpHeaders.ContentType, guessContentType(ext))
        }

        val multipart = MultiPartFormDataContent(
            formData {
                append(
                    key = "file",                        // ← OkHttp와 동일
                    value = file.readBytes(),
                    headers = partHeaders
                )
            }
        )

        val apiRes: ApiResponse<UploadResultDto> = client.post("/agreements/upload/$categoryId") {
            setBody(multipart)
            expectSuccess = false
        }.body()

        return apiRes.data ?: throw IllegalStateException(
            apiRes.message ?: "Upload failed – empty data"
        )
    }

    override suspend fun requestAnalysis(id: Long) {
        client.patch("/agreements/analysis/$id")
    }
}

fun guessContentType(ext: String): String = when (ext.lowercase()) {
    "jpg", "jpeg" -> "image/jpeg"
    "png" -> "image/png"
    "pdf" -> "application/pdf"
    else -> "application/octet-stream"
}
package com.poscodx.contract_ai_partner.data.repository

import com.poscodx.contract_ai_partner.data.mapper.toDomain
import com.poscodx.contract_ai_partner.data.remote.api.CategoryApi
import com.poscodx.contract_ai_partner.data.remote.api.ContractApi
import com.poscodx.contract_ai_partner.domain.model.Contract
import com.poscodx.contract_ai_partner.domain.model.ContractDetail
import com.poscodx.contract_ai_partner.domain.repository.ContractRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ContractRepositoryImpl @Inject constructor(
    private val api: ContractApi
) : ContractRepository {
    override suspend fun fetchContracts(): List<Contract> =
        api.getContractList().map { it.toDomain() }

    override suspend fun fetchContractDetail(id: Long): ContractDetail =
        api.getContract(id).toDomain()

    override suspend fun uploadAndAnalyze(
        categoryId: Long,
        file: File
    ): Long {

        // 1) 업로드 → PK(id) 획득
        val id = api.uploadContract(categoryId, file).id

        // 2) AI 분석 PATCH 호출
        api.requestAnalysis(id)

        return id
    }
}
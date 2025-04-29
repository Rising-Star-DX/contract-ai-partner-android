package com.poscodx.contract_ai_partner.data.repository

import com.poscodx.contract_ai_partner.data.mapper.toDomain
import com.poscodx.contract_ai_partner.data.remote.api.ContractApi
import com.poscodx.contract_ai_partner.domain.model.Contract
import com.poscodx.contract_ai_partner.domain.model.ContractDetail
import com.poscodx.contract_ai_partner.domain.repository.ContractRepository
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

}
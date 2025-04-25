package com.poscodx.contract_ai_partner.data.repository

import com.poscodx.contract_ai_partner.data.mapper.toDomain
import com.poscodx.contract_ai_partner.data.remote.api.StandardApi
import com.poscodx.contract_ai_partner.domain.repository.StandardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandardRepositoryImpl @Inject constructor(
    private val api: StandardApi
) : StandardRepository {
    // 기준 문서 전체 조회
    override suspend fun fetchStandards() =
        api.getStandardList().data.map { it.toDomain() }

    // 기준 문서 단일 조회
    override suspend fun fetchDetail(id: Long) =
        api.getStandardDetail(id).data.toDomain()
}
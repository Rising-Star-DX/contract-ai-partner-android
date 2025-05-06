package com.poscodx.contract_ai_partner.di

import com.poscodx.contract_ai_partner.data.remote.api.CategoryApi
import com.poscodx.contract_ai_partner.data.remote.api.ContractApi
import com.poscodx.contract_ai_partner.data.remote.api.StandardApi
import com.poscodx.contract_ai_partner.data.remote.impl.CategoryApiImpl
import com.poscodx.contract_ai_partner.data.remote.impl.ContractApiImpl
import com.poscodx.contract_ai_partner.data.remote.impl.StandardApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    // 계약서 api
    @Binds
    @Singleton
    abstract fun bindContractApi(
        impl: ContractApiImpl
    ): ContractApi

    // 기준 문서 api
    @Binds
    @Singleton
    abstract fun bindStandardApi(
        impl: StandardApiImpl
    ): StandardApi

    // 카테고리 api
    @Binds
    @Singleton
    abstract fun bindCategoryApi(
        impl: CategoryApiImpl
    ): CategoryApi
}
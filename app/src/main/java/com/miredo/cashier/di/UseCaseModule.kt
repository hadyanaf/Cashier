package com.miredo.cashier.di

import com.miredo.cashier.domain.repository.Repository
import com.miredo.cashier.domain.usecase.DeleteSaleUseCase
import com.miredo.cashier.domain.usecase.GetAttendanceReportsUseCase
import com.miredo.cashier.domain.usecase.GetSalesUseCase
import com.miredo.cashier.domain.usecase.GetSingleSaleUseCase
import com.miredo.cashier.domain.usecase.InsertAttendanceTaskUseCase
import com.miredo.cashier.domain.usecase.InsertSaleUseCase
import com.miredo.cashier.domain.usecase.UpdateSaleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetAttendanceReportsUseCase(repository: Repository): GetAttendanceReportsUseCase =
        GetAttendanceReportsUseCase(repository)

    @Provides
    @Singleton
    fun provideInsertAttendanceTaskUseCase(repository: Repository): InsertAttendanceTaskUseCase =
        InsertAttendanceTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSalesUseCase(repository: Repository): GetSalesUseCase =
        GetSalesUseCase(repository)

    @Provides
    @Singleton
    fun provideInsertSaleUseCase(repository: Repository): InsertSaleUseCase =
        InsertSaleUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSingleSaleUseCase(repository: Repository): GetSingleSaleUseCase =
        GetSingleSaleUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateSaleUseCase(repository: Repository): UpdateSaleUseCase =
        UpdateSaleUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteSaleUseCase(repository: Repository): DeleteSaleUseCase =
        DeleteSaleUseCase(repository)

}
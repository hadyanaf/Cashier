package com.miredo.cashier.di

import com.miredo.cashier.domain.repository.Repository
import com.miredo.cashier.domain.usecase.GetAttendanceReportsUseCase
import com.miredo.cashier.domain.usecase.GetSalesUseCase
import com.miredo.cashier.domain.usecase.InsertAttendanceTaskUseCase
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

}
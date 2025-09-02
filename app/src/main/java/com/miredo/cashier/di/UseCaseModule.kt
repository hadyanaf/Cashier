package com.miredo.cashier.di

import com.miredo.cashier.domain.repository.AuthRepository
import com.miredo.cashier.domain.repository.Repository
import com.miredo.cashier.domain.usecase.DeleteSaleUseCase
import com.miredo.cashier.domain.usecase.GetAttendanceReportsUseCase
import com.miredo.cashier.domain.usecase.GetAuthStateUseCase
import com.miredo.cashier.domain.usecase.GetSalesUseCase
import com.miredo.cashier.domain.usecase.GetSingleSaleUseCase
import com.miredo.cashier.domain.usecase.InsertAttendanceTaskUseCase
import com.miredo.cashier.domain.usecase.InsertSaleUseCase
import com.miredo.cashier.domain.usecase.SendPasswordResetEmailUseCase
import com.miredo.cashier.domain.usecase.SignInUseCase
import com.miredo.cashier.domain.usecase.SignOutUseCase
import com.miredo.cashier.domain.usecase.SignUpUseCase
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

    // Authentication Use Cases
    @Provides
    @Singleton
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase =
        SignInUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase =
        SignUpUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignOutUseCase(authRepository: AuthRepository): SignOutUseCase =
        SignOutUseCase(authRepository)

    @Provides
    @Singleton
    fun provideGetAuthStateUseCase(authRepository: AuthRepository): GetAuthStateUseCase =
        GetAuthStateUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSendPasswordResetEmailUseCase(authRepository: AuthRepository): SendPasswordResetEmailUseCase =
        SendPasswordResetEmailUseCase(authRepository)
}
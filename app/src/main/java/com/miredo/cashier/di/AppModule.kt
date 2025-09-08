package com.miredo.cashier.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miredo.cashier.data.repository.AuthRepositoryImpl
import com.miredo.cashier.data.repository.RepositoryImpl
import com.miredo.cashier.data.source.remote.RemoteDataSource
import com.miredo.cashier.data.source.remote.RemoteDataSourceImpl
import com.miredo.cashier.domain.repository.AuthRepository
import com.miredo.cashier.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): RemoteDataSource = RemoteDataSourceImpl(firestore, auth)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository =
        RepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(firebaseAuth)
}
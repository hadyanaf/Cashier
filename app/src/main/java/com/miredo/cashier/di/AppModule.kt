package com.miredo.cashier.di

import com.google.firebase.firestore.FirebaseFirestore
import com.miredo.cashier.data.repository.RepositoryImpl
import com.miredo.cashier.data.source.remote.RemoteDataSource
import com.miredo.cashier.data.source.remote.RemoteDataSourceImpl
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
    fun provideRemoteDataSource(firestore: FirebaseFirestore): RemoteDataSource =
        RemoteDataSourceImpl(firestore)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository =
        RepositoryImpl(remoteDataSource)
}
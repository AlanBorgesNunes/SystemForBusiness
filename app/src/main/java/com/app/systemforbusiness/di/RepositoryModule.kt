package com.app.systemforbusiness.di

import android.content.SharedPreferences
import com.app.systemforbusiness.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAutghRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImp(auth,database,appPreferences,gson)
    }

    @Provides
    @Singleton
    fun provideAddClientRepository(
        auth: FirebaseAuth,
        database: FirebaseFirestore
    ): AddClientRepository{
        return AddClientRepositoryImp(auth, database)
    }

    @Provides
    @Singleton
    fun provideWorkers(
        database: FirebaseFirestore
    ): AddWorkerRepository{
        return AddWorkerRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideCaixa(
        database: FirebaseFirestore
    ): CaixaRepository{
        return CaixaRepositoryImp(database)
    }

}
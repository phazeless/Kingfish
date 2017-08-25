package com.directdev.portal.di

import com.directdev.portal.network.BimayApi
import com.directdev.portal.network.BimayService
import com.directdev.portal.network.NetworkHelper
import com.directdev.portal.utils.NullConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
@Module
class NetworkModule {
    @Provides
    fun provideInterceptor() : Interceptor = StethoInterceptor()

    @Provides
    fun provideOkHttpClient(interceptor: Interceptor) : OkHttpClient =
            OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addNetworkInterceptor(interceptor)
                    .followRedirects(false)
                    .build()

    @Provides
    fun provideBimayService(client: OkHttpClient) : BimayService =
            Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(NullConverterFactory())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(client)
                    .baseUrl("https://binusmaya.binus.ac.id/services/ci/index.php/")
                    .build().create(BimayService::class.java)

    @Provides
    fun provideNetworkHelper(bimayApi: BimayApi) : NetworkHelper = bimayApi
}
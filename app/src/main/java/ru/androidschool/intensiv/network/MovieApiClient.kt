package ru.androidschool.intensiv.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.androidschool.intensiv.BuildConfig

object MovieApiClient {
    @JvmStatic
    private var client: OkHttpClient = if (BuildConfig.DEBUG) {
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }).build()
     } else {
        OkHttpClient.Builder().build()
    }

    val apiClient: MovieApiInterface by lazy {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return@lazy retrofit.create((MovieApiInterface::class.java))
    }
}

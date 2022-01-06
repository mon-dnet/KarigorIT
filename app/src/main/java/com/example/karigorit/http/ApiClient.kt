package com.example.karigorit.http

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.internal.http2.Http2Reader.Companion.logger
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object {

        var BASE_URL = "https://randomuser.me/api/"

        var retrofit: Retrofit? = null
        var okHttpClient: OkHttpClient? = null

        private fun buildClient(): OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build()
            return okHttpClient as OkHttpClient
        }

        // retrofit singeltone
        fun getClient(): Retrofit {
            if (retrofit == null) {
                val gson = GsonBuilder()
                    .serializeNulls()
                    .setLenient()
                    .create()


                retrofit = Retrofit.Builder()
                    .client(buildClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BASE_URL)
                    .build()
            }


            return retrofit!!
        }
    }
}


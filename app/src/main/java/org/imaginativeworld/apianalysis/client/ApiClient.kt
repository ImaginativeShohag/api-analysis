package org.imaginativeworld.apianalysis.client

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.imaginativeworld.apianalysis.ApiInterface
import org.imaginativeworld.apianalysis.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {

        @Volatile
        private var retrofit: Retrofit? = null

        @Volatile
        private var apiInterface: ApiInterface? = null

        private fun buildClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        }

        @Synchronized
        private fun getRetrofit(): Retrofit {
            return retrofit ?: synchronized(this) {
                retrofit ?: Retrofit.Builder()
                    .client(buildClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constant.SERVER_ENDPOINT + "/")
                    .build()
            }
        }

        @Synchronized
        fun getClient(): ApiInterface {

            return apiInterface ?: synchronized(this) {

                getRetrofit().create(ApiInterface::class.java)

            }

        }

    }
}
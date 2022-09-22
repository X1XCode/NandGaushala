package com.gaushala.webservices

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebApiClient constructor()//just an empty constructor for now
{
    private var webApi: WebApi? = null
    private var token = ""


    fun webApi_without(): WebApi? {

        System.setProperty("http.keepAlive", "false")

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val interceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }


        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(interceptor)
            .readTimeout(90000, TimeUnit.MILLISECONDS)
            .connectTimeout(90000, TimeUnit.MILLISECONDS)
            .build()

        client.connectTimeoutMillis

        val retrofit = Retrofit.Builder()
            .baseUrl(WebApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit_new = retrofit

        webApi = retrofit.create(WebApi::class.java)

        return webApi

    }

    fun webApi_with_header(token: String = ""): WebApi? {

        System.setProperty("http.keepAlive", "false")

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val interceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader(
                    "Authorization",
                    "Bearer $token"
                )
                .build()
            chain.proceed(newRequest)
        }


        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(interceptor)
            .readTimeout(90000, TimeUnit.MILLISECONDS)
            .connectTimeout(90000, TimeUnit.MILLISECONDS)
            .build()

        client.connectTimeoutMillis

        val retrofit = Retrofit.Builder()
            .baseUrl(WebApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit_new = retrofit

        webApi = retrofit.create(WebApi::class.java)

        return webApi

    }


    val webApi_without_new: WebApi?
        get() {

            System.setProperty("http.keepAlive", "false")

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val interceptor = Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .build()
                chain.proceed(newRequest)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(interceptor)
                .readTimeout(90000, TimeUnit.MILLISECONDS)
                .connectTimeout(90000, TimeUnit.MILLISECONDS)
                .build()

            client.connectTimeoutMillis

            val retrofit = Retrofit.Builder()
                .baseUrl(WebApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            retrofit_new = retrofit

            webApi = retrofit.create(WebApi::class.java)

            return webApi

        }


    val webApi_without_header: WebApi?
        get() {

            System.setProperty("http.keepAlive", "false")

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val interceptor = Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .build()
                chain.proceed(newRequest)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(interceptor)
                .readTimeout(90000, TimeUnit.MILLISECONDS)
                .connectTimeout(90000, TimeUnit.MILLISECONDS)
                .build()

            client.connectTimeoutMillis

            val retrofit = Retrofit.Builder()
                .baseUrl(WebApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            retrofit_new = retrofit

            webApi = retrofit.create(WebApi::class.java)

            return webApi

        }

    val webApi_with_MultiPart: WebApi?
        get() {

            try {
//                this.token = Methods.getLoginUser().getToken()

                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY

                val httpClient = OkHttpClient.Builder()

                val interceptor = Interceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .removeHeader("token")
                        .addHeader("Content-Type", "multipart/form-data")
                        .build()

                    chain.proceed(newRequest)
                }
                httpClient.addInterceptor(interceptor)
                httpClient.addInterceptor(logging)
                httpClient.readTimeout(90000, TimeUnit.MILLISECONDS)
                httpClient.connectTimeout(90000, TimeUnit.MILLISECONDS)

                val client = httpClient.build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(WebApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

                retrofit_new = retrofit

                webApi = retrofit.create(WebApi::class.java)
                return webApi

            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }


    companion object {
        private var webApiClient: WebApiClient? = null
        private var mcontext: Context? = null
        var retrofit_new: Retrofit? = null


        fun getInstance(context: Context): WebApiClient {
            if (webApiClient == null)
                webApiClient = WebApiClient()
            mcontext = context
            return webApiClient as WebApiClient
        }
    }

}

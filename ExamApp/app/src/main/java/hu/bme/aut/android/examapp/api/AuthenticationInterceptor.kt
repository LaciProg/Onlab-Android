package hu.bme.aut.android.examapp.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(private val authToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request().newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build())
}
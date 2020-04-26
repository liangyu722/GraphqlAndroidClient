package com.liang.android.clientgqlapp.util

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object ApolloClient {
    private const val BASE_URL = "https://ly-graphql.herokuapp.com/graphql"
    val instance: ApolloClient = ApolloClient.builder()
        .serverUrl(BASE_URL)
        .okHttpClient(OkHttpClient().newBuilder().build())
        .build()
}
package com.leoleo.kmmgithubsearch.data.api;

import com.leoleo.kmmgithubsearch.domain.exception.ApiErrorType
import io.ktor.client.call.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.http.*

internal class KtorHandler {
    /**
     * 共通のAPI エラーハンドリングはここで行う.
     */
    @Throws(ApiErrorType::class)
    fun handleResponseException(e: Throwable) {
        when (e) {
            is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException -> {
                throw ApiErrorType.Network
            }
            // ktor: 300番台のエラー
            is RedirectResponseException -> throw ApiErrorType.Redirect
            // ktor: 500番台のエラー
            is ServerResponseException -> throw ApiErrorType.Server
            // ktor: それ以外のエラー
            is ResponseException -> throw ApiErrorType.Unknown(e.message)
            else -> throw ApiErrorType.Unknown(e.message)
        }
    }
}
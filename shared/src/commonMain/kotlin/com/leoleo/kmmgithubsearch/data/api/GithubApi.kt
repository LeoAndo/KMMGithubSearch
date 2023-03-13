package com.leoleo.kmmgithubsearch.data.api

import com.leoleo.kmmgithubsearch.data.api.github.response.GithubErrorResponse
import com.leoleo.kmmgithubsearch.data.api.github.response.RepositoryDetailResponse
import com.leoleo.kmmgithubsearch.data.api.github.response.SearchRepositoryResponse
import com.leoleo.kmmgithubsearch.domain.exception.ApiErrorType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

internal class GithubApi(private val format: Json, private val ktorHandler: KtorHandler) {
    private val httpClient: HttpClient by lazy {
        HttpClient {
            defaultRequest {
                // TODO
                url.takeFrom(URLBuilder().takeFrom("https://api.github.com").apply {
                    encodedPath += url.encodedPath
                })
                header("Accept", "application/vnd.github.v3+json")
                // TODO header("Authorization", "Bearer !GITHUB_ACCESS_TOKEN!")
                header("X-GitHub-Api-Version", "2022-11-28")
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TIMEOUT_MILLIS
                connectTimeoutMillis = TIMEOUT_MILLIS
                socketTimeoutMillis = TIMEOUT_MILLIS
            }
            install(Logging) {
                logger = AppHttpLogger()
                level = LogLevel.BODY
            }
            expectSuccess = true // HttpResponseValidatorで必要な設定.
            HttpResponseValidator {
                handleResponseExceptionWithRequest { e, _ ->
                    when (e) {
                        is ClientRequestException -> { // ktor: 400番台のエラー
                            val errorResponse = e.response
                            val message =
                                format.decodeFromString<GithubErrorResponse>(errorResponse.body()).message
                            when (errorResponse.status) {
                                HttpStatusCode.Unauthorized -> throw ApiErrorType.UnAuthorized(
                                    message
                                )
                                HttpStatusCode.NotFound -> throw ApiErrorType.NotFound(message)
                                HttpStatusCode.Forbidden -> throw ApiErrorType.Forbidden(message)
                                HttpStatusCode.UnprocessableEntity -> {
                                    throw ApiErrorType.UnprocessableEntity(message)
                                }
                                else -> throw ApiErrorType.Unknown(message)
                            }
                        }
                        else -> ktorHandler.handleResponseException(e)
                    }
                }
            }
        }
    }

    @Throws(ApiErrorType::class, CancellationException::class)
    suspend fun searchRepositories(
        query: String,
        page: Int,
        perPage: Int = SEARCH_PER_PAGE,
        sort: String = "stars"
    ): SearchRepositoryResponse {
        /*
            // サーバーサイドのAPI開発が完了するまではFlavorをstubにし、開発を進める.
            return format.decodeFromStubData<SearchRepositoryResponse>(
                context,
                format,
                "search_repositories_success.json"
            )
         */
        val response: HttpResponse = httpClient.get {
            url { path("search", "repositories") }
            parameter("q", query)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
        }
        return format.decodeFromString(response.body())
    }

    @Throws(ApiErrorType::class, CancellationException::class)
    suspend fun fetchRepositoryDetail(
        ownerName: String,
        repositoryName: String
    ): RepositoryDetailResponse {
        val response: HttpResponse = httpClient.get {
            url {
                path("repos", ownerName, repositoryName)
            }
        }
        return format.decodeFromString(response.body())
    }

    companion object {
        const val SEARCH_PER_PAGE = 20
        private const val TIMEOUT_MILLIS: Long = 30 * 1000
    }
}
package com.leoleo.kmmgithubsearch.domain.repository

import com.leoleo.kmmgithubsearch.domain.exception.ApiErrorType
import com.leoleo.kmmgithubsearch.domain.model.RepositoryDetail
import com.leoleo.kmmgithubsearch.domain.model.RepositorySummary
import kotlin.coroutines.cancellation.CancellationException

/**
 * Exceptionをスローする場合は必ず@Throwsをつける必要がある
 * つけないと、iOS側でエラーハンドリングができなかった.
 */
interface GithubRepoRepository {
    @Throws(ApiErrorType::class, CancellationException::class)
    suspend fun searchRepositories(query: String, page: Int): List<RepositorySummary>

    @Throws(ApiErrorType::class, CancellationException::class)
    suspend fun getRepositoryDetail(
        ownerName: String,
        repositoryName: String
    ): RepositoryDetail
}
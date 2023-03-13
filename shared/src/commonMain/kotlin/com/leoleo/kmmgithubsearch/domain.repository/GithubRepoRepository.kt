package com.leoleo.kmmgithubsearch.domain.repository

import com.leoleo.kmmgithubsearch.domain.exception.model.RepositoryDetail
import com.leoleo.kmmgithubsearch.domain.exception.model.RepositorySummary

interface GithubRepoRepository {
    suspend fun searchRepositories(query: String): List<RepositorySummary>
    suspend fun getRepositoryDetail(ownerName: String, repositoryName: String): RepositoryDetail
}
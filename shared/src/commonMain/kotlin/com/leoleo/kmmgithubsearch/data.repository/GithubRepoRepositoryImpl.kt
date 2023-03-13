package com.leoleo.kmmgithubsearch.data.repository

import com.leoleo.kmmgithubsearch.data.api.github.GithubApi
import com.leoleo.kmmgithubsearch.data.api.github.response.toModel
import com.leoleo.kmmgithubsearch.data.api.github.response.toModels
import com.leoleo.kmmgithubsearch.domain.exception.model.RepositoryDetail
import com.leoleo.kmmgithubsearch.domain.exception.model.RepositorySummary
import com.leoleo.kmmgithubsearch.domain.repository.GithubRepoRepository

internal class GithubRepoRepositoryImpl constructor(
    private val api: GithubApi
) : GithubRepoRepository {
    override suspend fun searchRepositories(query: String, page: Int): List<RepositorySummary> =
        api.searchRepositories(query, page).toModels()

    override suspend fun getRepositoryDetail(
        ownerName: String,
        repositoryName: String
    ): RepositoryDetail = api.fetchRepositoryDetail(ownerName, repositoryName).toModel()
}
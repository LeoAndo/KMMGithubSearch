package com.leoleo.kmmgithubsearch.domain.exception.model

data class RepositoryDetail(
    val name: String,
    val ownerAvatarUrl: String,
    val stargazersCount: String,
    val forksCount: String,
    val openIssuesCount: String,
    val watchersCount: String,
    val language: String?,
)
package com.leoleo.kmmgithubsearch.android.ui.search

import com.leoleo.kmmgithubsearch.domain.exception.ApplicationError
import com.leoleo.kmmgithubsearch.domain.model.RepositorySummary

data class UiState(
    val isFirstFetched: Boolean = false,
    val repositories: List<RepositorySummary> = listOf(),
    val nextPageNo: Int? = 1,
    val isLoading: Boolean = false,
    val applicationError: ApplicationError? = null
)
package com.leoleo.kmmgithubsearch.android.ui.detail

import com.leoleo.kmmgithubsearch.domain.exception.model.RepositoryDetail

sealed interface UiState {
    object Initial : UiState
    object Loading : UiState
    data class Data(val repositoryDetail: RepositoryDetail) : UiState
    data class Error(val throwable: Throwable) : UiState
}
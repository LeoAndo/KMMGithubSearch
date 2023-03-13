package com.leoleo.kmmgithubsearch.android.ui.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoleo.kmmgithubsearch.data.api.github.SEARCH_PER_PAGE
import com.leoleo.kmmgithubsearch.di.Container
import com.leoleo.kmmgithubsearch.domain.exception.ApplicationError
import com.leoleo.kmmgithubsearch.domain.exception.ValidationErrorType
import com.leoleo.kmmgithubsearch.domain.model.RepositorySummary
import com.leoleo.kmmgithubsearch.domain.repository.GithubRepoRepository
import kotlinx.coroutines.*

class SearchViewModel constructor(
    private val repository: GithubRepoRepository = Container.githubRepository,
    private val workerDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState())
        private set

    init {
        Log.e("TAG", "ando call init")
    }

    fun searchRepositories(query: String, page: Int = 1, refresh: Boolean = false) {
        viewModelScope.launch(workerDispatcher) {
            val repositories = if (refresh) listOf() else uiState.repositories
            try {
                if (query.isEmpty()) throw ValidationErrorType.Empty("please input search word.")
                stepToLoadingState(repositories)
                val newItems = repository.searchRepositories(query = query, page = page)
                val isLastPage = newItems.size < SEARCH_PER_PAGE
                stepToData(newItems, isLastPage, page)
            } catch (e: Throwable) {
                Log.e("TAG", "ando error: $e")
                if (e is ApplicationError) stepToError(repositories, e)
            }
        }
    }

    private fun stepToLoadingState(repositories: List<RepositorySummary>) {
        uiState =
            UiState(repositories = repositories, isLoading = true, applicationError = null)
    }

    private fun stepToData(newItems: List<RepositorySummary>, isLastPage: Boolean, page: Int) {
        if (isLastPage) {
            uiState = UiState(
                isFirstFetched = true,
                repositories = uiState.repositories + newItems,
                nextPageNo = null,
                isLoading = false,
                applicationError = null
            )
        } else {
            val nextPageNo = page + 1
            uiState = UiState(
                isFirstFetched = true,
                repositories = uiState.repositories + newItems,
                nextPageNo = nextPageNo,
                isLoading = false,
                applicationError = null
            )
        }
    }

    private fun stepToError(repositories: List<RepositorySummary>, e: ApplicationError) {
        uiState = UiState(repositories = repositories, isLoading = false, applicationError = e)
    }
}
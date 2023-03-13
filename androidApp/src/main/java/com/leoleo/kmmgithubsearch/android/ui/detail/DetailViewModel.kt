package com.leoleo.kmmgithubsearch.android.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoleo.kmmgithubsearch.di.Container
import com.leoleo.kmmgithubsearch.domain.repository.GithubRepoRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class DetailViewModel constructor(
    private val repository: GithubRepoRepository = Container.githubRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState>(UiState.Initial)
        private set
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        uiState = UiState.Error(throwable)
    }

    fun getRepositoryDetail(ownerName: String, repositoryName: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            uiState = UiState.Loading
            uiState = UiState.Data(repository.getRepositoryDetail(ownerName, repositoryName))
        }
    }
}
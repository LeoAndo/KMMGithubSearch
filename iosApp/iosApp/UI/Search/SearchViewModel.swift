//
//  SearchViewModel.swift
//  iosApp
//
//  Created by LeoAndo on 2023/03/13.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
final class SearchViewModel : ObservableObject {
    @Published var uiState: SearchUiState = SearchUiState()
    private let repository = Container().githubRepository
    
    func searchRepositories(query: String, page: Int32 = 1, refresh: Bool = false) {
        var repositories = uiState.repositories
        if refresh {
            repositories = []
        }
        
        if query.isEmpty {
            // throw ValidationErrorType.Empty(message: "please input search word.")
            self.stepToError(repositories, "please input search word.")
            return
        }
        stepToLoadingState(repositories)
        repository.searchRepositories(query: query, page: page) { newItems, error in
            print("ando error:  \(error)")
            if let error = error {
                print("ando stepToError")
                self.stepToError(repositories, error.localizedDescription)
            }
            if let newItems = newItems {
                print("ando stepToData")
                let isLastPage = newItems.count < GithubApiKt.SEARCH_PER_PAGE
                self.stepToData(newItems, isLastPage, page)
            }
        }
    }
    
    private func stepToLoadingState(_ repositories: [RepositorySummary]) {
        uiState = SearchUiState(repositories: repositories, isLoading: true, applicationError: nil)
    }
    
    private func stepToData(_ newItems: [RepositorySummary], _ isLastPage: Bool, _ page: Int32) {
        if (isLastPage) {
            uiState = SearchUiState(isFirstFetched: true, repositories: uiState.repositories + newItems, nextPageNo: nil, isLoading: false, applicationError: nil)
        } else {
            let nextPageNo = page + 1
            uiState = SearchUiState(isFirstFetched: true, repositories: uiState.repositories + newItems, nextPageNo: nextPageNo, isLoading: false, applicationError: nil)
        }
    }
    
    private func stepToError(_ repositories: [RepositorySummary], _ e: String) {
        uiState = SearchUiState(repositories: repositories, isLoading: false, applicationError: e)
    }
}

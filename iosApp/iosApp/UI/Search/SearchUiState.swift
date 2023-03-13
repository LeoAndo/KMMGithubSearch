//
//  SearchUiState.swift
//  iosApp
//
//  Created by LeoAndo on 2023/03/13.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

struct SearchUiState {
    var isFirstFetched: Bool
    var repositories: [RepositorySummary]
    var nextPageNo: Int32?
    var isLoading: Bool
    var applicationError: String?
    
    init(isFirstFetched: Bool = false, repositories: [RepositorySummary] = [], nextPageNo: Int32? = 1, isLoading: Bool = false, applicationError: String? = nil) {
        self.isFirstFetched = isFirstFetched
        self.repositories = repositories
        self.nextPageNo = nextPageNo
        self.isLoading = isLoading
        self.applicationError = applicationError
    }
}

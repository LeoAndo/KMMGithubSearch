//
//  DetailViewModel.swift
//  iosApp
//
//  Created by LeoAndo on 2023/03/14.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
final class DetailViewModel : ObservableObject {
    @Published var uiState: DetailUiState = DetailUiState.initial
    private let repository = Container().githubRepository
    
    func getRepositoryDetail(name: String, ownerName: String) {
        uiState = DetailUiState.loading
        repository.getRepositoryDetail(ownerName: ownerName, repositoryName: name) { result, error in
            if let error = error {
                self.uiState = DetailUiState.error(error.localizedDescription)
            }
            if let result = result {
                self.uiState = DetailUiState.data(DetailUiState.Data(detail:result))
            }
        }
    }
}

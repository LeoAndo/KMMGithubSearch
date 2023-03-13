//
//  DetailUiState.swift
//  iosApp
//
//  Created by LeoAndo on 2023/03/14.
//  Copyright © 2023 orgName. All rights reserved.
//
import shared

enum DetailUiState {
    struct Data { let detail: RepositoryDetail}
    case initial
    case loading
    case data(Data)
    case error(String)
}

//
//  SearchScreen.swift
//  iosApp
//
//  Created by LeoAndo on 2023/03/13.
//  Copyright © 2023 orgName. All rights reserved.
//


import SwiftUI
import shared

struct SearchScreen: View {
    @StateObject var viewModel: SearchViewModel
    var body: some View {
        MainContent(uiState: viewModel.uiState, onSubmit: { query in
            viewModel.searchRepositories(query: query, refresh: true)
        }, onReload: { query in
            viewModel.searchRepositories(query: query, page: viewModel.uiState.nextPageNo ?? 1)
        }, onMoreLoad: { query in
            viewModel.searchRepositories(query: query, page: viewModel.uiState.nextPageNo ?? 1)
        })
    }
}

struct MainContent: View {
    let uiState: SearchUiState
    let onSubmit: ((String) -> Void)
    @State var inputText = "" // TextFieldがBinding<String> に依存するのでStatelessにできない
    let onReload: ((String) -> Void)
    let onMoreLoad: ((String) -> Void)
    var body: some View {
        NavigationStack {
            VStack() {
                TextField("キーワード", text: $inputText, prompt: Text("キーワードを入力してください"))
                    .onSubmit { onSubmit(inputText) }
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()
                Spacer()
                
                ZStack {
                    // エラー表示View
                    if let applicationError = uiState.applicationError { // アンラップ
                        AppAlertDialog(openDialog: true, title:"Error", positiveButtonText: "Reload", message: applicationError, onNegativeButtonTap: {}, onPositiveButtonTap: {
                            onReload(inputText)
                        })
                    }
                    
                    // ローディングView
                    if uiState.isLoading {
                        ProgressView("fetching…")
                            .progressViewStyle(CircularProgressViewStyle())
                    }
                    
                    // データ件数0件表示View
                    if uiState.isFirstFetched && uiState.repositories.isEmpty {
                        AppAlertDialog(openDialog: true, title:"Result Empty", positiveButtonText: "Reload", message: "result empty...", onNegativeButtonTap: {}, onPositiveButtonTap: {
                            onReload(inputText)
                        })
                    }
                    
                    // データ取得成功時のListView
                    SearchScreenScucessView(repositories: uiState.repositories, onMoreLoad: {
                        if uiState.nextPageNo != nil { // nextPageNoがnilの場合次ページはない
                            onMoreLoad(inputText)
                        }
                    })
                }
                Spacer()
            }
        }
    }
}

/// 正常系で表示するView
struct SearchScreenScucessView: View {
    let repositories: [RepositorySummary]
    let onMoreLoad: (() -> Void)
    var body: some View {
        VStack(spacing: 0) {
            ScrollView(.vertical, showsIndicators: true) {
                LazyVStack(spacing: 0) {
                    ForEach(repositories, id: \.self) { repository in
                        // セル１行分のレイアウト - START
                        NavigationLink(value: repository) {
                            VStack(alignment: .leading) {
                                Text(repository.name)
                                Text(repository.ownerName)
                                Divider()
                            }
                            .frame(maxWidth: .infinity)
                            .padding([.leading, .trailing], 8)
                        }.onAppear {
                            let lastRepository = repositories.last
                            if repository == lastRepository {  // 最後までスクロールされた場合
                                onMoreLoad()
                            }
                        }
                        // セル１行分のレイアウト - END
                    }
                }.navigationDestination(for: RepositorySummary.self) { repository in
                     DetailScreen(name: repository.name, ownerName: repository.ownerName, viewModel: DetailViewModel())
                }
            }
        }
    }
}

struct SearchScreen_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            let successData: [RepositorySummary] = [
                RepositorySummary(id: 1, name: "repo01", ownerName: "owner01"),
                RepositorySummary(id: 2, name: "repo02", ownerName: "owner02"),
                RepositorySummary(id: 3, name: "repo03", ownerName: "owner03"),
                RepositorySummary(id: 4, name: "repo04", ownerName: "owner04"),
                RepositorySummary(id: 5, name: "repo05", ownerName: "owner05"),
                RepositorySummary(id: 6, name: "repo06", ownerName: "owner06"),
                RepositorySummary(id: 7, name: "repo07", ownerName: "owner07"),
                RepositorySummary(id: 8, name: "repo08", ownerName: "owner08"),
                RepositorySummary(id: 9, name: "repo09", ownerName: "owner09"),
                RepositorySummary(id: 10, name: "repo10", ownerName: "owner10"),
                RepositorySummary(id: 11, name: "repo11", ownerName: "owner11"),
                RepositorySummary(id: 12, name: "repo12", ownerName: "owner12"),
                RepositorySummary(id: 13, name: "repo13", ownerName: "owner13"),
                RepositorySummary(id: 14, name: "repo14", ownerName: "owner14"),
                RepositorySummary(id: 15, name: "repo15", ownerName: "owner15"),
            ]
            
            // ローディング: 初回
            MainContent(uiState: SearchUiState(isFirstFetched: false, isLoading: true), onSubmit: {_ in }, inputText: "Flutter", onReload: {_ in },onMoreLoad:{_ in })
            // ローディング: データあり
            MainContent(uiState: SearchUiState(isFirstFetched: true, repositories: successData, isLoading: true), onSubmit: {_ in }, inputText: "Flutter", onReload: {_ in },onMoreLoad:{_ in })
            
            // 正常系: データあり
            MainContent(uiState:SearchUiState(isFirstFetched: true, repositories: successData), onSubmit: {_ in }, inputText: "Flutter", onReload: {_ in },onMoreLoad:{_ in })
            // 正常系: データなし
            MainContent(uiState:SearchUiState(isFirstFetched: true), onSubmit: {_ in }, inputText: "Flutter", onReload: {_ in },onMoreLoad:{_ in })
            
            // 異常系: データあり
            MainContent(uiState: SearchUiState(isFirstFetched: true, repositories: successData, applicationError: "network error...."), onSubmit: {_ in }, inputText: "Flutter", onReload: {_ in },onMoreLoad:{_ in })
            // 異常系: 初回 ネットワークエラー
            MainContent(uiState: SearchUiState(isFirstFetched: false, applicationError: "network error...."), onSubmit: {_ in }, inputText: "Flutter", onReload: {_ in },onMoreLoad:{_ in })
        }
    }
}

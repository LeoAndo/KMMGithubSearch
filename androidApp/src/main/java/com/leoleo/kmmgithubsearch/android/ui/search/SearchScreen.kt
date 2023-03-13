package com.leoleo.kmmgithubsearch.android.ui.search

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.leoleo.kmmgithubsearch.android.ui.component.AppSurface
import com.leoleo.kmmgithubsearch.android.ui.preview.PreviewPhoneDevice
import com.leoleo.kmmgithubsearch.domain.exception.ApiErrorType
import com.leoleo.kmmgithubsearch.domain.model.RepositorySummary
import com.leoleo.kmmgithubsearch.android.R
import com.leoleo.kmmgithubsearch.android.ui.component.AppAlertDialog
import com.leoleo.kmmgithubsearch.android.ui.component.AppError
import com.leoleo.kmmgithubsearch.android.ui.component.AppLoading
import com.leoleo.kmmgithubsearch.android.ui.preview.PrevData
import com.leoleo.kmmgithubsearch.domain.exception.ValidationErrorType

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navigateToDetailScreen: (String, String) -> Unit,
    viewModel: SearchViewModel
) {
    var query by rememberSaveable { mutableStateOf("") }
    SearchScreenStateless(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag(stringResource(id = R.string.test_tag_search_screen)),
        uiState = viewModel.uiState,
        query = query,
        onValueChange = { query = it },
        onSubmit = { viewModel.searchRepositories(query = query, refresh = true) },
        onSearch = {
            viewModel.searchRepositories(
                query = query,
                page = viewModel.uiState.nextPageNo ?: 1
            )
        },
        onClickCardItem = { ownerName, name -> navigateToDetailScreen(ownerName, name) },
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenStateless(
    modifier: Modifier = Modifier,
    uiState: UiState,
    query: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSearch: () -> Unit,
    onClickCardItem: (String, String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = modifier) {
        OutlinedTextField(
            value = query,
            label = {
                Text(text = stringResource(R.string.label_search_keyword))
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                onSubmit()
            }),
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            isError = query.isEmpty()
        )
        Spacer(modifier = Modifier.size(12.dp))

        Box {
            // State: Result Empty
            if (uiState.isFirstFetched && uiState.repositories.isEmpty()) {
                AppAlertDialog(
                    titleText = "Result Empty",
                    messageText = stringResource(id = R.string.empty_message),
                    confirmText = stringResource(id = R.string.reload),
                    onClickConfirmButton = onSearch
                )
            }

            // State: Success
            SuccessView(
                modifier = Modifier.fillMaxSize(),
                repositories = uiState.repositories,
                onMoreLoad = {
                    Log.d("TAG", "ando ${uiState.isLoading} : ${uiState.nextPageNo}")
                    // nextPageNoがnullの場合次ページはない
                    if (uiState.nextPageNo != null && !uiState.isLoading) {
                        Log.d("TAG", "ando 次ページを取得する nextPageNo: ${uiState.nextPageNo}")
                        onSearch()
                    }
                },
                onClickCardItem = { ownerName, repositoryName ->
                    onClickCardItem(ownerName, repositoryName)
                })

            // State: Error
            when (val error = uiState.applicationError) {
                is ApiErrorType -> {
                    AppAlertDialog(
                        titleText = "Fetch Error",
                        messageText = error.localizedMessage,
                        confirmText = stringResource(id = R.string.reload),
                        onClickConfirmButton = onSearch
                    )
                }
                is ValidationErrorType -> {
                    val message = error.localizedMessage
                        ?: stringResource(id = R.string.default_error_message)
                    AppError(
                        message = message,
                        onReload = onSearch,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // State: Loading
            if (uiState.isLoading) AppLoading(modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.size(12.dp))
    }
}

// 正常系で表示するView
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessView(
    modifier: Modifier,
    repositories: List<RepositorySummary>,
    onMoreLoad: () -> Unit,
    onClickCardItem: (String, String) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(modifier = modifier, state = listState) {
        items(repositories) { item ->
            Card(
                onClick = { onClickCardItem(item.ownerName, item.name) },
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        item.ownerName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(item.name)
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }

    val isScrolledToTheEnd by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1 }
    }
    if (isScrolledToTheEnd) {
        // TODO: WORKAROUND 次ページ取得時に2ページ分取得してしまうので LaunchedEffectを使用し回避した
        LaunchedEffect(Unit, block = {
            Log.d("TAG", "ando isScrolledToTheEnd: $isScrolledToTheEnd")
            onMoreLoad()
        })
    }
}

// ローディング: 初回
@PreviewPhoneDevice
@Composable
private fun Prev_Loading_isFirstFetched_False_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(isFirstFetched = false, isLoading = true),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}

// ローディング: データあり
@PreviewPhoneDevice
@Composable
private fun Prev_Loading_isNotEmpty_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(
                isFirstFetched = true,
                isLoading = true,
                repositories = PrevData.repositorySummaries
            ),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}

// 正常系: データあり
@PreviewPhoneDevice
@Composable
private fun Prev_Success_isNotEmpty_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(
                isFirstFetched = true,
                repositories = PrevData.repositorySummaries
            ),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}

// 正常系: データなし
@PreviewPhoneDevice
@Composable
private fun Prev_Success_isEmpty_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(isFirstFetched = true),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}

// 異常系: データあり
@PreviewPhoneDevice
@Composable
private fun Prev_Error_isNotEmpty_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(
                isFirstFetched = true,
                repositories = PrevData.repositorySummaries,
                applicationError = ApiErrorType.Network
            ),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}

// 異常系: 初回 ネットワークエラー
@PreviewPhoneDevice
@Composable
private fun Prev_Error_isFirstFetched_False_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(
                isFirstFetched = false,
                applicationError = ApiErrorType.Network
            ),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}

// 異常系: 初回 httpエラー
@PreviewPhoneDevice
@Composable
private fun Prev_Error_Http_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(
                isFirstFetched = false,
                applicationError = ApiErrorType.UnAuthorized("Bad Credential.")
            ),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}

// 異常系: 初回 入力バリデーション
@PreviewPhoneDevice
@Composable
private fun Prev_Error_Validation_SearchScreen() {
    AppSurface {
        SearchScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            uiState = UiState(
                isFirstFetched = false,
                applicationError = ValidationErrorType.Empty("please input search word.")
            ),
            query = "flutter",
            onValueChange = {},
            onSubmit = {},
            onSearch = {},
            onClickCardItem = { _, _ -> },
        )
    }
}
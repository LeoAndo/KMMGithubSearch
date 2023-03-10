package com.leoleo.kmmgithubsearch.android.ui.medium

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leoleo.kmmgithubsearch.android.R
import com.leoleo.kmmgithubsearch.android.ui.MyNavHost
import com.leoleo.kmmgithubsearch.android.ui.Page
import com.leoleo.kmmgithubsearch.android.ui.TopDestinations
import com.leoleo.kmmgithubsearch.android.ui.component.AppSurface
import com.leoleo.kmmgithubsearch.android.ui.preview.PreviewTabletDevice
import com.leoleo.kmmgithubsearch.android.ui.user.UserScreen

@Composable
fun MediumMainScreen() {
    val items = Page.values()
    var selectedItem by rememberSaveable { mutableStateOf(items[0]) }
    MediumMainScreenStateless(
        modifier = Modifier
            .testTag(stringResource(id = R.string.test_tag_medium_main_screen))
            .fillMaxSize()
            .padding(12.dp),
        selectedItem = selectedItem,
        onClickNavigationRailItem = { item ->
            selectedItem = item
        })
}

@Composable
private fun MediumMainScreenStateless(
    modifier: Modifier = Modifier,
    selectedItem: Page,
    onClickNavigationRailItem: (Page) -> Unit,
) {
    Row {
        NavigationRail {
            Page.values().forEachIndexed { _, item ->
                NavigationRailItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = selectedItem == item,
                    onClick = { onClickNavigationRailItem(item) },
                    modifier = Modifier
                        .testTag(item.label)
                        .padding(12.dp)
                )
            }
        }
        when (Page.values().firstOrNull { it == selectedItem }) {
            Page.HOME -> HomeScreen(modifier)
            Page.SEARCH -> {
                MyNavHost(startDestination = TopDestinations.SearchRoute.routeName)
            }
            Page.User -> UserScreen(modifier)
            null -> Text(
                text = "$selectedItem is unknown.",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(text = stringResource(id = R.string.medium_screen_message))
        Text(text = stringResource(id = R.string.medium_screen_message2))
        Text(text = stringResource(id = R.string.medium_screen_message3))
    }
}

@PreviewTabletDevice
@Composable
private fun Prev_Home_MediumMainScreen() {
    AppSurface {
        MediumMainScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            selectedItem = Page.HOME,
            onClickNavigationRailItem = {},
        )
    }
}

@PreviewTabletDevice
@Composable
private fun Prev_Search_MediumMainScreen() {
    AppSurface {
        MediumMainScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            selectedItem = Page.SEARCH,
            onClickNavigationRailItem = {},
        )
    }
}

@PreviewTabletDevice
@Composable
private fun Prev_User_MediumMainScreen() {
    AppSurface {
        MediumMainScreenStateless(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            selectedItem = Page.User,
            onClickNavigationRailItem = {},
        )
    }
}
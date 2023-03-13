package com.leoleo.kmmgithubsearch.android.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.leoleo.kmmgithubsearch.android.ui.compact.CompactMainScreen
import com.leoleo.kmmgithubsearch.android.ui.component.AppSurface
import com.leoleo.kmmgithubsearch.android.ui.expanded.ExpandedMainScreen
import com.leoleo.kmmgithubsearch.android.ui.medium.MediumMainScreen

@Composable
fun MainScreen(windowWidthSizeClass: WindowWidthSizeClass) {
    AppSurface {
        when (windowWidthSizeClass) {
            WindowWidthSizeClass.Compact -> CompactMainScreen()
            WindowWidthSizeClass.Medium -> MediumMainScreen()
            WindowWidthSizeClass.Expanded -> ExpandedMainScreen()
        }
    }
}
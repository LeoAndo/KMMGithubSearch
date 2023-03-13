package com.leoleo.kmmgithubsearch.android.ui.preview

import com.leoleo.kmmgithubsearch.domain.model.RepositorySummary

object PrevData {
    val repositorySummaries: List<RepositorySummary> by lazy {
        buildList {
            repeat(30) { idx ->
                add(RepositorySummary(id = idx, name = "repo$idx", ownerName = "owner$idx"))
            }
        }
    }
}
package com.leoleo.kmmgithubsearch.di

import com.leoleo.kmmgithubsearch.data.api.github.GithubApi
import com.leoleo.kmmgithubsearch.data.api.KtorHandler
import com.leoleo.kmmgithubsearch.data.repository.GithubRepoRepositoryImpl
import com.leoleo.kmmgithubsearch.domain.repository.GithubRepoRepository
import kotlinx.serialization.json.Json

// TODO KMMにおいてのDIの Best Practiceが確立されるまでこの形で開発を進める
object Container {
    private val ktorHandler by lazy {
        println("ando create ktorHandler")
        KtorHandler()
    }
    private val githubApi by lazy {
        println("ando create githubApi")
        GithubApi(
            format = Json { ignoreUnknownKeys = true },
            ktorHandler = ktorHandler
        )
    }
    val githubRepository: GithubRepoRepository
        get() {
            println("ando create githubRepository")
            return GithubRepoRepositoryImpl(githubApi)
        }
}
package com.leoleo.kmmgithubsearch

import com.leoleo.kmmgithubsearch.data.api.github.GithubApi
import com.leoleo.kmmgithubsearch.data.api.KtorHandler
import com.leoleo.kmmgithubsearch.data.api.github.response.toModels
import kotlinx.serialization.json.Json

class Greeting {
    private val platform: Platform = getPlatform()
    suspend fun greet(): String {
        val api = GithubApi(format = Json { ignoreUnknownKeys = true }, KtorHandler())
        val results = api.searchRepositories("AndroidGithubSearch", 1).toModels()
        return results.first().ownerName
    }
}
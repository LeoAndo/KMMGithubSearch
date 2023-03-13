package com.leoleo.kmmgithubsearch.data.api.github.response

@kotlinx.serialization.Serializable
internal data class GithubErrorResponse(val documentation_url: String, val message: String)
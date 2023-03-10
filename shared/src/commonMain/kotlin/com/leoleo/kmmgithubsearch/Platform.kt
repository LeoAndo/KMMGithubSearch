package com.leoleo.kmmgithubsearch

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
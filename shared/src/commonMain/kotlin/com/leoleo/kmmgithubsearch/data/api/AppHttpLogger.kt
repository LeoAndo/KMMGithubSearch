package com.leoleo.kmmgithubsearch.data.api

import io.ktor.client.plugins.logging.*

internal class AppHttpLogger : Logger {
    override fun log(message: String) {
        println("AppHttpLogger $message")
    }
}
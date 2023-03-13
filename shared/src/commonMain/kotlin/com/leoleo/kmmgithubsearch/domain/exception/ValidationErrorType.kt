package com.leoleo.kmmgithubsearch.domain.exception

/**
 * 検査の例外用クラス
 */
sealed class ValidationErrorType : ApplicationError() {
    data class Empty(override val message: String?) : ValidationErrorType()
}
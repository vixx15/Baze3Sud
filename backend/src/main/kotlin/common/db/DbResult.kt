package com.baze3.common.db

sealed class DbResult<out T> {
    data class Success<T>(val data: T, val message: String? = null) : DbResult<T>()

    data class Error(
        val message: String,
        val code: String? = null,
        val isValidationError: Boolean = false
    ) : DbResult<Nothing>()
}
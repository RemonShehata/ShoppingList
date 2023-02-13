package com.example.shoppinglist.data

sealed class State<out T, out E> {
    object Loading : State<Nothing, Nothing>()
    data class Error<E>(val errorType: E) : State<Nothing, E>()
    data class Success<T>(val data: T) : State<T, Nothing>()
}

sealed class InsertionError {
    object DuplicateItem : InsertionError()
    data class InvalidData(val fields: InvalidField) : InsertionError()
}

sealed class InvalidField {
    object ItemName : InvalidField()
    object ItemQuantity : InvalidField()
}

sealed class UpdateError {
    object Failure : UpdateError()
}

sealed class QueryError {
    object GeneralError : QueryError()
}

package ru.magomedcoder.chatopenai.utils

sealed class Failure : Throwable() {

    object NetworkError : Failure() {
        override val message = "Сеть недоступна"
    }

    data class OtherError(val throwable: Throwable? = null) : Failure()

}

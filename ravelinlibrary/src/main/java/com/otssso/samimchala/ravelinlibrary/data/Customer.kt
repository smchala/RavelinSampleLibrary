package com.otssso.samimchala.ravelinlibrary.data

import java.util.regex.Pattern.compile

data class Customer(
    var customerId: String = "default",
    var email: String = "email@email.com",
    var name: String = "default"
) {
    val alphaRegex = "[A-Za-z]".toRegex()

    init {
        val anyEmailRegex = compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        if (!alphaRegex.matches(name)) {
            name = "default"
        }
        if(!anyEmailRegex.matcher(email).matches()){
            email = "email@email.com"
        }
    }
}
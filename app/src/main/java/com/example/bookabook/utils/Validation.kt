package com.example.bookabook.utils

import android.util.Patterns

object Validation {

    fun validateInput(input: String): ValidationMSG {

        return if (input == "") {
            ValidationMSG.Empty
        } else if (!input.matches("^[\\u0621-\\u064Aa-zA-Z\\d\\-_\\s]+\$".toRegex())) {
            ValidationMSG.NotMatchingNames
        } else if (input.length < 3) {
            ValidationMSG.TooSmall
        } else ValidationMSG.Good
    }

    fun validateEmail(input: String): ValidationMSG {
        return if (input == "") {
            ValidationMSG.Empty
        } else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            ValidationMSG.NotEmail
        } else ValidationMSG.Good
    }

    fun validatePhone(input: String): ValidationMSG {

        return if (input == "") {
            ValidationMSG.Empty
        } else if (!Patterns.PHONE.matcher(input).matches()) {
            ValidationMSG.NotPhone
        } else if (input.length < 10) {
            ValidationMSG.TooSmall
        } else ValidationMSG.Good
    }

    fun validatePassword(input: String): ValidationMSG {

        return when {
            input == "" -> {
                ValidationMSG.Empty
            }
            input.length < 5 -> {
                ValidationMSG.TooSmall
            }
            else -> ValidationMSG.Good
        }
    }


    fun validationResult(it: ValidationMSG?): String? {
        return when (it) {
            ValidationMSG.Empty -> "Can not be empty"
            ValidationMSG.TooSmall -> "Too Small"
            ValidationMSG.NotMatchingNames -> "Must contain only letters numbers and underscores"
            ValidationMSG.NotPhone -> "Please enter valid phone number"
            ValidationMSG.NotEmail -> "Please enter valid Email Address"
            else -> null
        }
    }

}

enum class ValidationMSG {
    Empty,
    TooSmall,
    NotMatchingNames,
    NotEmail,
    NotPhone,
    Good
}
package com.example.enrollapp

import android.util.Patterns

class Validator {
    //  A placeholder username validation check
      fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    //  A placeholder name validation check
     fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    // A placeholder number validation check
     fun isNumberValid(number: String): Boolean {
        return (number.length > 10 && number.length < 12)
    }

    // A placeholder gender validation check
     fun isGenderValid(gender: String): Boolean {
        return (gender != "gender")
    }

    private fun isInteger(str: String?) = str?.toIntOrNull()?.let {  true } ?:  false
}
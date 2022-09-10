package com.example.midtermproject.stringmanipulation

object StringManipulation {
    private const val vowels = "aeiou"
    private const val consonants = "bcdfghjklmnpqrstvwxyz"

    // 2.1 convert all consonant to $
    fun convertConsonantsToSign(str: String, replacement: Char = '$'): String {
        var processedStr = ""
        for (char in str) {
            processedStr += if (char.lowercase() in consonants) replacement else char
        }
        return processedStr
    }

    // 2.2 convert the string to uppercase
    fun getUppercase(str: String): String {
        return str.uppercase()
    }

    // 2.3 total number of vowels
    fun getVowelCount(str: String): Int {
        var i = 0
        for (char in str) { if (char in vowels) i++ }
        return i
    }
}
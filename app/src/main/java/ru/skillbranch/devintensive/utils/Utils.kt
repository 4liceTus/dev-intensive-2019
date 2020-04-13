package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName:String?): Pair<String?, String?> {
        val parts:List<String>? = fullName?.trim()?.split(" ")
        return Pair(parts?.getOrNull(0), parts?.getOrNull(1))
    }

    fun transliteration(payload: String, divider: String = " "): String {
        // TODO
        return " "
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val name = firstName.orEmpty().trim().getOrNull(0)?.toUpperCase()
        val surname = lastName.orEmpty().trim().getOrNull(0)?.toUpperCase()
        val firstInit = name?.toString() ?: ""
        val secondInit = surname?.toString() ?: ""
        return "$firstInit$secondInit".ifEmpty { null }
    }
}
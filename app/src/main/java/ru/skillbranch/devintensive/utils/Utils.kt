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
        // TODO
        return " "
    }
}
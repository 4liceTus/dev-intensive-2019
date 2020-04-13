package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName:String?): Pair<String?, String?> {
        val parts:List<String>? = fullName?.trim()?.split(" ")
        return Pair(parts?.getOrNull(0)?.ifEmpty { null }, parts?.getOrNull(1)?.ifEmpty { null })
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val map = getTranslitMap()
        val builder = StringBuilder()

        for (char in payload.trim())
            builder.append(getTranslitChar(char, map))

        return builder.toString().replace(" ", divider)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val name = firstName.orEmpty().trim().getOrNull(0)?.toUpperCase()
        val surname = lastName.orEmpty().trim().getOrNull(0)?.toUpperCase()
        val firstInit = name?.toString() ?: ""
        val secondInit = surname?.toString() ?: ""
        return "$firstInit$secondInit".ifEmpty { null }
    }

    private fun getTranslitMap(): HashMap<Char, String> {
        val map = hashMapOf<Char, String>()
        map['а'] = "a"
        map['б'] = "b"
        map['в'] = "v"
        map['г'] = "g"
        map['д'] = "d"
        map['е'] = "e"
        map['ё'] = "e"
        map['ж'] = "zh"
        map['з'] = "z"
        map['и'] = "i"
        map['й'] = "i"
        map['к'] = "k"
        map['л'] = "l"
        map['м'] = "m"
        map['н'] = "n"
        map['о'] = "o"
        map['п'] = "p"
        map['р'] = "r"
        map['с'] = "s"
        map['т'] = "t"
        map['у'] = "u"
        map['ф'] = "f"
        map['х'] = "h"
        map['ц'] = "c"
        map['ч'] = "ch"
        map['ш'] = "sh"
        map['щ'] = "sh'"
        map['ъ'] = ""
        map['ы'] = "i"
        map['ь'] = ""
        map['э'] = "e"
        map['ю'] = "yu"
        map['я'] = "ya"

        return map
    }

    private fun getTranslitChar(char: Char, map: HashMap<Char, String>): String {
        val translit  = map[char.toLowerCase()] ?: char.toString()
        return if (char.isUpperCase() && translit.isNotEmpty())
            translit.capitalize()
        else translit
    }
}
package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    val trimString = this.trim()
    return if (trimString.length <= length) trimString else """${trimString.substring(0, length).trim()}..."""
}

fun String.stripHtml(): String{
    /* пример escape-последовательности html: &amp; = & (ampersand)
     . - любой символ
     * - {0,} - 0 и более повторений символа
     ? - ленивый квантификатор
     | - или
     [] - последовательность
     ^ - отрицание
     */
    val htmlRegex = Regex("(<.*?>)|(&[a-z]{1,4}?;)")
    val spaceRegex = Regex(" {2,}") // больше одного пробела
    return this.replace(htmlRegex, "").replace(spaceRegex, " ")
}
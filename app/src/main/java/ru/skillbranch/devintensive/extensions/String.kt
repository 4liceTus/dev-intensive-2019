package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    val trimString = this.trim()
    return if (trimString.length <= length) trimString else """${trimString.substring(0, length).trim()}..."""
}
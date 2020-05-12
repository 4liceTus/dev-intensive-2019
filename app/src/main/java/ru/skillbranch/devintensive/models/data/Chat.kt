package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.models.BaseMessage

class Chat (
    val id: String,
    val members: MutableList<User> = mutableListOf(),
    val messages: MutableList<BaseMessage> = mutableListOf()
)
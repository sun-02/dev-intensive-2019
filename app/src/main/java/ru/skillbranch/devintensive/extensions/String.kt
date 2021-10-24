package ru.skillbranch.devintensive.extensions

fun String.truncate(maxLength: Int = 16): String {
    return StringBuilder().append(substring(0 until maxLength).trim())
        .append("...")
        .toString()
}

fun String.stripHtml(): String {
    val regex = ">.+<\\/".toRegex()
    val str = regex.find(this)!!.value
    return str.substring(1 until str.length - 2).replace("\\s+".toRegex(), " ")
}
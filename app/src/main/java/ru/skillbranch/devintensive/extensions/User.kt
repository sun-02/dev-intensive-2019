package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.models.UserView
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

fun User.toUserView() : UserView {
    val fullName = "$firstName $lastName"
    val nickName = Utils.transliteration(fullName)
    val initials = Utils.toInitials(firstName, lastName)
    val lastVisit: Date? = lastVisit?.clone() as? Date
    val status = when {
        lastVisit == null -> "Was never appeared"
        isOnline -> "Online"
        else -> "Last time was ${lastVisit.humanizeDiff()}"
    }

    return UserView(
        id,
        fullName = fullName,
        nickName = nickName,
        avatar,
        initials = initials,
        status = status,
    )
}



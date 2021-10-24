package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id : String,
    var firstName : String?,
    var lastName : String?,
    var avatar : String?,
    var rating : Int = 0,
    var respect : Int = 0,
    var lastVisit : Date? = Date(),
    var isOnline : Boolean = false
) {
    constructor(
        id: String,
        firstName: String?,
        lastName: String?
    ) : this(id, firstName, lastName, null)

    constructor(id: String) : this(id, "John", "Doe")

    init {
        println("It's Alive!!!" +
            "${if (lastName === "Doe") "His name id $firstName $lastName"
                else "And his name is $firstName $lastName"}\n")
    }

    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?): User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User("$lastId", firstName, lastName)
        }
    }

    class Builder() {
        var id : String = "-1"
            private set
        var firstName : String? = null
            private set
        var lastName : String? = null
            private set
        var avatar : String? = null
            private set
        var rating : Int = 0
            private set
        var respect : Int = 0
            private set
        var lastVisit : Date? = Date()
            private set
        var isOnline : Boolean = false
            private set

        fun id(id : String): Builder = apply { this.id = id }

        fun firstName(firstName : String?): Builder = apply { this.firstName = firstName }

        fun lastName(lastName : String?): Builder = apply { this.lastName = lastName }

        fun avatar(avatar : String?): Builder = apply { this.avatar = avatar }

        fun rating(rating : Int): Builder = apply { this.rating = rating }

        fun respect(respect : Int = 0): Builder = apply { this.respect = respect }

        fun lastVisit(lastVisit : Date?): Builder = apply { this.lastVisit = lastVisit }

        fun isOnline(isOnline : Boolean): Builder = apply { this.isOnline = isOnline }

        fun build(): User = User(
            id,
            firstName,
            lastName,
            avatar,
            rating,
            respect,
            lastVisit,
            isOnline
        )

    }

    fun printMe() = println("""
            id: $id,
            firstName: $firstName,
            lastName: $lastName,
            avatar: $avatar,
            rating: $rating,
            respect: $respect,
            lastVisit: $lastVisit,
            isOnline: $isOnline,
        """.trimIndent())
}
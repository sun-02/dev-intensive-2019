package ru.skillbranch.devintensive.utils

//import org.jsoup.Jsoup

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts = fullName?.split(" ")
        if (fullName.isNullOrBlank()) return null to null
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    fun transliteration(str: String, outDivider: String = " "): String {
        val dict = getTransliterationMap()
        val str1 = str.replace(" ", outDivider)
        return str1.map { c -> dict[c] ?: c }.joinToString(separator = "")
    }

    private fun getTransliterationMap(): Map<Char, String> {
//        val url = "https://skill-branch.ru/resources/dev-intensive-2019/" +
//                "lesson2/transliteration.html"
//        val lines = getTextFromHtml(url)
        val lines = """
            "а": "a",
            "б": "b",
            "в": "v",
            "г": "g",
            "д": "d",
            "е": "e",
            "ё": "e",
            "ж": "zh",
            "з": "z",
            "и": "i",
            "й": "i",
            "к": "k",
            "л": "l",
            "м": "m",
            "н": "n",
            "о": "o",
            "п": "p",
            "р": "r",
            "с": "s",
            "т": "t",
            "у": "u",
            "ф": "f",
            "х": "h",
            "ц": "c",
            "ч": "ch",
            "ш": "sh",
            "щ": "sh'",
            "ъ": "",
            "ы": "i",
            "ь": "",
            "э": "e",
            "ю": "yu",
            "я": "ya",
        """.trimIndent().split("\n")
        val dict0 = lines.map { line ->
            "[A-я]".toRegex().find(line)!!.value[0] to
                    "[A-z]".toRegex().findAll(line)
                        .joinToString(separator = "") { c -> c.value }
        }
            .toMap()
        val dict1 = dict0.map { e ->
            val keyUp = e.key.uppercaseChar()
            if (e.value.isNotEmpty()) {
                val val0 = e.value[0]
                val val0up = val0.uppercaseChar()
                keyUp to e.value.replaceFirst(val0, val0up)
            } else {
                keyUp to ""
            }
        }.toMap()
        val dict: MutableMap<Char, String> = mutableMapOf()
        dict.putAll(dict0)
        dict.putAll(dict1)
        return dict
    }

//    fun getTextFromHtml(url: String): List<String> {
//        val userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:93.0)" +
//                " Gecko/20100101 Firefox/93.0"
//        val doc = Jsoup.connect(url)
//            .userAgent(userAgent)
//            .get()
//        return doc.select("p").eachText()
//    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName.isNullOrBlank() && lastName.isNullOrBlank()) return null
        return (firstName?.get(0)?.uppercase() ?: "") +
                (lastName?.get(0)?.uppercase() ?: "")
    }
}
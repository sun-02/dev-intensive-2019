package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = SECOND * 60
const val HOUR = MINUTE * 60
const val DAY = HOUR * 24

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += getTimeInterval(value, units)

    this.time = time
    return this
}

private fun getTimeInterval(
    value: Int,
    units: TimeUnits
) = when (units) {
    TimeUnits.SECOND -> value * SECOND
    TimeUnits.MINUTE -> value * MINUTE
    TimeUnits.HOUR -> value * HOUR
    TimeUnits.DAY -> value * DAY
}

fun Date.humanizeDiff(): String {
    val lastSeenInterval = Date().time - this.time
    val humanizedIntervals = getHumanizedIntervals()
    var diff: String? = null
    val pluralsRegex = "N [А-я]+".toRegex()
    for (interval in humanizedIntervals) {
        if (lastSeenInterval < interval.key) {
            diff = interval.value
            if (diff.contains("N")) {
                val pluralString = pluralsRegex.find(diff)!!.value
                val replacement = when (pluralString) {
                    "N минут" -> TimeUnits.MINUTE.plural(((abs(lastSeenInterval)+200) / MINUTE).toInt())
                    "N часов" -> TimeUnits.HOUR.plural((abs(lastSeenInterval) / HOUR).toInt())
                    "N дней" -> TimeUnits.DAY.plural((abs(lastSeenInterval) / DAY).toInt())
                    else -> throw IllegalArgumentException("Wrong plurals time substring")
                }
                diff = diff.replace(pluralString, replacement)
            }
            break
        }
    }
    return diff!!
}

private fun getHumanizedIntervals(): Map<Long, String> {
//    val url = "https://skill-branch.ru/resources/dev-intensive-2019/lesson2/intervals.html"
//    val lines = Utils.getTextFromHtml(url)
    val lines = """
        0с - 1с "только что"
        1с - 45с "несколько секунд назад"
        45с - 75с "минуту назад"
        75с - 45мин "N минут назад"
        45мин - 75мин "час назад"
        75мин 22ч "N часов назад"
        22ч - 26ч "день назад"
        26ч - 360д "N дней назад"
        >360д "более года назад"
    """.trimIndent().split("\n")
    val keyRegex = ">|(\\d+(с|мин|ч|д))".toRegex()
    val valRegex = "\"[NА-я ]+\"".toRegex()
    val numRegex = "\\d+".toRegex()
    val tuRegex = "с|мин|ч|д".toRegex()
    val intervals: MutableMap<Long, String> = mutableMapOf()
    val posIntervals = lines.map { line ->
        val match = keyRegex.find(line)
        val keyString = match!!.next()!!.value
        val key = if (line.contains('>')) {
            Long.MAX_VALUE
        } else {
            val keyNumber = numRegex.find(keyString)!!.value.toInt()
            val keyTu = when (tuRegex.find(keyString)!!.value) {
                "с" -> TimeUnits.SECOND
                "мин" -> TimeUnits.MINUTE
                "ч" -> TimeUnits.HOUR
                "д" -> TimeUnits.DAY
                else -> throw IllegalArgumentException("Wrong time unit")
            }
            getTimeInterval(keyNumber, keyTu)
        }
        val valString = valRegex.find(line)!!.value
        val valRange = 1 until valString.length - 1
        key to valString.subSequence(valRange).toString()
    }.toList()

    val negIntervals = listOf<Pair<Long, String>>(
        -31_104_000_000L to "более чем через год",
        -93_600_000L to "через N дней",
        -79_200_000L to "через день",
        -4_500_000L to "через N часов",
        -2_700_000L to "через час",
        -75_000L to "через N минут",
        -45_000L to "через минуту",
        0L to "через несколько секунд",
    )

    intervals.putAll(negIntervals)
    intervals.putAll(posIntervals)

    return intervals
}

enum class TimeUnits(
    private val one: String,
    private val twoToFour: String,
    private val fiveToNineAndZero: String
) {
    SECOND("секунду", "секунды", "секунд"),
    MINUTE("минуту", "минуты", "минут"),
    HOUR("час", "часа", "часов"),
    DAY("день", "дня", "дней");

    fun plural(value: Int): String{
        return "$value " +
        (if (value in 11..20 ||
            value % 10 in 5..9 ||
            value % 10 == 0) this.fiveToNineAndZero
        else if (value % 10 in 2..4) this.twoToFour
        else this.one)
    }
}
package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += units.value * value
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val dif = abs(this.time - date.time)
    val isPast = this.time < date.time

    return when {
        dif <= TimeUnits.SECOND.value -> "только что"
        dif <= TimeUnits.SECOND.value * 45 -> getPeriodForm("несколько секунд", isPast)
        dif <= TimeUnits.SECOND.value * 75 -> getPeriodForm("минуту", isPast)
        dif <= TimeUnits.MINUTE.value * 45 -> getPeriodForm(TimeUnits.MINUTE.plural((dif / TimeUnits.MINUTE.value).toInt()), isPast)
        dif <= TimeUnits.MINUTE.value * 75 -> getPeriodForm("час", isPast)
        dif <= TimeUnits.HOUR.value * 22 -> getPeriodForm(TimeUnits.HOUR.plural((dif / TimeUnits.HOUR.value).toInt()), isPast)
        dif <= TimeUnits.HOUR.value * 26 -> getPeriodForm("день", isPast)
        dif <= TimeUnits.DAY.value * 360 -> getPeriodForm(TimeUnits.DAY.plural((dif / TimeUnits.DAY.value).toInt()), isPast)
        else -> if(isPast) "более года назад" else "более чем через год"
    }
}

fun Date.shortFormat(): String? {
    val pattern = if(this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time / TimeUnits.DAY.value
    val day2 = date.time / TimeUnits.DAY.value
    return  day1 == day2
}

fun getPeriodForm(interval: String, isPast: Boolean): String {
    val prefix = if (isPast) "" else "через"
    val postfix = if (isPast) "назад" else ""
    return "$prefix $interval $postfix".trim()
}

fun getPluralForm(amount: Int, units: TimeUnits): String {
    return when(val valueAmount = abs(amount) % 100) {
        1 -> Plurals.ONE.get(units)
        in 2..4 -> Plurals.FEW.get(units)
        0, in 5..19 -> Plurals.MANY.get(units)
        else -> getPluralForm(valueAmount % 10, units)
    }
}

enum class TimeUnits(val value:Long){
    SECOND(1000L),
    MINUTE(60 * SECOND.value),
    HOUR(60 * MINUTE.value),
    DAY(24 * HOUR.value);

    fun plural(value: Int): String {
        return "$value ${getPluralForm(value, this)}"
    }
}

enum class Plurals(private val second: String, private val minute: String, private val hour: String, private val day: String){
    ONE("секунду", "минуту", "час", "день"),
    FEW("секунды", "минуты", "часа", "дня"),
    MANY("секунд","минут", "часов", "дней");

    fun get(unit: TimeUnits): String {
        return when(unit) {
            TimeUnits.SECOND -> second
            TimeUnits.MINUTE -> minute
            TimeUnits.HOUR -> hour
            TimeUnits.DAY -> day
        }
    }
}
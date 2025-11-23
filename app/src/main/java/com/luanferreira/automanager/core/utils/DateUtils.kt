package com.luanferreira.automanager.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.toDataLegivel(): String {
    if (this == 0L) return "Não informada"
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    formatter.timeZone = TimeZone.getTimeZone("UTC") // O DatePicker do Material trabalha em UTC
    return formatter.format(Date(this))
}
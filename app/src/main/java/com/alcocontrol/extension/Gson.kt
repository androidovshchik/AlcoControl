package com.alcocontrol.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.listFromJson(json: String): List<T> =
    fromJson(json, object : TypeToken<List<T>>() {}.type)
package com.alcocontrol.data

import android.content.res.AssetManager
import com.alcocontrol.extension.listFromJson
import com.alcocontrol.model.Alcohol
import com.alcocontrol.model.Character
import com.google.gson.Gson
import java.io.BufferedReader
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val assets: AssetManager,
    private val gson: Gson
) {

    val allChars: Map<Alcohol, List<List<Character>>>
        get() {
            return Alcohol.values()
                .associateWith { gson.listFromJson(readFile(it.data)) }
        }

    fun readFile(path: String): String {
        return assets.open(path)
            .bufferedReader()
            .use(BufferedReader::readText)
    }
}
package com.alcocontrol.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Character {

    @Expose
    @SerializedName("image")
    lateinit var image: String

    @Expose
    @SerializedName("array")
    lateinit var array: String
}
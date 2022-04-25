package com.alcocontrol.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate

class Profile {

    @Expose
    @SerializedName("name")
    lateinit var name: String

    @Expose
    @SerializedName("weight")
    var weight = 0f

    @Expose
    @SerializedName("birthday")
    lateinit var birthday: LocalDate

    @Expose
    @SerializedName("gender")
    lateinit var gender: Gender

    val hasGender: Boolean
        get() = ::gender.isInitialized
}
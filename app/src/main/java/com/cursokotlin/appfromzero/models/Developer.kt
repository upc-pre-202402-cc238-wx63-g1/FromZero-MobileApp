package com.cursokotlin.appfromzero.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Developer(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "lastName")
    val rating: Float,
    @ColumnInfo(name = "profilePic")
    val profilePic: Int,
    @ColumnInfo(name = "countryFlag")
    val countryFlag: Int,
    @ColumnInfo(name = "summary")
    val summary: String,
    @ColumnInfo(name = "skills")
    val skills: String,
    @ColumnInfo(name = "phone")
    val phone: String? = "999 999 999",
    @ColumnInfo(name = "email")
    val email: String? = "example@gmail.com",
    @ColumnInfo(name = "profileImgUrl")
    val profileImgUrl: String? = "https://i.pinimg.com/474x/c5/43/cd/c543cd798d203442a63ee559dd3e0d7f.jpg"
)
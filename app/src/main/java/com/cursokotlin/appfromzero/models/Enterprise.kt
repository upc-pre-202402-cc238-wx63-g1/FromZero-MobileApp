package com.cursokotlin.appfromzero.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Enterprise (
    @PrimaryKey(autoGenerate = false)
    val id: Long? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "website")
    var website: String,
    @ColumnInfo(name = "pictureUrl")
    var pictureUrl: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "field")
    var field: String,
    @ColumnInfo(name = "socialRazon")
    var socialRazon: String,
    @ColumnInfo(name = "cellphone")
    var cellphone: String
)
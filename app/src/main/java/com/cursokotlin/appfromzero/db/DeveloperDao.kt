package com.cursokotlin.appfromzero.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cursokotlin.appfromzero.models.Developer

@Dao
interface DeveloperDao {
    @Insert
    fun insertOne(developer: Developer)

    @Query("SELECT * FROM Developer WHERE id = :id")
    fun getDeveloperByUserId(id: Long): Developer
}
package com.cursokotlin.appfromzero.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cursokotlin.appfromzero.models.Developer

@Dao
interface DeveloperDao {
    @Insert
    fun insertOne(developer: Developer)

    @Query("SELECT * FROM Developer WHERE id = :userId LIMIT 1")
    fun getDeveloperByUserId(userId: Long): Developer?

    @Query("UPDATE Developer SET skills = :skills, summary = :summary, phone = :phone WHERE id = :userId")
    fun updateDeveloperProfile(userId: Long, skills: String, summary: String, phone: String)

    @Query("UPDATE Developer SET profileImgUrl = :profileImgUrl WHERE id = :userId")
    fun updateProfileImg(userId: Long, profileImgUrl: String)
}
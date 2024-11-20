package com.cursokotlin.appfromzero.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cursokotlin.appfromzero.models.Enterprise

@Dao
interface EnterpriseDao {
    @Insert
    fun insertOne(enterprise: Enterprise)

    @Query("SELECT * FROM Enterprise WHERE id = :userId LIMIT 1")
    fun getEnterpriseByUserId(userId: Long): Enterprise?

    @Query("UPDATE Enterprise SET website = :website, description = :description, field = :field, cellphone = :cellphone WHERE id = :userId")
    fun updateEnterpriseProfile(userId: Long, website: String, description: String, field: String, cellphone: String)

    @Query("UPDATE Enterprise SET pictureUrl = :pictureUrl WHERE id = :userId")
    fun updateProfileImg(userId: Long, pictureUrl: String)
}
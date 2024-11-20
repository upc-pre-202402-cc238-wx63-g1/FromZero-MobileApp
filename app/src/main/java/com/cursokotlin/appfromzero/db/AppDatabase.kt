package com.cursokotlin.appfromzero.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cursokotlin.appfromzero.models.Developer
import com.cursokotlin.appfromzero.models.Enterprise

@Database(entities = [Developer::class, Enterprise::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDeveloperDao(): DeveloperDao
    abstract fun getEnterpriseDao(): EnterpriseDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(context, AppDatabase::class.java, "developer.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}
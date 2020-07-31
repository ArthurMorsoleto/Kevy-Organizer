package com.amb.kevyorganizer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1)
abstract class DataBaseService : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "product.db"

        private var instance: DataBaseService? = null

        private fun create(context: Context): DataBaseService =
            Room.databaseBuilder(
                context, DataBaseService::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()

        fun getInstance(context: Context): DataBaseService =
            (instance ?: create(context)).also { instance = it }
    }

    abstract fun productDao(): ProductDao
}
package com.example.bionime.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Mask::class], version = 1)
abstract class MaskDatabase : RoomDatabase() {
    abstract fun maskDao(): MaskDao

    companion object {
        @Volatile
        private var INSTANCE: MaskDatabase? = null

        fun getDatabase(context: Context): MaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, MaskDatabase::class.java, "mask_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CartItemEntity::class,
        OrderEntity::class,
        SavedDesignEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BabyPrintDatabase : RoomDatabase() {
    abstract fun babyPrintDao(): BabyPrintDao

    companion object {
        @Volatile
        private var INSTANCE: BabyPrintDatabase? = null

        fun getDatabase(context: Context): BabyPrintDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BabyPrintDatabase::class.java,
                    "babyprint_database"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

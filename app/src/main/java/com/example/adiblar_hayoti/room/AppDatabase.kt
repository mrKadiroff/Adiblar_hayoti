package com.example.adiblar_hayoti.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Adib_Entity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun adibDao(): AdibDao


    companion object {
        private var instance:AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context):AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,AppDatabase::class.java,"adib_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

}
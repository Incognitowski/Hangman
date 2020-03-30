package com.incognito.hangman

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlayerEntity::class], version = 1)
open abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext,
            AppDatabase::class.java, "hang-man")
            .allowMainThreadQueries()
            .build()
    })

}

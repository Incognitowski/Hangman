package com.incognito.hangman

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "score") val score: Int?
){

    override fun toString(): String {
        return userName ?: "Sem Nome";
    }

}
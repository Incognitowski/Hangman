package com.incognito.hangman

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAll(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE uid = :uid LIMIT 1")
    fun find(uid : Int): PlayerEntity;

    @Insert
    fun insert(player: PlayerEntity)

    @Delete
    fun delete(player: PlayerEntity)

    @Query("UPDATE players SET score = :score WHERE uid = :uid")
    fun setNewScore(score: Int, uid: Int);
}
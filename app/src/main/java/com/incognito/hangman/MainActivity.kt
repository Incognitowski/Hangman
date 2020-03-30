package com.incognito.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun managePlayers(view: View){
        val intent = Intent(this, ManagePlayersActivity::class.java);
        startActivity(intent);
    }

    fun startOnePlayerGame(view : View){
        val intent = Intent(this, SetupOnePlayerGameActivity::class.java);
        startActivity(intent);
    }

    fun startTwoPlayerGame(view : View){
        val intent = Intent(this, SetupTwoPlayerGameActivity::class.java);
        startActivity(intent);
    }

}

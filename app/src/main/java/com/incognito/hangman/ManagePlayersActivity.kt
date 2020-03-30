package com.incognito.hangman

import android.content.Context
import android.os.Bundle
import android.security.ConfirmationPrompt
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.manage_players.*

class ManagePlayersActivity : AppCompatActivity() {

    lateinit var dbManager : AppDatabase;
    lateinit var playerDao : PlayerDao;
    lateinit var players: List<PlayerEntity>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_players);

        dbManager = AppDatabase.getInstance(this);
        playerDao = dbManager.playerDao();

        updatePlayerList();

    }

    fun closePlayerManager(view: View){
        finish();
    }

    fun updatePlayerList(){
        players = playerDao.getAll();
        playerList.adapter = PlayerListAdapter(players, this);
        playerList.layoutManager = LinearLayoutManager(this);
    }

    fun createPlayer(view: View){

        if(playerNameEditText.text.isBlank()){
            Toast.makeText(this, R.string.invalid_user_name_alert, Toast.LENGTH_SHORT).show();
            return;
        }

        var newPlayer = PlayerEntity(uid = null, userName = playerNameEditText.text.toString(), score = 0);

        playerDao.insert(newPlayer);

        playerNameEditText.text.clear();

        updatePlayerList();

        val inputManager: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

    }

    fun deletePlayer(view: View){

        var playerToDelete = playerDao.find( view.getTag(R.string.btn_delete_player_tag) as Int );

        playerDao.delete(playerToDelete);

        updatePlayerList();

    }

}
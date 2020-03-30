package com.incognito.hangman

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.one_player_game_setup.playerOneSpinner
import kotlinx.android.synthetic.main.two_player_game_setup.*

class SetupTwoPlayerGameActivity : AppCompatActivity() {

    lateinit var dbManager : AppDatabase;
    lateinit var playerDao : PlayerDao;
    lateinit var players: List<PlayerEntity>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.two_player_game_setup);

        dbManager = AppDatabase.getInstance(this);
        playerDao = dbManager.playerDao();
        players = playerDao.getAll();

        if(players.size < 2){
            Toast.makeText(this, "Você não possui jogadores suficientes para iniciar um jogo em dupla. Por favor cadastre mais jogadores para iniciar.", Toast.LENGTH_LONG).show();
            (btnIniciarJogoDupla as Button).isEnabled = false;
            (btnIniciarJogoDupla as Button).isClickable = false;
        }

        initializePlayerSpinner();

    }

    private fun initializePlayerSpinner() {

        val adapter: ArrayAdapter<PlayerEntity> = ArrayAdapter<PlayerEntity>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            players
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playerOneSpinner.adapter = adapter;
        playerTwoSpinner.adapter = adapter;

    }

    fun startGame(view : View){

        val playerOneId = (playerOneSpinner.selectedItem as PlayerEntity).uid;
        val playerTwoId = (playerTwoSpinner.selectedItem as PlayerEntity).uid;

        if(playerOneId == playerTwoId){
            Toast.makeText(this, R.string.two_player_game_same_player_error, Toast.LENGTH_SHORT).show();
            return;
        }

        val intent = Intent(this, TwoPlayerGame::class.java);
        intent.putExtra("com.incognito.hangman.PLAYER_1_ID", playerOneId);
        intent.putExtra("com.incognito.hangman.PLAYER_2_ID", playerTwoId);
        intent.putExtra("com.incognito.hangman.PLAYER_1_SCORE", 0);
        intent.putExtra("com.incognito.hangman.PLAYER_2_SCORE", 0);
        intent.putExtra("com.incognito.hangman.TWO_PLAYER_ROUND", 1);
        startActivity(intent);
        finish();

    }

    fun closeSetup(view : View){
        finish();
    }

}

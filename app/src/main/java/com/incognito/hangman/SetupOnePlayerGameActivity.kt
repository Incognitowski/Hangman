package com.incognito.hangman

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.one_player_game_setup.*

class SetupOnePlayerGameActivity : AppCompatActivity() {

    lateinit var dbManager : AppDatabase;
    lateinit var playerDao : PlayerDao;
    lateinit var players: List<PlayerEntity>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.one_player_game_setup);

        dbManager = AppDatabase.getInstance(this);
        playerDao = dbManager.playerDao();
        players = playerDao.getAll();

        initializePlayerSpinner();

        if(players.isEmpty()){
            Toast.makeText(this, "Você não possui um jogador cadastrado para jogar. Por favor cadastre-se antes de iniciar.", Toast.LENGTH_LONG).show();
            (btnIniciarJogoDupla as Button).isEnabled = false;
            (btnIniciarJogoDupla as Button).isClickable = false;
        }

    }

    private fun initializePlayerSpinner() {

        val adapter: ArrayAdapter<PlayerEntity> = ArrayAdapter<PlayerEntity>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            players
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playerOneSpinner.adapter = adapter;

    }

    fun startGame(view : View){

        val intent = Intent(this, OnePlayerGame::class.java);
        intent.putExtra("com.incognito.hangman.GAME_DIFFICULTY", gameDifficultySpinner.selectedItem.toString());
        intent.putExtra("com.incognito.hangman.PLAYER_1_ID", (playerOneSpinner.selectedItem as PlayerEntity).uid);
        startActivity(intent);
        finish();

    }

    fun closeSetup(view : View){
        finish();
    }

}

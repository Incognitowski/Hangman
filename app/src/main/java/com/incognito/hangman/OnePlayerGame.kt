package com.incognito.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import kotlinx.android.synthetic.main.one_player_game.*
import java.lang.reflect.Array

class OnePlayerGame : AppCompatActivity() {

    lateinit var dbManager: AppDatabase;
    lateinit var playerDao: PlayerDao;
    lateinit var player: PlayerEntity;
    lateinit var difficulty: String;

    var score = 60;
    lateinit var currentWordState: String;
    lateinit var secretWord: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.one_player_game)

        dbManager = AppDatabase.getInstance(this);
        playerDao = dbManager.playerDao();

        player = playerDao.find(intent.getIntExtra("com.incognito.hangman.PLAYER_1_ID", 0));
        difficulty = intent.getStringExtra("com.incognito.hangman.GAME_DIFFICULTY");

        makeGame();

    }

    fun resetGame(view: View) {

        val intent = Intent(this, OnePlayerGame::class.java);
        intent.putExtra("com.incognito.hangman.GAME_DIFFICULTY", difficulty);
        intent.putExtra(
            "com.incognito.hangman.PLAYER_1_ID",
            intent.getIntExtra("com.incognito.hangman.PLAYER_1_ID", 0)
        );
        startActivity(intent);
        finish();

    }

    fun closeGame(view: View) {

        finish();

    }

    private fun makeGame() {

        secretWord = getSecretWord(difficulty);

//        Toast.makeText(this, secretWord, Toast.LENGTH_SHORT).show();

        currentWordState = "".padEnd(secretWord.length, '_');

        currentWordHolder.text = currentWordState;

    }

    private fun getSecretWord(difficulty: String): String {

        lateinit var randomWord: String;

        var easyWords = resources.getStringArray(R.array.easy_words)
        var mediumWords = resources.getStringArray(R.array.medium_words)
        var hardWords = resources.getStringArray(R.array.hard_words)

        when (difficulty) {
            "Fácil" -> randomWord = easyWords.random()
            "Médio" -> randomWord = mediumWords.random()
            "Difícil" -> randomWord = hardWords.random()
        }

        return randomWord;

    }

    fun guessCharacter(view: View) {

        var characterToTest = (view as Button).text[0];

        if (secretWord.contains(characterToTest, true)) {

            var it = 0;

            for (letter in secretWord) {

                if (letter.toUpperCase() == characterToTest) {

                    currentWordState = currentWordState.replaceRange(it, it + 1, letter.toString());
                    currentWordHolder.text = currentWordState;
                }
                it++;
            }

            if (!currentWordState.contains('_')) {
                winGame();
                return;
            }

        } else {
            score = score.minus(10);
            addAnotherPieceToDummy();
        }

        (view as Button).isEnabled = false;
        (view as Button).isClickable = false;

    }

    private fun addAnotherPieceToDummy() {

        when (score) {
            60 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_1);
            }
            50 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_2);
            }
            40 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_3);
            }
            30 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_4);
            }
            20 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_5);
            }
            10 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_6);
            }
            0 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_7);
                loseGame();
            }
        }

    }

    private fun loseGame() {

        val alertDialog = AlertDialog.Builder(this).setTitle("Você perdeu :/")
            .setMessage("A palavra secreta era: " + secretWord + "!, mais sorte da próxima vez, " + player.userName + ".");

        alertDialog.setPositiveButton("Ok :(") { dialog, which ->
            finish();
        }

        alertDialog.show();

    }

    private fun winGame() {

        var newPlayerScore = player.score?.plus(score);

        playerDao.setNewScore(newPlayerScore!!, player.uid!!);

        val alertDialog = AlertDialog.Builder(this).setTitle("Você ganhou!")
            .setMessage("Você acertou a palavra secreta e ganhou " + score.toString() + " pontos. Parabéns " + player.userName + "!");

        alertDialog.setPositiveButton("Ok :)") { dialog, which ->
            finish();
        }

        alertDialog.show();

    }

}

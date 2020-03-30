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
import kotlinx.android.synthetic.main.one_player_game.currentWordHolder
import kotlinx.android.synthetic.main.one_player_game.dummyHolder
import kotlinx.android.synthetic.main.two_player_game.*
import kotlin.random.Random

class TwoPlayerGame : AppCompatActivity() {

    lateinit var dbManager : AppDatabase;
    lateinit var playerDao : PlayerDao;
    lateinit var playerOne: PlayerEntity;
    lateinit var playerTwo: PlayerEntity;
    lateinit var currentPlayer: PlayerEntity;

    private var playerOneScore: Int = 0;
    private var playerTwoScore: Int = 0;
    lateinit var difficulty: String;

    lateinit var currentWordState: String;
    lateinit var secretWord: String;

    private var errors : Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.two_player_game)

        dbManager = AppDatabase.getInstance(this);
        playerDao = dbManager.playerDao();

        playerOne = playerDao.find(intent.getIntExtra("com.incognito.hangman.PLAYER_1_ID", 0));
        playerTwo = playerDao.find(intent.getIntExtra("com.incognito.hangman.PLAYER_2_ID", 0));

        playerOneScore = intent.getIntExtra("com.incognito.hangman.PLAYER_1_SCORE", 0);
        playerTwoScore = intent.getIntExtra("com.incognito.hangman.PLAYER_2_SCORE", 0);

        when(intent.getIntExtra("com.incognito.hangman.TWO_PLAYER_ROUND", 1)){
            1 -> difficulty = "Fácil"
            2 -> difficulty = "Médio"
            3 -> difficulty = "Difícil"
        }

        makeGame();

        randomizeStartingPlayer();

    }

    private fun randomizeStartingPlayer() {

        var randomizer = Random.nextInt(0,100);

        currentPlayer = if (randomizer % 2 == 0) playerOne else playerTwo;

        indicadorRodada.text = "Agora é a vez de " + currentPlayer.userName;

    }

    fun resetGame(view: View) {

        val intent = Intent(this, TwoPlayerGame::class.java);
        intent.putExtra("com.incognito.hangman.PLAYER_1_ID", playerOne.uid);
        intent.putExtra("com.incognito.hangman.PLAYER_2_ID", playerTwo.uid);
        intent.putExtra("com.incognito.hangman.PLAYER_1_SCORE", 0);
        intent.putExtra("com.incognito.hangman.PLAYER_2_SCORE", 0);
        intent.putExtra("com.incognito.hangman.TWO_PLAYER_ROUND", 1);
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

            givePointsToCurrentPlayer();

            for (letter in secretWord) {

                if (letter.toUpperCase() == characterToTest) {

                    currentWordState = currentWordState.replaceRange(it, it + 1, letter.toString());
                    currentWordHolder.text = currentWordState;

                }
                it++;
            }

            if (!currentWordState.contains('_')) {
                resolveGameProgress();
                return;
            }

        } else {
            errors = errors.plus(1);
            switchPlayerRound();
            addAnotherPieceToDummy();
        }

        (view as Button).isEnabled = false;
        (view as Button).isClickable = false;

    }

    private fun givePointsToCurrentPlayer() {

        if(currentPlayer.uid == playerOne.uid){
            playerOneScore = playerOneScore.plus(10);
        }else{
            playerTwoScore = playerTwoScore.plus(10);
        }

    }

    private fun switchPlayerRound() {

        if(currentPlayer.uid == playerOne.uid){
            currentPlayer = playerTwo;
        }else{
            currentPlayer = playerOne;
        }

        indicadorRodada.text = "Agora é a vez de " + currentPlayer.userName;

    }

    private fun resolveGameProgress() {

        when(intent.getIntExtra("com.incognito.hangman.TWO_PLAYER_ROUND", 1)){
            1, 2 -> {

                var nextRound = intent.getIntExtra("com.incognito.hangman.TWO_PLAYER_ROUND", 1) + 1;

                val intent = Intent(this, TwoPlayerGame::class.java);
                intent.putExtra("com.incognito.hangman.PLAYER_1_ID", playerOne.uid);
                intent.putExtra("com.incognito.hangman.PLAYER_2_ID", playerTwo.uid);
                intent.putExtra("com.incognito.hangman.PLAYER_1_SCORE", playerOneScore);
                intent.putExtra("com.incognito.hangman.PLAYER_2_SCORE", playerTwoScore);
                intent.putExtra("com.incognito.hangman.TWO_PLAYER_ROUND", nextRound);
                startActivity(intent);
                finish();

            }
            3 -> {
                winGame();
            }
        }

    }

    private fun addAnotherPieceToDummy() {

        when (errors) {
            1 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_1);
            }
            2 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_2);
            }
            3 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_3);
            }
            4 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_4);
            }
            5 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_5);
            }
            6 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_6);
            }
            7 -> {
                dummyHolder.setImageResource(R.drawable.ic_crash_dummy_7);
                loseGame();
            }
        }

    }

    private fun loseGame() {

        val alertDialog = AlertDialog.Builder(this).setTitle("Vocês perderam :/")
            .setMessage("A palavra secreta era: " + secretWord + "!, mais sorte da próxima vez, " + playerOne.userName + " e " + playerTwo.userName + ".");

        alertDialog.setPositiveButton("Ok :(") { dialog, which ->
            finish();
        }

        alertDialog.show();

    }

    private fun winGame() {

        var winner : PlayerEntity = playerOne;
        var scoreToAdd : Int = playerOneScore;

        if(playerTwoScore > playerOneScore) {
            winner = playerTwo;
            scoreToAdd = playerTwoScore;
        }

        var newPlayerScore = winner.score?.plus(scoreToAdd);

        playerDao.setNewScore(newPlayerScore!!, winner.uid!!);

        val alertDialog = AlertDialog.Builder(this).setTitle("Você ganhou!")
            .setMessage("Vocês acertaram as três palavras, mas " + winner.userName + " acertou mais vezes, então ganhou " + scoreToAdd.toString() + " pontos. Parabéns!");

        alertDialog.setPositiveButton("Ok :)") { dialog, which ->
            finish();
        }

        alertDialog.show();

    }

}

package com.example.a2in1app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

lateinit var cl_guessThePhraseMain: ConstraintLayout

// create variables
lateinit var tv_phrase: TextView
lateinit var tv_guessed: TextView
lateinit var tv_result: TextView
lateinit var tv_charResult: TextView
lateinit var tv_remaining: TextView
lateinit var IT_userInput: TextInputEditText
lateinit var bt_guessPhrase: Button
lateinit var tv_score: TextView

private lateinit var sharedPreferences: SharedPreferences

val phrase = "to be or not to be"
var hiddenPhrase = "** ** ** *** ** **"
var gameMode = true // true = sentence & false = char

var credit = 10

var gameResult = ""

class guessThePhrase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_the_phrase)

        cl_guessThePhraseMain = findViewById(R.id.cl_guessThePhraseMain)

        // init variables
        cl_guessThePhraseMain = findViewById<ConstraintLayout>(R.id.cl_guessThePhraseMain)
        tv_phrase = findViewById(R.id.tv_phrase)
        tv_guessed = findViewById(R.id.tv_guessed)
        tv_result = findViewById(R.id.tv_result)
        tv_charResult = findViewById(R.id.tv_charResult)
        tv_remaining = findViewById(R.id.tv_remaining)
        IT_userInput = findViewById(R.id.IT_userInput)
        bt_guessPhrase = findViewById(R.id.bt_guessPhrase)
        tv_score = findViewById(R.id.tv_score)
        hiddenPhrase = "** ** ** *** ** **"
        credit = 10
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        IT_userInput.setHint("Guess the full phrase")
        val highestScore = sharedPreferences.getString("score", "").toString()
        if (!highestScore.isNullOrBlank()) {
            tv_score.text = "The highest score $highestScore"
        }
        bt_guessPhrase.setOnClickListener { game() }

    }

    fun game() {
        if (credit <= 1) {
            tv_remaining.text = "zero guesses remaining"
            credit = 10
            gameResult = "You lost!"
            customAlert()
        } else {
            if (IT_userInput.text.toString().isNullOrBlank()) { // empty input
                // create a snack bar
                Snackbar.make(clMain, "Enter a text please..", Snackbar.LENGTH_LONG).show()
            } else {
                if (gameMode) { // full sentence guess
                    if (sentenceCheck()) {
                        gameResult = "You won!"
                        updateScore()
                        customAlert()
                    } else {
                        tv_result.text = "Wrong guess: ${IT_userInput.text.toString()}"
                        updateGameMode()
                        IT_userInput.setText("")
                        credit--
                    }
                } else { // char guess
                    if (charCheck()) {
                        if (hiddenPhrase.contains("*")) {

                        } else {
                            updateScore()
                            gameResult = "You won!"
                            customAlert()
                        }
                    } else {
                        updateGameMode()
                        IT_userInput.setText("")
                        credit--
                    }
                }
            }
            tv_remaining.text = "$credit guesses remaining"
            tv_phrase.text = hiddenPhrase

        }
    }

    fun updateGameMode(): Boolean {
        if (gameMode) {
            IT_userInput.setHint("Guess a char")
            gameMode = false
            IT_userInput.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(1)))
            return true
        } else {
            IT_userInput.setHint("Guess the full phrase")
            gameMode = true
            IT_userInput.setFilters(arrayOf<InputFilter>())
            return false
        }
    }

    fun sentenceCheck(): Boolean {
        if (IT_userInput.text.toString() == phrase) {
            Log.d("MainActivityGame", "You won!")
            return true
        } else {
            Log.d("MainActivityGame", "You lost!")
            return false
        }
    }

    fun charCheck(): Boolean {
        if (phrase.contains(IT_userInput.text.toString())) {
            var lettersIndex = ArrayList<Int>()
            var counter = 0
            for (i in phrase) {
                if (IT_userInput.text.toString()[0] == i) {
                    lettersIndex.add(counter)
                }
                counter++
            }
            for (i in 0..(lettersIndex.size - 1)) {
                val chars = hiddenPhrase.toCharArray()
                chars[lettersIndex[i]] = IT_userInput.text.toString()[0]
                hiddenPhrase = String(chars)
            }
            tv_charResult.text = "Found ${lettersIndex.size.toInt()} ${IT_userInput.text.toString()[0]}"
            tv_guessed.text = "You guessed ${IT_userInput.text.toString()[0]}"
            Log.d("MainActivityGame", "${IT_userInput.text.toString()} exists!")
            return true
        } else {
            tv_guessed.text = "You guessed ${IT_userInput.text.toString()[0]}"
            Log.d("MainActivityGame", "The char doesn't exist!")
            return false
        }
    }

    private fun customAlert(){
        // first we create a variable to hold an AlertDialog builder
        val dialogBuilder = AlertDialog.Builder(this)

        // here we set the message of our alert dialog
        dialogBuilder.setMessage("Do you want to play again?:")
            // positive button text and action
            .setPositiveButton("yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> finish()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("$gameResult")

        // show alert dialog
        alert.show()
    }

    fun updateScore() {
        // We can save data with the following code
        with(sharedPreferences.edit()) {
            putString("score", (credit).toString())
            apply()
        }
        val highestScore = sharedPreferences.getString("score", "").toString()
        if (!highestScore.isNullOrBlank()) {
            tv_score.text = "The highest score $highestScore"
        }
    }







    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.phrase_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_guessThePhraseNew -> {
                IT_userInput.setText("")
                this.recreate()
                return true
            }
            R.id.mi_numbersGame -> {
                val intent = Intent(this, numbersGame::class.java)
                startActivity(intent)
                return true
            }
            R.id.mi_mainMenu -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
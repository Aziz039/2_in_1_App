package com.example.a2in1app

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

lateinit var cl_numbersGameMain: ConstraintLayout

lateinit var tv_answer: TextView
lateinit var tv_chances: TextView
lateinit var ti_userAnswer: TextInputEditText
lateinit var bt_guess: Button
var chances: Int = 0
var randomNumber: Int = 0
var gameResultNumber: String = ""

class numbersGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numbers_game)

        cl_numbersGameMain = findViewById(R.id.cl_numbersGameMain)

        tv_answer = findViewById(R.id.tv_answer)
        tv_chances = findViewById(R.id.tv_chances)
        ti_userAnswer = findViewById(R.id.ti_userAnswer)
        bt_guess = findViewById(R.id.bt_guess)
        chances = 3
        randomNumber = Random.nextInt(11)
        gameResultNumber = ""
        bt_guess.setOnClickListener { game() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.numbers_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_numbersGameNew -> {
                ti_userAnswer.setText("")
                this.recreate()
                return true
            }
            R.id.mi_guessThePhrase -> {
                val intent = Intent(this, guessThePhrase::class.java)
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

    fun game() {
        if (chances > 0) {
            Log.d("MainActivityGame", "answer is $randomNumber")
            Log.d("MainActivityGame", "answer is ${ti_userAnswer.text.toString().toInt()}")

            if (ti_userAnswer.text.isNullOrEmpty()) {2
                Log.d("MainActivityGame", "Enter a number..")
            } else {
                tv_answer.text = "You guessed ${ti_userAnswer.text}"
                if (ti_userAnswer.text.toString().toInt() == randomNumber) {
                    gameResultNumber = "You won!"
                    customAlert()
                } else {
                    chances--
                    if (chances == 1) {
                        tv_chances.text = "You have $chances guess left!"
                        Log.d("MainActivityGame", "answer is $randomNumber")
                    } else {
                        tv_chances.text = "You have $chances guesses left!"
                    }
                }
            }

        } else {
            tv_chances.text = "You have $chances guess left!"
            gameResultNumber = "You lost the game!"
            customAlert()
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
        alert.setTitle("$gameResultNumber")

        // show alert dialog
        alert.show()
    }
}
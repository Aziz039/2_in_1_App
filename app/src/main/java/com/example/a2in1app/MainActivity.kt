package com.example.a2in1app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

lateinit var clMain: ConstraintLayout
lateinit var bt_numbersGame: Button
lateinit var bt_guessThePhrase: Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clMain = findViewById(R.id.clMain)

        bt_numbersGame = findViewById(R.id.bt_numbersGame)
        bt_guessThePhrase = findViewById(R.id.bt_guessThePhrase)

        bt_numbersGame.setOnClickListener { playNumbersGame() }
        bt_guessThePhrase.setOnClickListener { playGuessGame() }
    }

    fun playNumbersGame() {
        val intent = Intent(this, numbersGame::class.java)
        startActivity(intent)
    }

    fun playGuessGame() {
        val intent = Intent(this, guessThePhrase::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_numbersGame -> {
                val intent = Intent(this, numbersGame::class.java)
                startActivity(intent)
                return true
            }
            R.id.mi_guessThePhrase -> {
                val intent = Intent(this, guessThePhrase::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
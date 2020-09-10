package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModel : ViewModel() {
    init {
        Log.i("GameViewModel", "View Model created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "View Model destored!")
    }

    // The current word
    public var word = ""

    // The current score
    public var score = 0

    // The list of words - the front of the list is the next word to guess
    public lateinit var wordList: MutableList<String>

    /**
     * Resets the list of words and randomizes the order
     */
    public fun resetList() {

        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    public fun onSkip() {
        score--
        nextWord()
    }

    public fun onCorrect() {
        score++
        nextWord()
    }


    /**
     * Moves to the next word in the list
     */
    public fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            word = ""

        } else {
            word = wordList.removeAt(0)
        }
    }


}
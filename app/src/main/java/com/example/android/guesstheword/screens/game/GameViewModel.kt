package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModel : ViewModel() {
    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word


    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

val isRealtime: LiveData<Int>
    get() = _score

    init {
        Log.i("GameViewModel", "View Model created!")
        //Live Data is always nullable type. so need to initialize it for null safety
        _score.value = 0
        _word.value = ""

        resetList()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "View Model destored!")
    }

        // The list of words - the front of the list is the next word to guess
    lateinit var wordList: MutableList<String>

    /**
     * Resets the list of words and randomizes the order
     */
    fun resetList() {

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

    fun onSkip() {
        _score.value = (_score.value?:0).minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value?:0).plus(1)
        nextWord()
    }


    /**
     * Moves to the next word in the list
     */
    fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            _word.value = ""

        } else {
            _word.value = wordList.removeAt(0)
        }
    }


}
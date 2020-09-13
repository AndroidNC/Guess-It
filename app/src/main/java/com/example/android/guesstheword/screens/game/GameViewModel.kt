package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel :  ViewModel() {

    companion object  {
        //This represents different important times
        //This is when the game is over
        const val DONE = 0L
        //this is number of milliseconds in a second
        const val ONE_SECOND = 1000L
        //This i
        const val COUNTDOWN_TIME = 20000L
    }

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    private var _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
        get() = _currentTime

    private lateinit var timer: CountDownTimer

    init {
        Log.i("GameViewModel", "View Model created!")
        //Live Data is always nullable type. so need to initialize it for null safety
        _score.value = 0
        _word.value = ""
        _eventGameFinished.value = false

         timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
               _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _eventGameFinished.value = true
            }
        }

        timer.start()

        resetList()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.i("GameViewModel", "View Model destored!")
    }

        // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

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

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty())
            resetList()

        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
        _score.value = (_score.value?:0).minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value?:0).plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }

}
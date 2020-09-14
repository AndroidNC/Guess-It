package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
// The first value indicates the number of milliseconds to wait before turning the vibrator on.
// The next value indicates the number of milliseconds for which to keep the vibrator on before turning it off.
// Subsequent values alternate between durations in milliseconds to turn the vibrator off or to turn the vibrator on.
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 200, 100, 200, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 1000)
private val NO_BUZZ_PATTERN = longArrayOf(0)



class GameViewModel :  ViewModel() {

    // These are the three different types of buzzing in the game. Buzz pattern is the number of
    // milliseconds each interval of buzzing and non-buzzing takes.
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object  {
        //This represents different important times in the game, such as game length.

        //This is when the game is over
        const val DONE = 0L

        //this is number of milliseconds in a second
        const val ONE_SECOND = 1000L

        //This is the time when the phone will start buzzing each second
        const val COUNTDOWN_PANIC_SECONDS = 5L

        //This is the tatal time of the game
        const val COUNTDOWN_TIME = 20000L
    }

    private lateinit var timer: CountDownTimer

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    //Current Time
    private var _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
        get() = _currentTime

    //The String version of hte current time
    val currentTimeString = Transformations.map(currentTime, {time ->
        DateUtils.formatElapsedTime(time)
    })

    //Event which triggers the end of the game
    private var _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished


    private var _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz : LiveData<BuzzType>
        get() = _eventBuzz


    init {
        Log.i("GameViewModel", "View Model created!")
        //Live Data is always nullable type. so need to initialize it for null safety
        _score.value = 0
        _word.value = ""
        _eventGameFinished.value = false
        _eventBuzz.value = BuzzType.NO_BUZZ

         timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
               _currentTime.value = millisUntilFinished / ONE_SECOND

                if(_currentTime.value!! < COUNTDOWN_PANIC_SECONDS)
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
            }

            override fun onFinish() {
                _eventGameFinished.value = true
                _eventBuzz.value = BuzzType.GAME_OVER
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
        _eventBuzz.value = BuzzType.CORRECT

        _score.value = (_score.value?:0).plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

}
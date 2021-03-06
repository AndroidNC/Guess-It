/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import kotlin.concurrent.timer

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var gameViewModel : GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.i("GameFragment", "called ViewModelProviders.of")

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)


        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        binding.gameViewModel = gameViewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this //this allows the live data to be observed directly from the view


        gameViewModel.eventGameFinished.observe(viewLifecycleOwner, Observer { eventGameFinished ->
            if(eventGameFinished)
                gameOver()
        })

        gameViewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
           if(buzzType != GameViewModel.BuzzType.NO_BUZZ) {
               buzz(buzzType.pattern)
               gameViewModel.onBuzzComplete()
           }
        })

        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameOver() {
        val action = GameFragmentDirections.actionGameToScore(gameViewModel.score.value?:0)
        findNavController(this).navigate(action)

        gameViewModel.onGameFinishComplete()
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.getSystemService(Vibrator::class.java)
        } else {
            TODO("VERSION.SDK_INT < M")
        }

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}

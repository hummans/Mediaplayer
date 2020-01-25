package com.mediaplayer.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.player.PlayerController
import com.mediaplayer.repository.Song
import javax.inject.Inject

@FragmentScope
class HomeViewModel @Inject constructor(repository: SongsRepository, private val playerController: PlayerController) : ViewModel() {
    private val _songsLiveData = MutableLiveData<List<Song>>()
    val songsLiveData = _songsLiveData

    init {
        try {
            val songs = repository.getSongs()
            _songsLiveData.value = songs
            Log.d(TAG, "NEw HOme View model")
        } catch (e: Exception) {
            print(e)
        }


    }


    fun play(song: Song) {
        playerController.play(song)
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
package com.mediaplayer.com;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

public class Music implements MediaPlayer.EventListener{

	static MediaPlayer mediaPlayer;
	private LibVLC mLibVLC;
	ArrayList<String> options = new ArrayList<>();
	Media mCurrentMedia;
	MusicHelperInterface musicHelperInterface;

	public Music(Context context, MusicHelperInterface musicHelperInterface)  {
		mediaPlayer = null;
		mLibVLC = new LibVLC(context, options);
		mediaPlayer = new MediaPlayer(mLibVLC);
		this.musicHelperInterface = musicHelperInterface;
		mediaPlayer.setEventListener(this);
	}

	public void setStreamPath(String path) throws  RuntimeException{
		try {
			mCurrentMedia = new Media(mLibVLC, path);
			mediaPlayer.setMedia(mCurrentMedia);
		} catch (Exception ex) {
			ex.printStackTrace();
			mediaPlayer.release();
			throw new RuntimeException("Couldn't load music, uh oh!");
		}
	}
	public void setStreamUri(Uri path) throws  RuntimeException{
		try {
			if(mCurrentMedia != null) mCurrentMedia.release();

			mCurrentMedia = new Media(mLibVLC, path);
			mediaPlayer.setMedia(mCurrentMedia);
		} catch (Exception ex) {
			ex.printStackTrace();
			mediaPlayer.release();
			throw new RuntimeException("Couldn't load music, uh oh!");
		}
	}

	public void play() {
		mediaPlayer.play();
	}

	public void resume(){
		mediaPlayer.play();
	}
	public void reset() {
		if(mediaPlayer!=null){
			mediaPlayer.stop();
		}
		mCurrentMedia = null;
	}
	public void stop(){
		mCurrentMedia.release();
		mediaPlayer.stop();
	}

	public int getDuration() {
		if(mediaPlayer!=null){
			return  (int)mediaPlayer.getMedia().getDuration();
		}
		return 0;
	}
	public int getCurrentPosition() {
		if(mediaPlayer!=null){
			return  (int)mediaPlayer.getTime();
		}
		return 0;
	}

	public void release(){
		mediaPlayer.release();
	}

	public void switchTracks() {
		mediaPlayer.setTime(0);
		mediaPlayer.pause();
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}


	public void setVolume(int volume) {
		mediaPlayer.setVolume(volume);
	}

	public void dispose() {
		if (mediaPlayer.isPlaying()) {
			stop();
		}
		mediaPlayer.release();
	}

	public void seekTo(int position) {
		mediaPlayer.setTime(position);
	}

	@Override
	public void onEvent(MediaPlayer.Event event) {

		switch (event.type){
			case MediaPlayer.Event.Opening:
				Log.d("VLC", "Opening" + isPlaying());
				musicHelperInterface.onStarted();
				break;
			case MediaPlayer.Event.Buffering:
				Log.d("VLC", "Buffer" +  + event.getBuffering());
				break;
			case MediaPlayer.Event.Playing:
				Log.d("VLC", "Playing");
				break;
			case MediaPlayer.Event.Paused:
				Log.d("VLC", "Paused");
				break;
			case MediaPlayer.Event.Stopped:
				Log.d("VLC", "Stopped :: " + mediaPlayer.isPlaying());
				break;
			case MediaPlayer.Event.PositionChanged:
				Log.d("VLC", "Position Changed");
				break;
			case MediaPlayer.Event.TimeChanged:
				Log.d("VLC", "Time Changed" );
				musicHelperInterface.timeChange(getDuration(), (int)event.getTimeChanged());
				break;
			case MediaPlayer.Event.EndReached:
				Log.d("VLC", "End Reched:: " + mediaPlayer.isPlaying());
				musicHelperInterface.onComplete(getDuration());
				break;
		}
	}


	public interface MusicHelperInterface {
		void onStarted();
		void onComplete(int duration);
		void timeChange(int duration, int current);
	}
}
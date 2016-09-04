package com.game.ian.test;

import java.io.File;
import java.net.URI;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
	static class SoundSample implements Cloneable {
		public String Path;
		public MediaPlayer Player;
		public Media Media;

		public SoundSample(String path) {
			this.Path = path;

			File file = new File("res\\Music\\explodeEffect.mp3");
			this.Media = new Media(file.toURI().toString());
			this.Player = new MediaPlayer(this.Media);
		}

		protected Object clone() throws CloneNotSupportedException {
			this.Player = new MediaPlayer(this.Media);
			return super.clone();
		}
	}

	private static MediaPlayer _player_bg = null;
	private static SoundManager.SoundSample _player_explosion = null;
	private static long _last_play_explosion_tick = 0L;

	public static void initialSound() {
		new JFXPanel();

		_player_explosion = new SoundManager.SoundSample("res\\Music\\explodeEffect.mp3");

		playBGM();
	}

	private static void playBGM() {
		try {
			File file = new File("res\\Music\\bgMusic.mp3");
			Media hit = new Media(file.toURI().toString());
			_player_bg = new MediaPlayer(hit);
			_player_bg.setCycleCount(-1);
			_player_bg.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playExplosion() {
		if (System.currentTimeMillis() - _last_play_explosion_tick < 100L) {
			return;
		}
		try {
			SoundManager.SoundSample sound = (SoundManager.SoundSample) _player_explosion.clone();
			sound.Player.setOnEndOfMedia(new Runnable() {
				public void run() {
					sound.Player.dispose();
				}
			});
			sound.Player.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
		_last_play_explosion_tick = System.currentTimeMillis();
	}
	
	public static void Mute() {
		try {
			if(!_player_bg.isMute()){
				_player_bg.setMute(true);
			}else{
				_player_bg.setMute(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package com.game.ian.util;

import java.io.File;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class SoundManager {
	static class SoundSample implements Cloneable {
		public String Path;
		public File file;
		public BasicPlayer player;

		public SoundSample(String path) {
			this.Path = path;

			file = new File("res\\Music\\explodeEffect.mp3");
			this.player = new BasicPlayer();
			try {
				this.player.open(file);
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}

		protected Object clone() throws CloneNotSupportedException {
			this.player = new BasicPlayer();
			try {
				this.player.open(file);
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
			return super.clone();
		}
	}

	private static BasicPlayer _player_bg = null;
	private static SoundManager.SoundSample _player_explosion = null;
	private static long _last_play_explosion_tick = 0L;

	public static void initialSound() {

		_player_explosion = new SoundManager.SoundSample("res\\Music\\explodeEffect.mp3");

		playBGM();
	}

	private static void playBGM() {
		try {
			File file = new File("res\\Music\\bgMusic.mp3");
			_player_bg = new BasicPlayer();
			_player_bg.open(file);
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
			System.out.println(sound.player.getStatus());
			sound.player.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
		_last_play_explosion_tick = System.currentTimeMillis();
	}


}

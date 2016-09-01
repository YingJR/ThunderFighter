package com.game.ian;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by matt1201 on 2016/8/7.
 */
public class GameCanvas extends Canvas {
	public void onDraw(BufferedImage image) {
		Graphics graphics = getGraphics();

		if (graphics != null) {
			graphics.drawImage(image, 0, 0, null);
		}
	}

	public void addKeyListener(MainScene _main_scene) {
		requestFocus();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				_main_scene.keyPressed(e);
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				_main_scene.keyReleased(e);
			}
			
		});
	}
}

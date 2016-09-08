package com.game.ian.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.game.ian.MainScene;
import com.game.ian.Sprite;

public class Label extends Sprite {
	public static final int FONT_SIZE = 15;
	private String _text;

	public Label(MainScene scene, int width, int height) {
		this(scene, null, width, height);

		this._img = new BufferedImage(width, height, 2);
		this._img.createGraphics();
	}

	public Label(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
	}

	public String get_text() {
		return this._text;
	}

	public void set_text(String text) {
		this._text = text;
		Graphics2D graphics = (Graphics2D) this._img.getGraphics();

		graphics.setBackground(new Color(0, 0, 0, 0));
		graphics.clearRect(0, 0, get_width(), get_height());

		graphics.setColor(Color.white);

		graphics.setFont(new Font("Arial Black", 0, FONT_SIZE));
		graphics.drawString(this._text, 0, FONT_SIZE);
	}

	public BufferedImage getImg() {
		return this._img;
	}

}

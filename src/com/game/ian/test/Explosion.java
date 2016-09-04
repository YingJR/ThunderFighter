package com.game.ian.test;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.game.ian.Animation;
import com.game.ian.MainScene;
import com.game.ian.Sprite;

public class Explosion extends Animation implements Cloneable {
	private static Explosion _sample = null;

	public static Explosion newInstance(MainScene scene) {
		if (_sample == null) {
			_sample = new Explosion(scene, null, "res\\explosion.png", 100, 100, 33);
		}
		try {
			return (Explosion) _sample.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Explosion newInstance(MainScene scene,Animation.StatusListener listener) {
			_sample = new Explosion(scene, listener, "res\\explosion.png", 100, 100, 33);
		try {
			return (Explosion) _sample.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	private Explosion(MainScene scene, Animation.StatusListener listener, String img_path, int width, int height,
			int frame_count) {
		super(scene, listener, img_path, width, height, frame_count);

		this._frames.clear();
		this._frame_count = frame_count;

		int row_element_count = 15;
		int column_element_count = 3;
		try {
			BufferedImage stream = ImageIO.read(new File(img_path));
			int frame_width = stream.getWidth() / row_element_count;
			int frame_height = stream.getHeight() / column_element_count;

			int left = 0;
			int top = 0;
			for (int i = 0; i < frame_count; i++) {
				if ((i != 0) && (i % row_element_count == 0)) {
					top += frame_height;
					left = 0;
				}
				BufferedImage img_frame = cropImage(stream, new Rectangle(left, top, frame_width, frame_height));
				left += frame_width;

				this._frames.add(new Sprite(scene, img_frame, width, height));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

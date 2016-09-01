package com.game.ian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by matt1201 on 2016/8/7.
 */
public class main extends Canvas {
	public static final int WINDOWS_WIDTH = 431;
	public static final int WINDOWS_HEIGHT = 768;

	private static Frame _frame = new Frame("exercise");
	private static GameCanvas _canvas = new GameCanvas();
	private static BufferedImage _render_image = new BufferedImage(WINDOWS_WIDTH, WINDOWS_HEIGHT,
			BufferedImage.TYPE_INT_ARGB);
	private static Graphics _image_graphics = _render_image.createGraphics();

	private static List<Sprite> _images = new LinkedList<>();
	private static MainScene _main_scene = new MainScene();

	public static void main(String[] args) {
		int borderWidth_bar = 40;
		int borderWidth_side = 10;

		_frame.setSize(WINDOWS_WIDTH + borderWidth_side, WINDOWS_HEIGHT + borderWidth_bar);
		_frame.add(_canvas, BorderLayout.CENTER);
		_frame.setVisible(true);
		_frame.setResizable(false);
		_frame.setBounds(800, 200, WINDOWS_WIDTH + borderWidth_side, WINDOWS_HEIGHT + borderWidth_bar);

		_canvas.addKeyListener(_main_scene);

		_frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				int i = JOptionPane.showConfirmDialog(null, "exit application?");
				if (i == 0)
					System.exit(0);
			}
		});

		onUpdate();
	}

	private static void onUpdate() {

		int frames = 0;
		long timer = System.currentTimeMillis();

		while (true) {

			// 場景更新
			_main_scene.update();
			// 渲染
			onRender();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer = System.currentTimeMillis();
				System.out.println("FPS :" + frames);
				frames = 0;
			}
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void clearSprite() {
		_images.clear();
	}

	public static void addSprite(Sprite image) {
		_images.add(image);
	}

	private static void onRender() {
		_image_graphics.setColor(new Color(126, 196, 255));
		_image_graphics.clearRect(0, 0, WINDOWS_WIDTH, WINDOWS_HEIGHT);

		for (int i = 0; i < _images.size(); i++) {
			int offset_x = _images.get(i).get_width() / 2;
			int offset_y = _images.get(i).get_height() / 2;
			_image_graphics.drawImage(_images.get(i).getImg(), _images.get(i).get_x() - offset_x,
					_images.get(i).get_y() - offset_y, _images.get(i).get_width(), _images.get(i).get_height(), null);
		}

		_canvas.onDraw(_render_image);
	}

}

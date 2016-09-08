package com.game.ian.util;

import java.awt.image.BufferedImage;
import com.game.ian.MainScene;
import com.game.ian.Sprite;

public class HpController extends Sprite implements Cloneable {

	public static final int WIDTH = 60;
	public static final int HEIGHT = 6;
	private BufferedImage _render_image = new BufferedImage(60, 6, 2);
	private Sprite _sprite_bg = null;
	private Sprite _sprite_hp;
	private int _hp = 100;

	public int get_hp() {
		return this._hp;
	}

	public BufferedImage getImg() {
		return this._render_image;
	}

	private void updateImage() {
		this._render_image.getGraphics().drawImage(this._sprite_bg.getImg(), 0, 0, this._sprite_bg.get_width(),
				this._sprite_bg.get_height(), null);
		this._render_image.getGraphics().drawImage(this._sprite_hp.getImg(), 0, 0,
				(int) (this._sprite_hp.get_width() * (get_hp() / 100.0F)), this._sprite_hp.get_height(), null);
	}
	
	public HpController(MainScene scene) {
		super(scene, "res\\hp_bg.png", 60, 6);

		this._render_image.createGraphics();

		this._sprite_bg = new Sprite(scene, "res\\hp_bg.png", 60, 6);
		this._sprite_hp = new Sprite(scene, "res\\hp_value.png", 60, 6);

		updateImage();
	}

	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void damage(int value) {
		this._hp -= value;
		updateImage();
	}

	public void reset() {
		this._hp = 100;
		updateImage();
	}

}

package com.game.ian.model;

import com.game.ian.MainScene;
import com.game.ian.Sprite;

public class Bullet2 extends Bullet {

	public static enum Type {
		Fighter(0), Enemy(1);

		private int _value = -1;

		private Type(int value) {
			this._value = value;
		}
	}

	private int speed;
	private Type _type;

	private int x;
	private int y;

	private boolean _is_exist = true;

	public boolean get_is_exist() {
		return this._is_exist;
	}

	public Bullet2(MainScene scene, String img_path, int width, int height,int speed) {
		super(scene, img_path, width, height);
		this.speed =speed;
	}

	public Bullet2(MainScene scene, String img_path, int width, int height, int get_x, int get_y, Type type) {
		super(scene, img_path, width, height);
		setPosition(get_x, get_y);
		this._type= type;
	}

	// 更新狀態
	public void update() {
		this.x = this.get_x();
		this.y = this.get_y();
		move();
	}

	// 移動
	private void move() {	
		y += speed;
		setPosition(x, y);
		if (x - get_width() / 2 < this._scene.get_rect().left) {
			this._is_exist = false;
		}
		if (x + get_width() / 2 > this._scene.get_rect().right) {
			this._is_exist = false;
		}
		if (y - get_height() / 2 < this._scene.get_rect().top) {
			this._is_exist = false;
		}
		if (y + get_height() / 2 > this._scene.get_rect().bottom) {
			this._is_exist = false;
		}
	}
}

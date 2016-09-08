package com.game.ian.util;

import java.awt.Point;

import com.game.ian.MainScene;
import com.game.ian.model.Bullet;

public class FireController {
	public static final int FIRE_INTERVAL = 100;

	public static enum Type {
		Fighter, Enemy;

		private Type() {
		}
	}

	private long _last_fire_tick = 0L;
	private FireController.Type _type;

	public FireController.Type get_type() {
		return this._type;
	}

	public Bullet fire(MainScene scene, FireController.Type type, MoveDirection direction, Point position,
			String res_path, int w, int h, int speed) {
		if (System.currentTimeMillis() - this._last_fire_tick < FIRE_INTERVAL) {
			return null;
		}
		this._type = type;
		Bullet b;

		if(type == Type.Fighter){
			b = new Bullet(scene, res_path, w, h,position.x,position.y,Bullet.Type.Fighter);
		}else{
			b = new Bullet(scene, res_path, w, h,position.x,position.y,Bullet.Type.Enemy);
		}
//			b = new Bullet(scene, res_path, w, h,position.x,position.y);
		b.setPosition(position.x, position.y);

		this._last_fire_tick = System.currentTimeMillis();
		return (Bullet)b;
	}
}

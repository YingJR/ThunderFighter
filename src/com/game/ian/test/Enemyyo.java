package com.game.ian.test;

import java.awt.Insets;
import java.awt.Point;

import com.game.ian.Animation;
import com.game.ian.Animation.StatusListener;
import com.game.ian.MainScene;
import com.game.ian.Sprite;
import com.game.ian.model.Bullet;

public class Enemyyo extends Sprite {
	// 出生間隔
	public static final int SPAWN_INTERVAL = 200;
	private static final double _fire_probability = 0.3D;

	static class EnemyProperties {
		public String Path = "";
		public int Width = 0;
		public int Height = 0;
		public int Score = 0;

		public EnemyProperties(String path, int w, int h, int score) {
			this.Path = path;
			this.Width = w;
			this.Height = h;
			this.Score = score;
		}
	}

	public static Enemyyo.EnemyProperties[] ENEMY_IMAGES = { 
			new Enemyyo.EnemyProperties("res\\enemy1.png", 80, 44, 50),
			new Enemyyo.EnemyProperties("res\\enemy2.png", 65, 40, 40),
			new Enemyyo.EnemyProperties("res\\enemy3.png", 45, 25, 15),
			new Enemyyo.EnemyProperties("res\\enemy4.png", 64, 30, 20) };
	private static long _last_spawn_tick = 0L;
	private boolean _is_exist = true;
	private long _last_turn_direction_tick = 0L;
	private MoveDirection _current_direction = MoveDirection.Down;
	private FireController _fireController = new FireController();
	private int _score = 0;

	public boolean get_is_exist() {
		return this._is_exist;
	}

	public int get_score() {
		return this._score;
	}

	public static Enemyyo Spawn(MainScene scene) {
		if (System.currentTimeMillis() - _last_spawn_tick < 200L) {
			return null;
		}
		int index = (int) (Math.random() * ENEMY_IMAGES.length);

		Enemyyo.EnemyProperties properties = ENEMY_IMAGES[index];

		Enemyyo enemy = new Enemyyo(scene, properties.Path, properties.Width, properties.Height, properties.Score);

		int bound_left = scene.get_rect().left + enemy.get_width() / 2;
		int bound_right = scene.get_rect().right - enemy.get_width() / 2;

		int x = (int) (Math.random() * (bound_right - bound_left) + bound_left);
		int y = scene.get_rect().top - enemy.get_height();

		enemy.setPosition(x, y);
		
		
		_last_spawn_tick = System.currentTimeMillis();

		return enemy;
	}

	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		if (x - get_width() / 2 < this._scene.get_rect().left - 80) {
			this._is_exist = false;
		}
		if (x + get_width() / 2 > this._scene.get_rect().right + 80) {
			this._is_exist = false;
		}
		if (y - get_height() / 2 < this._scene.get_rect().top - 80) {
			this._is_exist = false;
		}
		if (y + get_height() / 2 > this._scene.get_rect().bottom + 80) {
			this._is_exist = false;
		}		
	}

	protected Enemyyo(MainScene scene, String img_path, int width, int height, int score) {
		super(scene, img_path, width, height);
		this._score = score;
	}

	public void update() {
		if (System.currentTimeMillis() - this._last_turn_direction_tick > Math.random() * 700.0D + 300.0D) {
			MoveDirection next_directoin = MoveDirection.getRandomDirection();
			if (next_directoin != MoveDirection.None) {
				this._current_direction = next_directoin;
			}
			this._last_turn_direction_tick = System.currentTimeMillis();
		}
		switch (this._current_direction.ordinal()) {
		case 1:
			setPosition(this._x, this._y + 3);
			break;
		case 2:
			setPosition(this._x - 1, this._y);
			break;
		case 3:
			setPosition(this._x + 1, this._y);
			break;
		}

	}

	public Bullet checkFire() {
		if (Math.random() * 100.0D > _fire_probability) {
			return null;
		}
		Point position = get_position();
		position.y += get_height() / 2;

		return this._fireController.fire(this._scene, FireController.Type.Enemy, MoveDirection.Down, position,
				"res\\bullet_enemy.png", 20, 20, 3);
	}
	
	public Explosion DoCollision(Animation.StatusListener listener){
	
		Explosion e = Explosion.newInstance(_scene,listener);
		e.setPosition(this.get_x(), this.get_y());

		return e;
	}
	
}

package com.game.ian;

import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.game.ian.model.Bullet;
import com.game.ian.model.Bullet.Type;
import com.game.ian.model.Fighter;
import com.game.ian.test.Enemy;
import com.game.ian.test.Enemyyo;

/**
 * Created by Matt on 2016/8/8.
 */
public class MainScene {
	class RenderLayer {
		public Sprite Sprite;
		public int Layer;

		public RenderLayer(Sprite sprite, int layer) {
			Sprite = sprite;
			Layer = layer;
		}

		@Override
		public int hashCode() {
			return Sprite.hashCode();
		}
	}

	public static final int Velocity_Fighter = 4;
	public static final int Velocity_Bullet = 7;
	public static final int Velocity_Bullet_Enemy = 3;
	public static final int Velocity_Enemy = 1;

	private List<RenderLayer> _render_objects = new CopyOnWriteArrayList<>();
	private Insets _rect = null;

	public Insets get_rect() {
		return _rect;
	}

	private Fighter _fighter;
	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	private LinkedList<Enemyyo> enemys = new LinkedList<Enemyyo>();
	private Enemy _enemy5;
	private Enemyyo _enemy;
	private Sprite _sprite_bg;
	private Sprite _sprite_bgB;

	public MainScene() {
		_rect = new Insets(0, 0, main.WINDOWS_HEIGHT, main.WINDOWS_WIDTH);

		_sprite_bg = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
		_sprite_bg.setPosition(main.WINDOWS_WIDTH / 2, main.WINDOWS_HEIGHT / 2);
		addToScene(_sprite_bg);

		_sprite_bgB = new Sprite(this, "res\\bgB.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
		_sprite_bgB.setPosition(main.WINDOWS_WIDTH / 2, -1 * main.WINDOWS_HEIGHT / 2);
		addToScene(_sprite_bgB);

//		int left = 80;
//		Sprite _enemy1 = new Sprite(this, "res\\enemy1.png", 80, 80);
//		_enemy1.setPosition(left, main.WINDOWS_HEIGHT / 2);
//		addToScene(_enemy1);
//		left += 80;
//
//		Sprite _enemy2 = new Sprite(this, "res\\enemy2.png", 80, 80);
//		_enemy2.setPosition(left, main.WINDOWS_HEIGHT / 2);
//		addToScene(_enemy2);
//		left += 80;
//
//		Sprite _enemy3 = new Sprite(this, "res\\enemy3.png", 80, 80);
//		_enemy3.setPosition(left, main.WINDOWS_HEIGHT / 2);
//		addToScene(_enemy3);
//		left += 80;
//
//		Sprite _enemy4 = new Sprite(this, "res\\enemy4.png", 80, 80);
//		_enemy4.setPosition(left, main.WINDOWS_HEIGHT / 2);
//		addToScene(_enemy4);
//		left += 80;
//
//		Sprite _bullet = new Bullet(this, "res\\bullet.png", 16, 20);
//		_bullet.setPosition(left, main.WINDOWS_HEIGHT / 2);
//		addToScene(_bullet);

		_fighter = new Fighter(this, "res\\fighter.png", 90, 60, 3);
		SpawnFighter();
		addToScene(_fighter);

//		_enemy5 = new Enemy(this, "res\\enemy4.png", 80, 90);
//		_enemy5.setPosition(100, 100);
//		addToScene(_enemy5);
		
//		_enemy= Enemyyo.Spawn(this);
//		addToScene(_enemy);
		
	}

	// 重置飛機位置
	private void SpawnFighter() {
		_fighter.setPosition(main.WINDOWS_WIDTH / 2, main.WINDOWS_HEIGHT - 60);
	}

	// 更新畫面
	private void updateFrame() {
		main.clearSprite();

		List<RenderLayer> render_objs = _render_objects;

		Collections.sort(render_objs, new Comparator<RenderLayer>() {
			public int compare(RenderLayer o1, RenderLayer o2) {
				return o1.Layer - o2.Layer;
			}
		});

		for (RenderLayer renderLayer : render_objs)
			main.addSprite(renderLayer.Sprite);
	}

	// 更新
	public void update() {
		updateFrame();

		_fighter.update();
//		_enemy5.update();

		//如果生成不為null加入場景
		if((_enemy=Enemyyo.Spawn(this))!=null){
			enemys.add(_enemy);
			addToScene(_enemy,4);
		}
		
		for (int i = 0; i < enemys.size(); i++) {
			Enemyyo enemy = enemys.get(i);
			// 如果超出畫面
			if (!enemy.get_is_exist()) {
				enemys.remove(enemy);
				removeFromScene(enemy);
			}
			enemy.update();
			Bullet b = enemy.checkFire();

			if(b!=null){
//				System.out.println(b);
				addToScene(b, 4);
				bullets.add(b);
			}
		}
		
		
		
		// 滾動背景
		updateBg();
		//TODO
		System.currentTimeMillis();

		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			// 如果超出畫面
			if (!bullet.get_is_exist()) {
				bullets.remove(bullet);
				removeFromScene(bullet);
			}
			bullet.update();
		}

	}

	// 移除場景物件
	private void removeFromScene(Sprite sprite) {
		for (int i = 0; i < _render_objects.size(); i++)
			if (_render_objects.get(i).hashCode() == sprite.hashCode())
				_render_objects.remove(i);
	}

	// 加入場景
	private void addToScene(Sprite sprite) {
		addToScene(sprite, 0);
	}

	// 指定加入層
	private void addToScene(Sprite sprite, int layer) {
		_render_objects.add(new RenderLayer(sprite, layer));
	}

	// 滾動背景
	private void updateBg() {
		final int BG_SPEED = 2;
		if (_sprite_bg != null && _sprite_bgB != null) {
			int A_x = _sprite_bg.get_x();
			int A_y = _sprite_bg.get_y();
			int B_y = _sprite_bgB.get_y();

			_sprite_bg.setPosition(A_x, A_y + BG_SPEED);
			_sprite_bgB.setPosition(A_x, B_y + BG_SPEED);

			if (A_y >= main.WINDOWS_HEIGHT / 2 + main.WINDOWS_HEIGHT) {
				A_y = -1 * main.WINDOWS_HEIGHT / 2;
				_sprite_bg.setPosition(A_x, A_y + BG_SPEED);
			}
			if (B_y >= main.WINDOWS_HEIGHT / 2 + main.WINDOWS_HEIGHT) {
				B_y = -1 * main.WINDOWS_HEIGHT / 2;
				_sprite_bgB.setPosition(A_x, B_y + BG_SPEED);
			}

		}

	}

	// 按下鍵盤事件
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_UP) {
			_fighter.Up = true;
		} else if (key == KeyEvent.VK_DOWN) {
			_fighter.Down = true;
		} else if (key == KeyEvent.VK_LEFT) {
			_fighter.Left = true;
		} else if (key == KeyEvent.VK_RIGHT) {
			_fighter.Right = true;
		} else if (key == KeyEvent.VK_CONTROL) {
//			Bullet tempb = new Bullet(this, "res\\bullet.png", 16, 20, _fighter.get_x(), _fighter.get_y(),
//					Type.Fighter);
			Bullet tempb =_fighter.Fire();
			if(tempb!=null){
				addToScene(tempb, 1);
				bullets.add(tempb);
			}

		}

	}

	// 釋放鍵盤事件
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_UP) {
			_fighter.Up = false;
		} else if (key == KeyEvent.VK_DOWN) {
			_fighter.Down = false;
		} else if (key == KeyEvent.VK_LEFT) {
			_fighter.Left = false;
		} else if (key == KeyEvent.VK_RIGHT) {
			_fighter.Right = false;
		} else if (key == KeyEvent.VK_CONTROL) {
//			Bullet tempb = new Bullet(this, "res\\bullet.png", 16, 20, _fighter.get_x(), _fighter.get_y(),
//					Type.Fighter);
//			addToScene(tempb, 1);
//			bullets.add(tempb);
		}
	}

}

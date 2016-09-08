package com.game.ian;

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.game.ian.Animation.StatusListener;
import com.game.ian.model.Bullet;
import com.game.ian.model.Enemy;
import com.game.ian.model.Fighter;
import com.game.ian.util.Explosion;
import com.game.ian.util.HpController;
import com.game.ian.util.Label;
import com.game.ian.util.SoundManager;

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
	private LinkedList<Bullet> Fbullets = new LinkedList<Bullet>();
	private LinkedList<Enemy> enemys = new LinkedList<Enemy>();

	private Enemy _enemy;
	private Sprite _sprite_bg;
	private Sprite _sprite_bgB;
	private Sprite hp = new HpController(this);
	private Label label = new Label(this, 100, 20);

	public MainScene() {
		 SoundManager.initialSound();

		_rect = new Insets(0, 0, main.WINDOWS_HEIGHT, main.WINDOWS_WIDTH);

		_sprite_bg = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
		_sprite_bg.setPosition(main.WINDOWS_WIDTH / 2, main.WINDOWS_HEIGHT / 2);
		addToScene(_sprite_bg);

		_sprite_bgB = new Sprite(this, "res\\bgB.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
		_sprite_bgB.setPosition(main.WINDOWS_WIDTH / 2, -1 * main.WINDOWS_HEIGHT / 2);
		addToScene(_sprite_bgB);

		_fighter = new Fighter(this, "res\\fighter.png", 90, 60, 3);
		SpawnFighter();
		addToScene(_fighter);
		
		label.set_text("");
		label.setPosition(80, 20);
		addToScene(label,5);
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
		// _enemy5.update();

		// 如果生成不為null加入場景
		if ((_enemy = Enemy.Spawn(this)) != null) {
			enemys.add(_enemy);
			addToScene(_enemy, 4);
		}

		for (int i = 0; i < enemys.size(); i++) {
			Enemy enemy = enemys.get(i);
			// 如果超出畫面
			if (!enemy.get_is_exist()) {
				enemys.remove(enemy);
				removeFromScene(enemy);
			}
			enemy.update();
			Bullet b = enemy.checkFire();

			if (b != null) {
				// System.out.println(b);
				addToScene(b, 4);
				bullets.add(b);
			}
		}

		// 滾動背景
		updateBg();
		// TODO
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

		for (int i = 0; i < Fbullets.size(); i++) {
			Bullet bullet = Fbullets.get(i);
			// 如果超出畫面
			if (!bullet.get_is_exist()) {
				Fbullets.remove(bullet);
				removeFromScene(bullet);
			}
			bullet.update();
		}

		checkCollision();

		if (checkDamage()) {
			if (((HpController) hp).get_hp() == 0) {
				SpawnFighter();
				((HpController) hp).reset();
			}

		}

		hp.setPosition(_fighter._x, _fighter._y+_fighter.get_height()/2);
		addToScene(hp, 4);

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
			// Bullet tempb = new Bullet(this, "res\\bullet.png", 16, 20,
			// _fighter.get_x(), _fighter.get_y(),
			// Type.Fighter);
			Bullet tempb = _fighter.Fire();
			if (tempb != null) {
				addToScene(tempb, 1);
				Fbullets.add(tempb);
			}

		} else if (key == KeyEvent.VK_M) {
			// SoundManager.Mute();
			// SoundManager2.playExplosion();
		} else if (key == KeyEvent.VK_COMMA) {

			Explosion a = Explosion.newInstance(this);
			a.setPosition(_fighter.get_x(), _fighter.get_y());

			// a._listener.onCompleted(_fighter);
			// java Collision Detection
			addToScene(a);

			// bullets.add(tempb);
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
			// Bullet tempb = new Bullet(this, "res\\bullet.png", 16, 20,
			// _fighter.get_x(), _fighter.get_y(),
			// Type.Fighter);
			// addToScene(tempb, 1);
			// bullets.add(tempb);
		}
	}

	public boolean checkCollision() {

		for (Enemy e : enemys) {
			Rectangle r = new Rectangle(e.get_x(), e.get_y(), e.get_width(), e.get_height());

			for (Bullet tb : Fbullets) {
				Rectangle b = new Rectangle(tb.get_x(), tb.get_y(), tb.get_width(), tb.get_height());

				// Assuming there is an intersect method, otherwise just
				// handcompare the values
				if (r.intersects(b)) {
					// A Collision!
					// we know which enemy (e), so we can call e.DoCollision();
					// SoundManager.playExplosion();
//					SoundManager.playExplosion();

					StatusListener listener = new Animation.StatusListener() {
						@Override
						public void onCompleted(Animation animation) {
							removeFromScene(animation);
						}
					};

					Explosion ex = e.DoCollision(listener);

					// ex.setPosition(e.get_x(), e.get_y());
					removeFromScene(e);
					enemys.remove(e);
					removeFromScene(tb);
					Fbullets.remove(tb);
					addToScene(ex, 4);
					// System.out.println("Explosion");
					return true;
				}

			}
		}

		return false;
	}

	public boolean checkDamage() {

		Sprite f = _fighter;
		Rectangle r = new Rectangle(f.get_x(), f.get_y(), f.get_width(), f.get_height());

		for (Bullet tb : bullets) {
			Rectangle b = new Rectangle(tb.get_x(), tb.get_y(), tb.get_width(), tb.get_height());

			// Assuming there is an intersect method, otherwise just
			// handcompare the values
			if (r.intersects(b)) {
				// A Collision!
				// we know which enemy (e), so we can call e.DoCollision();

				// StatusListener listener = new Animation.StatusListener() {
				// @Override
				// public void onCompleted(Animation animation) {
				// removeFromScene(animation);
				// }
				// };
				((HpController) hp).damage(10);

				// Explosion ex = f.DoCollision(listener);

				// ex.setPosition(e.get_x(), e.get_y());
				// removeFromScene(e);
				// enemys.remove(e);
				removeFromScene(tb);
				bullets.remove(tb);
				// addToScene(ex, 4);
				// System.out.println("Explosion");
				return true;
			}

		}

		return false;
	}

	public void setFPS(int frames) {
		label.set_text("FPS:" + frames);
	}

}

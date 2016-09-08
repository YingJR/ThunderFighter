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
	public static final int Background_SPEED = 4;

	private List<RenderLayer> _render_objects = new CopyOnWriteArrayList<>();
	private Insets _rect = null;

	public Insets get_rect() {
		return _rect;
	}

	private Fighter _fighter;
	private LinkedList<Enemy> enemys = new LinkedList<Enemy>();
	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	private LinkedList<Bullet> Fbullets = new LinkedList<Bullet>();

	private Enemy _enemy;
	private Sprite _sprite_bg1;
	private Sprite _sprite_bg2;
	private Sprite hp = new HpController(this);
	private Label fpslabel = new Label(this, 100, 20);
	private Label scorelabel = new Label(this, 100, 20);
	private int Score = 0;

	public MainScene() {
		SoundManager.initialSound();

		_rect = new Insets(0, 0, main.WINDOWS_HEIGHT, main.WINDOWS_WIDTH);

		_sprite_bg1 = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT + Background_SPEED);
		_sprite_bg1.setPosition(main.WINDOWS_WIDTH / 2, main.WINDOWS_HEIGHT / 2);
		addToScene(_sprite_bg1);

		_sprite_bg2 = new Sprite(this, "res\\bgB.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT + Background_SPEED);
		_sprite_bg2.setPosition(main.WINDOWS_WIDTH / 2, -1 * main.WINDOWS_HEIGHT / 2);
		addToScene(_sprite_bg2);

		_fighter = new Fighter(this, "res\\fighter.png", 90, 60, 3);
		SpawnFighter();
		addToScene(_fighter);

		fpslabel.set_text("FPS:");
		fpslabel.setPosition(80, 20);
		addToScene(fpslabel, 5);

		scorelabel.set_text("Score: 0");
		scorelabel.setPosition(80, 40);
		addToScene(scorelabel, 5);

		addToScene(hp, 4);
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

		// Bullet tempb = _fighter.Fire();
		// if (tempb != null) {
		// addToScene(tempb, 1);
		// Fbullets.add(tempb);
		// }
		if (_fighter.shooting) {
			Bullet tempb = _fighter.Fire();
			if (tempb != null) {
				addToScene(tempb, 1);
				Fbullets.add(tempb);
			}
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
				Score = 0;
			}

		}

		hp.setPosition(_fighter._x, _fighter._y + _fighter.get_height() / 2);

		scorelabel.set_text("Score: " + Score);

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

		if (_sprite_bg1 != null && _sprite_bg2 != null) {
			int A_x = _sprite_bg1.get_x();
			int A_y = _sprite_bg1.get_y();
			int B_y = _sprite_bg2.get_y();

			_sprite_bg1.setPosition(A_x, A_y + Background_SPEED);
			_sprite_bg2.setPosition(A_x, B_y + Background_SPEED);

			if (A_y >= main.WINDOWS_HEIGHT / 2 + main.WINDOWS_HEIGHT) {
				A_y = -1 * main.WINDOWS_HEIGHT / 2;
				_sprite_bg1.setPosition(A_x, A_y + Background_SPEED);
			}
			if (B_y >= main.WINDOWS_HEIGHT / 2 + main.WINDOWS_HEIGHT) {
				B_y = -1 * main.WINDOWS_HEIGHT / 2;
				_sprite_bg2.setPosition(A_x, B_y + Background_SPEED);
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
			_fighter.shooting = true;
		} else if (key == KeyEvent.VK_M) {
			// SoundManager.Mute();
		} else if (key == KeyEvent.VK_COMMA) {

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
			_fighter.shooting = false;
		}
	}

	public boolean checkCollision() {

		for (Enemy e : enemys) {
			Rectangle r = new Rectangle(e.get_x()-e.get_width()/2, e.get_y()-e.get_height(), e.get_width(), e.get_height());

			for (Bullet tb : Fbullets) {
				Rectangle b = new Rectangle(tb.get_x()-tb.get_width()/2, tb.get_y()-tb.get_height()/2, tb.get_width(), tb.get_height());

				if (r.intersects(b)) {

					// SoundManager.playExplosion();

					StatusListener listener = new Animation.StatusListener() {
						@Override
						public void onCompleted(Animation animation) {
							removeFromScene(animation);
						}
					};

					Explosion ex = e.DoCollision(listener);
					Score += e.get_score();

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
		Rectangle r = new Rectangle(f.get_x() - f.get_width() / 2, f.get_y() - f.get_height() / 2, f.get_width(),
				f.get_height());

		for (Bullet tb : bullets) {
			Rectangle b = new Rectangle(tb.get_x()-tb.get_width()/2, tb.get_y()-tb.get_height()/2, tb.get_width(), tb.get_height());

			// Assuming there is an intersect method, otherwise just
			// handcompare the values
			

			
			if (r.intersects(b)) {


				
				StatusListener listener = new Animation.StatusListener() {
					@Override
					public void onCompleted(Animation animation) {
						removeFromScene(animation);
					}
				};

				Explosion e = Explosion.newInstance(this,listener);
				e.setPosition(tb.get_x(), tb.get_y());
				
				addToScene(e, 4);
				((HpController) hp).damage(10);

				removeFromScene(tb);
				bullets.remove(tb);

				return true;
			}

		}

		return false;
	}

	public void setFPS(int frames) {
		fpslabel.set_text("FPS:" + frames);
	}

}

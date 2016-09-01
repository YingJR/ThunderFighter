package com.game.ian.test;

import com.game.ian.MainScene;
import com.game.ian.Sprite;
import com.game.ian.model.Bullet;


public class Enemy extends Sprite {
	//移動方向
	public boolean Left,Right;
	
	//速度
	private int speed = MainScene.Velocity_Fighter;

	//位置
	private int x;
	private int y;

	public Enemy(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
	}	

	// Fighter移動
	private void move() {
		y+=speed;

		if(Left){
			x-=speed;
		}
		if(Right){
			x+=speed;
		}
		setPosition(x, y);
	}	

	// 在設定位置判斷邊界
	@Override
	public void setPosition(int x, int y) {

		if (x - get_width() / 2 < this._scene.get_rect().left) {
			x = this._scene.get_rect().left + (get_width() / 2);
		}
		if (x + get_width() / 2 > this._scene.get_rect().right) {
			x = this._scene.get_rect().right - (get_width() / 2);
		}
		if (y - get_height() / 2 < this._scene.get_rect().top) {
			y = this._scene.get_rect().top + (get_height() / 2);
		}
		if (y + get_height() / 2 > this._scene.get_rect().bottom) {
			y = this._scene.get_rect().bottom - (get_height() / 2);
		}
		super.setPosition(x, y);
	}

	// 更新狀態
	public void update() {
		this.x=this.get_x();
		this.y=this.get_y();
		 move();
	}
	
	public void fire(Bullet bullet){
		
	}

}

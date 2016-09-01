package com.game.ian.test;

import com.game.ian.MainScene;
import com.game.ian.Sprite;

public class Enemy extends Sprite {
	public static final int ENEMY_ALIVE_STATE = 0;
	public static final int ENEMY_DEATH_STATE = 1;
	static final int ENEMY_STEP_Y = 5;
	// X,Y 座標位置
	public int m_posX = 0;
	public int m_posY = 0;
	public int mAnimState = ENEMY_ALIVE_STATE;

	public int mPlayID = 0;

	public Enemy(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
		init(200, 20);
	}

	// 初始化座標
	public void init(int x, int y) {
		m_posX = x;
		m_posY = y;
		mAnimState = ENEMY_ALIVE_STATE;
		mPlayID = 0;
	}

	public void update() {
		m_posY += ENEMY_STEP_Y;
		setPosition(m_posX, m_posY);
	}

}

package com.game.ian.test;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class Enemy2 {
	// 存活狀態
	public static final int ENEMY_ALIVE_STATE = 0;

	// 死亡狀態
	public static final int ENEMY_DEATH_STATE = 1;
	// 飛行的Y軸速度
	static final int ENEMY_STEP_Y = 5;
	// X,Y 座標位置
	public int m_posX = 0;
	public int m_posY = 0;
	// 敵機狀態
	public int mAnimState = ENEMY_ALIVE_STATE; // 最初為存活狀態
	private Image enemyExplorePic[] = new Image[6];// 爆炸圖片[]

	// 當前frame的ID爆炸時的圖片
	public int mPlayID = 0;

	public Enemy2() {
		for (int i = 0; i < 6; i++) {
			enemyExplorePic[i] = Toolkit.getDefaultToolkit().getImage("image\\bomb_enemy_" + i + ".png");
		}
	}

	// 初始化座標
	public void init(int x, int y) {
		m_posX = x;
		m_posY = y;
		mAnimState = ENEMY_ALIVE_STATE;
		mPlayID = 0;
	}

	// 繪製敵機
	public void DrawEnemy(Graphics g, JPanel i) {
		// 狀態為死亡且死亡動畫撥放完畢,則不再繪製敵機
		if (mAnimState == ENEMY_DEATH_STATE && mPlayID < 6) {
			g.drawImage(enemyExplorePic[mPlayID], m_posX, m_posY, (ImageObserver) i);
			mPlayID++;
			return;
		}
		// 當狀態為存活
		Image pic = Toolkit.getDefaultToolkit().getImage("images/e1_0.png");
		g.drawImage(pic, m_posX, m_posY, (ImageObserver) i);
	}

	public void UpdateEnemy() {
		m_posY += ENEMY_STEP_Y;
	}

}

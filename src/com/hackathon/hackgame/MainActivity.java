package com.hackathon.hackgame;

import com.hackathon.framework.Graphics;
import com.hackathon.framework.Graphics.ImageFormat;
import com.hackathon.framework.Screen;
import com.hackathon.framework.implementation.AndroidGame;

public class MainActivity extends AndroidGame {

	@Override
	public Screen getInitScreen() {
		Graphics g = this.getGraphics();
		Assets.pleft = g.newImage("pleft.png", ImageFormat.RGB565).resize(50, 50);
		Assets.pright = g.newImage("pright.png", ImageFormat.RGB565).resize(50, 50);
		Assets.pstill = g.newImage("pstill.png", ImageFormat.RGB565).resize(50, 50);
		Assets.coin = g.newImage("coin.png", ImageFormat.RGB565).resize(30, 30);
		Assets.coin2 = g.newImage("coin66.png", ImageFormat.RGB565).resize(30, 30);
		Assets.coin3 = g.newImage("coin88.png", ImageFormat.RGB565).resize(30, 30);
		Assets.apple = g.newImage("apple.png", ImageFormat.RGB565).resize(30, 30);
		Assets.key = g.newImage("key.png", ImageFormat.RGB565).resize(50, 50);
		Assets.maze1 = g.newImage("maze1.PNG", ImageFormat.RGB565).resize(800, 480);
		Assets.maze2 = g.newImage("maze2.jpg", ImageFormat.RGB565).resize(800, 480);
		Assets.levelScreen = g.newImage("levelscreen.png", ImageFormat.RGB565).resize(800, 480);
		return new MainMenuScreen(this);
	}

	@Override
	public void onBackPressed() {
		getCurrentScreen().backButton();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}

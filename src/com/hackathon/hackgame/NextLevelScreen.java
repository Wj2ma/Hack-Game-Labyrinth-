package com.hackathon.hackgame;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.hackathon.framework.Graphics;
import com.hackathon.framework.Image;
import com.hackathon.framework.Graphics.ImageFormat;
import com.hackathon.framework.Input.TouchEvent;
import com.hackathon.framework.Screen;

public class NextLevelScreen extends Screen {

	private Paint paint;
	private Typeface boldFont;
	private int eX, eY;
	private int level, coins;
	private Image coin;

	public NextLevelScreen(MainActivity game, int l, int c) {
		super(game);
		paint = new Paint();
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint.setTypeface(boldFont);
		level = l;
		coins = c;
		Graphics g = game.getGraphics();
		coin = g.newImage("coin.png", ImageFormat.RGB565).resize(100, 100);
	}

	@Override
	public void update(float deltaTime) {
		List <TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for (TouchEvent e: touchEvents) {
			if (e.type == TouchEvent.TOUCH_DOWN) {
				eX = e.x;
				eY = e.y;
			} else if (e.type == TouchEvent.TOUCH_UP) {
				if (inBounds(eX,eY,50,320,200,100) && inBounds(e.x,e.y, 50,320,200,100))
					game.setScreen(new GameScreen((MainActivity) game, level));
				else if (inBounds(eX,eY,300, 320, 200, 100) && inBounds(e.x,e.y,300, 320, 200, 100))
					game.setScreen(new MainMenuScreen((MainActivity) game));
				else if (inBounds(eX,eY,550, 320, 200, 100) && inBounds(e.x,e.y,550, 320, 200, 100))
					game.setScreen(new GameScreen((MainActivity) game, level+1));
			}
				
		}
	}
	
	private boolean inBounds(int eX, int eY, int x, int y, int width, int height) {
		if (eX > x && eX < x + width - 1 && eY > y && eY < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 0, 0, 0);

		paint.setTextSize(75);
		paint.setColor(Color.WHITE);
		g.drawString("Level Complete!", 130, 100, paint);
		
		//Coins earned
		switch (coins) {
		case 3:
			g.drawImage(coin, 550, 170);
		case 2:
			g.drawImage(coin, 350, 170);
		case 1:
			g.drawImage(coin, 150, 170);
		}
		
		switch (coins) {
		case 0: 
			g.drawCircle(200, 220, 50, Color.LTGRAY);
		case 1:
			g.drawCircle(400, 220, 50, Color.LTGRAY);
		case 2:
			g.drawCircle(600, 220, 50, Color.LTGRAY);
		}
		
		//Buttons
		paint.setColor(Color.BLACK);
		paint.setTextSize(50f);
		g.drawRect(50, 320, 200, 100, Color.WHITE);
		g.drawRect(300, 320, 200, 100, Color.WHITE);
		g.drawRect(550, 320, 200, 100, Color.WHITE);
		g.drawString("Retry", 90, 388, paint);
		g.drawString("Menu", 338, 388, paint);
		g.drawString("Next", 600, 388, paint);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub

	}

}

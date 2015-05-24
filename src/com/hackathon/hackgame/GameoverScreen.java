package com.hackathon.hackgame;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.hackathon.framework.Graphics;
import com.hackathon.framework.Input.TouchEvent;
import com.hackathon.framework.Screen;

public class GameoverScreen extends Screen {

	private Paint paint;
	private Typeface boldFont;
	private int eX, eY;
	private int level;

	public GameoverScreen(MainActivity game, int l) {
		super(game);
		paint = new Paint();
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint.setTypeface(boldFont);
		level = l;
	}

	@Override
	public void update(float deltaTime) {
		List <TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for (TouchEvent e: touchEvents) {
			if (e.type == TouchEvent.TOUCH_DOWN) {
				eX = e.x;
				eY = e.y;
			} else if (e.type == TouchEvent.TOUCH_UP) {
				if (inBounds(eX,eY,100,320,250,100) && inBounds(e.x,e.y, 100,320,250,100))
					game.setScreen(new GameScreen((MainActivity) game, level));
				else if (inBounds(eX,eY,450,320,250,100) && inBounds(e.x,e.y,450,320,250,100))
					game.setScreen(new MainMenuScreen((MainActivity) game));
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
		g.drawString("You ran out of energy!", 30, 175, paint);
		
		paint.setColor(Color.BLACK);
		paint.setTextSize(50f);
		g.drawRect(100, 320, 250, 100, Color.WHITE);
		g.drawRect(450, 320, 250, 100, Color.WHITE);
		g.drawString("Retry", 160, 388, paint);
		g.drawString("Menu", 510, 388, paint);
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

package com.hackathon.hackgame;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.hackathon.framework.Graphics;
import com.hackathon.framework.Input.TouchEvent;
import com.hackathon.framework.Screen;

public class MainMenuScreen extends Screen{
	private Typeface choiceFont;
	private Typeface titleFont;
	private Paint paint, paint2;
	private int startX, startY; 

	public MainMenuScreen(MainActivity game) {
		super(game);
		paint = new Paint();
		choiceFont = Typeface.createFromAsset(game.getAssets(), "fonts/Candle3D.ttf");
		paint.setTypeface(choiceFont);
		paint2 = new Paint();
		titleFont = Typeface.createFromAsset(game.getAssets(), "fonts/jabjai_heavy.TTF");
		paint2.setTypeface(titleFont);
	}
	
	@Override
	// loops this while screen is on
	public void update (float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		for(TouchEvent e : touchEvents) {
			if(e.type == TouchEvent.TOUCH_DOWN) {
				startX = e.x;
				startY = e.y;
			} else if (e.type == TouchEvent.TOUCH_UP) {
				if(inBounds(startX, startY, 50, 250, 300, 125) && inBounds(e.x, e.y, 50, 250, 300, 125)){
					game.setScreen(new LevelScreen((MainActivity) game));
				} else if(inBounds(startX, startY, 450, 250, 300, 125) && inBounds(e.x, e.y, 450, 250, 300, 125)) {
					game.setScreen(new CreditScreen((MainActivity) game));
				}
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
		// Background
		g.drawImage(Assets.maze1, 0, 0);
		// Title
		paint2.setColor(Color.rgb(18, 205, 209));
		paint2.setTextSize(90f);
		g.drawString("The Labyrinth", 80, 120, paint2);
		// Buttons
		g.drawRect(50, 250, 300, 125, Color.BLACK);
		g.drawRect(450, 250, 300, 125, Color.BLACK);
		// Choice font
		paint.setColor(Color.WHITE);
		paint.setTextSize(80f);
		g.drawString("Start", 105, 330, paint);
		g.drawString("Credits", 470, 330, paint);
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

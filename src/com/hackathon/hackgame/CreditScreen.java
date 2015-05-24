package com.hackathon.hackgame;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.hackathon.framework.Graphics;
import com.hackathon.framework.Screen;
public class CreditScreen extends Screen{
	private Typeface Font;
	private Paint paint;

	public CreditScreen(MainActivity game) {
		super(game);
		paint = new Paint();
		Font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
		paint.setTypeface(Font);
	}
	
	@Override
	// loops this while screen is on
	public void update (float deltaTime) {
		
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		// Background
		g.drawImage(Assets.maze2, 0, 0);
		// Title
		paint.setColor(Color.BLACK);
		paint.setTextSize(90f);
		g.drawString("Credits:", 250, 120, paint);
		// Choice font
		paint.setTextSize(70f);
		g.drawString("William OG Ma", 160, 240, paint);
		g.drawString("Tony Teamplayer Jin", 80, 320, paint);
		g.drawString("Dweep Sleepless Shah", 55, 400, paint);
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
		game.setScreen(new MainMenuScreen((MainActivity)game));
	}
}
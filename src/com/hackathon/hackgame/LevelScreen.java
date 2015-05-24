package com.hackathon.hackgame;


import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.hackathon.framework.Graphics;
import com.hackathon.framework.Input.TouchEvent;
import com.hackathon.framework.Screen;

public class LevelScreen extends Screen{
	private Typeface boldFont;

	private SharedPreferences saves;
	private Typeface titleFont;
	private Paint paint, paint2;
	private int startX, startY; 
	private int page = 0;
	
	private int numLevels = 8;
	private boolean [] stageDone = new boolean[numLevels];

	public LevelScreen(MainActivity game) {
		super(game);
		paint = new Paint();
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint.setTypeface(boldFont);
		paint2 = new Paint();
		titleFont = Typeface.createFromAsset(game.getAssets(), "fonts/jabjai_heavy.TTF");
		paint2.setTypeface(titleFont);
		
		saves = PreferenceManager.getDefaultSharedPreferences(game);
		saves.getBoolean("Done", false);
		
		
		stageDone[0] = true;
		
		for (int i=1; i<numLevels; i++){
			stageDone[i] = saves.getBoolean("Done"+i, false);
		}
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
				if(inBounds(startX, startY, 68, 128, 129, 84) && inBounds(e.x, e.y, 68, 128, 129, 84)){
					if (stageDone[8*page]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+1));
					}
					
				} else if(inBounds(startX, startY, 242, 128, 130, 84) && inBounds(e.x, e.y, 242, 128, 130, 84)) {
					if (stageDone[8*page+1]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+2));
					}
				} else if(inBounds(startX, startY, 421, 128, 133, 84) && inBounds(e.x, e.y, 421, 128, 133, 84)) {
					if (stageDone[8*page+2]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+3));
					}
				} else if(inBounds(startX, startY, 605, 128, 127, 84) && inBounds(e.x, e.y, 605, 128, 127, 84)) {
					if (stageDone[8*page+3]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+4));
					}
				} else if(inBounds(startX, startY, 68, 240, 129, 86) && inBounds(e.x, e.y, 68, 240, 129, 86)) {
					if (stageDone[8*page+4]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+5));
					}
				} else if(inBounds(startX, startY, 242, 240, 130, 86) && inBounds(e.x, e.y, 242, 240, 130, 86)) {
					if (stageDone[8*page+5]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+6));
					}
				} else if(inBounds(startX, startY, 421, 240, 133, 86) && inBounds(e.x, e.y, 421, 240, 133, 86)) {
					if (stageDone[8*page+6]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+7));
					}
				} else if(inBounds(startX, startY, 605, 240, 127, 86) && inBounds(e.x, e.y, 605, 240, 127, 86)) {
					if (stageDone[8*page+7]){
						game.setScreen(new GameScreen((MainActivity) game,8*page+8));
					}
				} else if (inBounds(startX, startY, 67, 345, 80, 75) && inBounds(e.x, e.y, 67, 345, 80, 75)){
					if (page>numLevels/8){
						page--;
					}
				}else if (inBounds(startX, startY, 657, 345, 80, 75) && inBounds(e.x, e.y, 657,345,80,75)){
					if (page<numLevels/8-1){
						page++;
					}
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
	
	private void drawScore (int s, int x1, int y1, int x2, int y2){
		Graphics g=game.getGraphics();
		int radius = 15;
		int centreX= x2/6;
		int centreY=y2*2/3+y1;
		for (int i=0; i<3; i++){
			g.drawCircle(centreX*(2*i+1)+x1, centreY, radius, Color.BLACK);
			if (i<saves.getInt("Level"+(s+1),0)){
				

				g.drawCircle(centreX*(2*i+1)+x1, centreY, radius-2, Color.YELLOW);
			}else{
				g.drawCircle(centreX*(2*i+1)+x1, centreY, radius-2, Color.GRAY);
			}
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		// Background
		g.drawImage(Assets.levelScreen, 0, 0);
		
		
		paint.setColor(Color.rgb(0, 0, 0));
		paint.setTextSize(20f);
		g.drawString(""+(8*page+1)+"~"+8*(page+1), 800/2-30, 450, paint);
		paint.setColor(Color.rgb(255, 0, 0));
		paint.setTextSize(25f);
		paint2.setColor(Color.rgb(0,0,0));
		paint2.setTextSize(40f);
		
		
		String stage = "";
		int topline = 180;
		int bottomline= 290;
		int left1 = 85;
		int left2 = 260;
		int left3 = 441;
		int left4 = 625;
		
		
		
		
		for (int i=0; i<8; i++){
			stage = 8*page+i+1+"";
			if (i==0){
				if (stageDone[8*page+i]){
					g.drawString(stage, left1+35, topline-15, paint2);
					drawScore(i,68, 128, 129, 84 );
				}else{
					g.drawString("LOCKED", left1, topline, paint);
				}
			}else if (i==1){
				if (stageDone[8*page+i]){
					drawScore(i,242, 128, 130, 84);
					g.drawString(stage, left2+35, topline-15, paint2);
				}else{
					g.drawString("LOCKED", left2, topline, paint);
				}
			}else if (i==2){
				if (stageDone[8*page+i]){
					drawScore(i,421, 128, 133, 84 );
					g.drawString(stage, left3+35, topline-15, paint2);

				}else{
					g.drawString("LOCKED", left3,topline, paint);
				}
			}else if (i==3){
				if (stageDone[8*page+i]){
					drawScore(i,605, 128, 127, 84);
					g.drawString(stage, left4+35, topline-15, paint2);
				}else{
					g.drawString("LOCKED", left4, topline, paint);
				}
			}else if (i==4){
				if (stageDone[8*page+i]){
					drawScore(i,68, 240, 129, 86 );
					g.drawString(stage, left1+35, bottomline-15, paint2);
				}else{
					g.drawString("LOCKED", left1, bottomline, paint);
				}
			}else if (i==5){
				if (stageDone[8*page+i]){
					drawScore(i,242, 240, 130, 86 );
					g.drawString(stage, left2+35, bottomline-15, paint2);
				}else{
					g.drawString("LOCKED", left2, bottomline, paint);
				}
			}else if (i==6){
				if (stageDone[8*page+i]){
					drawScore(i,421, 240, 133, 86 );
					g.drawString(stage, left3+35, bottomline-15, paint2);
				}else{
					g.drawString("LOCKED", left3,bottomline, paint);
				}
			}else if (i==7){
				if (stageDone[8*page+i]){
					drawScore(i,605, 240, 127, 86);
					g.drawString(stage, left4+35, bottomline-15, paint2);
				}else{
					g.drawString("LOCKED", left4, bottomline, paint);
				}
			}
			
				
			
		}
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
		game.setScreen(new MainMenuScreen((MainActivity) game));
	}
}

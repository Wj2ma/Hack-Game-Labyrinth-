package com.hackathon.hackgame;

public class Apple {

	private int x, y;
	private boolean eaten = false;

	public Apple(int xC, int yC) {
		x = xC;
		y = yC;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isEaten() {
		return eaten;
	}
	
	public void setEaten(boolean s) {
		eaten = s;
	}
}

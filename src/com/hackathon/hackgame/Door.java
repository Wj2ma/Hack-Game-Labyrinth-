package com.hackathon.hackgame;

public class Door {

	private int x, y, width, height;
	private boolean passable = false;
	
	public Door(int xC, int yC, int w, int h) {
		x = xC;
		y = yC;
		width = w;
		height = h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isPassable() {
		return passable;
	}
	
	public void setPassable(boolean s) {
		passable = s;
	}
}

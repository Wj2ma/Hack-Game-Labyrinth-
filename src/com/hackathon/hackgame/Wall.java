package com.hackathon.hackgame;

public class Wall {

	private int x, y, width, height;
	
	public Wall(int tx, int ty, int ttx, int tty) {
		x = tx;
		y = ty;
		width = ttx;
		height = tty;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}

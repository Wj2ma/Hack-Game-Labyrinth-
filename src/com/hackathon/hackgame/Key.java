package com.hackathon.hackgame;

public class Key {

	private int x, y, finX, finY;
	private boolean picked = false, finished = false;
	
	public Key(int xC, int yC) {
		x = xC;
		y = yC;
		finX = x;
		finY = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getFinX() {
		return finX;
	}
	
	public int getFinY() {
		return finY;
	}
	
	public void addX(int a) {
		x += a;
	}
	
	public void addY(int a) {
		y += a;
	}

	public void setPicked(boolean s) {
		picked = true;
	}
	
	public boolean isPicked() {
		return picked;
	}
	
	public boolean finished() {
		return finished;
	}
	
	public void setFinished(boolean s) {
		finished = s;
	}
}

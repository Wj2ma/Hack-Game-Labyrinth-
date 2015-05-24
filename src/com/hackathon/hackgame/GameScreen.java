package com.hackathon.hackgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hackathon.framework.Graphics;
import com.hackathon.framework.Input.TouchEvent;
import com.hackathon.framework.Screen;

public class GameScreen extends Screen {

	enum Gamestate {
		Resume, Pause;
	}

	Gamestate state = Gamestate.Resume;

	// Triangle coordinates for the arrows
	private int[] tx1 = { 5, 55, 55 }, ty1 = { 207, 157, 257 }, tx2 = { 185,
			185, 235 }, ty2 = { 157, 257, 207 }, tx3 = { 70, 120, 170 },
			ty3 = { 112, 62, 112 }, tx4 = { 70, 120, 170 }, ty4 = { 297, 347,
					297 };
	private Typeface boldFont;
	private Paint paint;
	private int level, orientation = 0, moved = 0, coins = 0, k = 0, a = 0,
			b = 0, steps, maxSteps, keyCount = 0, endX, endY, endW, endH, eX,
			eY;
	private Animation person, coinAnim;
	private final int SPEED = 4;
	private boolean moveUp, moveLeft, moveRight, moveDown;
	private boolean[] initiator, coin = new boolean[3],
			hideCoin = new boolean[3];
	private int personX = 255, personY = 15;
	private int[] coinX = new int[3], coinY = new int[3], fcoinX = new int[3],
			fcoinY = new int[3], posX = new int[3], posY = new int[3],
			posKX = new int[3], posKY = new int[3];

	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Apple> apples = new ArrayList<Apple>();
	private ArrayList<Key> keys = new ArrayList<Key>();
	private ArrayList<Door> doors = new ArrayList<Door>();

	private SharedPreferences saves;
	private SharedPreferences.Editor editor;

	public GameScreen(MainActivity game, int l) {
		super(game);
		paint = new Paint();
		boldFont = Typeface.create((String) null, Typeface.BOLD);
		paint.setTypeface(boldFont);
		person = new Animation();
		person.addFrame(Assets.pleft, 100);
		person.addFrame(Assets.pstill, 100);
		person.addFrame(Assets.pright, 100);
		initiator = new boolean[4];
		coinAnim = new Animation();
		coinAnim.addFrame(Assets.coin, 100);
		coinAnim.addFrame(Assets.coin2, 100);
		coinAnim.addFrame(Assets.coin3, 100);
		coinAnim.addFrame(Assets.coin2, 100);
		posX[0] = 10;
		posX[1] = 60;
		posX[2] = 110;
		posY[0] = 435;
		posY[1] = 435;
		posY[2] = 435;
		posKX[0] = 5;
		posKX[1] = 55;
		posKX[2] = 105;
		posKY[0] = 375;
		posKY[1] = 375;
		posKY[2] = 375;
		coin[0] = false;
		coin[1] = false;
		coin[2] = false;
		hideCoin[0] = false;
		hideCoin[1] = false;
		hideCoin[2] = false;
		level = l;

		saves = PreferenceManager.getDefaultSharedPreferences(game);
		editor = saves.edit();

		// Get info from text file
		/*
		 * Text File is as follows First int N number of walls next N lines of
		 * wall coordinates/width heights Next 3 lines of coin coordinates Next
		 * a line for number of apples A Next A lines of apple coordinates Next
		 * a line for number of keys K Next K lines of key coordinates Next a
		 * line for number of doors D Next D lines of door coordinates Next a
		 * number for maxsteps last line is coordinates of end goal
		 */
		InputStream in = null;
		try {
			in = game.getFileIO().readAsset("Level" + level + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String conversion = convertStreamToString(in);
		Scanner scanner = new Scanner(conversion);
		int numWalls = scanner.nextInt();
		for (int i = 0; i < numWalls; i++)
			walls.add(new Wall(scanner.nextInt(), scanner.nextInt(), scanner
					.nextInt(), scanner.nextInt()));

		for (int i = 0; i < 3; i++) {
			coinX[i] = scanner.nextInt();
			coinY[i] = scanner.nextInt();
			fcoinX[i] = coinX[i];
			fcoinY[i] = coinY[i];
		}
		int numApples = scanner.nextInt();
		for (int i = 0; i < numApples; i++)
			apples.add(new Apple(scanner.nextInt(), scanner.nextInt()));
		int numKeys = scanner.nextInt();
		for (int i = 0; i < numKeys; i++)
			keys.add(new Key(scanner.nextInt(), scanner.nextInt()));
		int numDoors = scanner.nextInt();
		for (int i = 0; i < numDoors; i++)
			doors.add(new Door(scanner.nextInt(), scanner.nextInt(), scanner
					.nextInt(), scanner.nextInt()));
		maxSteps = scanner.nextInt();
		endX = scanner.nextInt();
		endY = scanner.nextInt();
		endW = scanner.nextInt();
		endH = scanner.nextInt();
		steps = scanner.nextInt();
	}

	private String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.w("LOG", e.getMessage());
			}
		}
		return sb.toString();
	}

	@Override
	// loops this while screen is on
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		for (TouchEvent e : touchEvents) {
			if (state == Gamestate.Resume) {
				if (e.type == TouchEvent.TOUCH_DOWN
						|| e.type == TouchEvent.TOUCH_HOLD
						|| e.type == TouchEvent.TOUCH_DRAGGED) {
					if (inBounds(e.x, e.y, 5, 157, 100, 100)) {
						moveLeft = true;
						moveRight = false;
						moveUp = false;
						moveDown = false;
					} else if (inBounds(e.x, e.y, 135, 157, 100, 100)) {
						moveLeft = false;
						moveRight = true;
						moveUp = false;
						moveDown = false;
					} else if (inBounds(e.x, e.y, 70, 62, 100, 100)) {
						moveLeft = false;
						moveRight = false;
						moveUp = true;
						moveDown = false;
					} else if (inBounds(e.x, e.y, 70, 247, 100, 100)) {
						moveLeft = false;
						moveRight = false;
						moveUp = false;
						moveDown = true;
					}
				} else if (e.type == TouchEvent.TOUCH_UP) {
					moveLeft = false;
					moveRight = false;
					moveUp = false;
					moveDown = false;
				}
			} else {
				if (e.type == TouchEvent.TOUCH_DOWN) {
					eX = e.x;
					eY = e.y;
				} else if (e.type == TouchEvent.TOUCH_UP) {
					if (inBounds(e.x, e.y, 60, 375, 125, 75)
							&& inBounds(eX, eY, 60, 375, 125, 75))
						state = Gamestate.Resume;
					else if (inBounds(e.x, e.y, 245, 375, 125, 75)
							&& inBounds(eX, eY, 245, 375, 125, 75)) {
						nullify();
						game.setScreen(new GameScreen((MainActivity) game,
								level));
						return;
					} else if (inBounds(e.x, e.y, 430, 375, 125, 75)
							&& inBounds(eX, eY, 430, 375, 125, 75)) {
						nullify();
						game.setScreen(new LevelScreen((MainActivity) game));
						return;
					} else if (inBounds(e.x, e.y, 615, 375, 125, 75)
							&& inBounds(eX, eY, 615, 375, 125, 75)) {
						nullify();
						game.setScreen(new MainMenuScreen((MainActivity) game));
						return;
					}
				}
			}
		}

		if (state == Gamestate.Resume) {
			for (Key key : keys) {
				if (!key.isPicked()
						&& inBounds(personX + 10, personY + 25, key.getX(),
								key.getY(), 50, 50))
					key.setPicked(true);

				if (key.isPicked() && !key.finished()) {
					int moveX = (int) Math
							.round((key.getFinX() - posKX[keyCount]) / 40.0);
					int moveY = (int) Math.round((posKY[keyCount] - key
							.getFinY()) / 40.0);
					if (key.getX() > posKX[keyCount])
						key.addX(-moveX);
					if (key.getY() < posKY[keyCount])
						key.addY(moveY);
					b++;
					if (b == 40) {
						key.setFinished(true);
						b = 0;
						keyCount++;
					}
				}
			}

			for (Apple ap : apples) {
				if (!ap.isEaten()
						&& inBounds(personX + 25, personY + 25, ap.getX(),
								ap.getY(), 30, 30)) {
					steps += 5;
					if (steps > maxSteps + 1)
						steps = maxSteps + 1;
					ap.setEaten(true);
				}
			}

			for (int i = 0; i < 3; i++) {
				if (!coin[i]
						&& inBounds(personX + 25, personY + 25, coinX[i],
								coinY[i], 30, 30)) {
					coin[i] = true;
					coins++;
				}

				if (coin[i] && !hideCoin[i]) {
					int moveX = (int) Math.round((fcoinX[i] - posX[k]) / 40.0);
					int moveY = (int) Math.round((posY[k] - fcoinY[i]) / 40.0);
					if (coinX[i] > posX[k])
						coinX[i] -= moveX;
					if (coinY[i] < posY[k])
						coinY[i] += moveY;
					a++;
					if (a == 40) {
						hideCoin[i] = true;
						a = 0;
						k++;
					}
				}
			}

			if (!inBounds(personX, personY, 240, 0, 560, 480)) {
				if (saves.getInt("Level" + level, 0) < coins)
					editor.putInt("Level" + level, coins);
				editor.putBoolean("Done" + level, true);
				editor.commit();
				nullify();
				game.setScreen(new NextLevelScreen((MainActivity) game, level,
						coins));
				return;
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
		g.drawARGB(255, 222, 185, 135);

		// Walls
		for (Wall w : walls)
			g.drawRect(w.getX(), w.getY(), w.getWidth(), w.getHeight(),
					Color.BLACK);

		// end exit
		g.drawRect(endX, endY, endW, endH, Color.GREEN);

		// apples
		for (Apple ap : apples)
			if (!ap.isEaten())
				g.drawImage(Assets.apple, ap.getX(), ap.getY());
		// doors
		for (Door d : doors)
			if (!d.isPassable())
				g.drawRect(d.getX(), d.getY(), d.getWidth(), d.getHeight(),
						Color.GRAY);

		// Grey area
		g.drawRect(0, 0, 240, 480, Color.LTGRAY);

		// coins
		int j = 0;
		for (int i = 0; i < 3; i++)
			if (hideCoin[i]) {
				g.drawImage(Assets.coin.resize(40, 40), posX[j], posY[j]);
				j++;
			}

		while (j < 3) {
			g.drawCircle(posX[j] + 20, posY[j] + 20, 20, Color.GRAY);
			j++;
		}

		// energy
		int color;
		if (steps * 1.0 / maxSteps > 3.0 / 4)
			color = Color.argb(255, 173, 255, 47);
		else if (steps * 1.0 / maxSteps > 1.0 / 2)
			color = Color.YELLOW;
		else if (steps * 1.0 / maxSteps > 1.0 / 4)
			color = Color.argb(255, 255, 165, 0);
		else
			color = Color.RED;
		g.drawRect(10, 10,
				(int) Math.round(((steps - 1) * 1.0 / maxSteps) * 220), 40,
				color);
		g.drawHollowRect(10, 10, 220, 40, Color.argb(255, 139, 69, 19), 3);
		paint.setTextSize(25f);
		g.drawString("Energy", 15, 38, paint);

		// D-pad
		// leftright arrows
		g.drawRect(50, 182, 55, 50, Color.BLACK);
		g.drawPolygon(tx1, ty1, Color.BLACK);
		g.drawRect(135, 182, 55, 50, Color.BLACK);
		g.drawPolygon(tx2, ty2, Color.BLACK);
		// updown arrows
		g.drawRect(95, 112, 50, 50, Color.BLACK);
		g.drawPolygon(tx3, ty3, Color.BLACK);
		g.drawRect(95, 247, 50, 55, Color.BLACK);
		g.drawPolygon(tx4, ty4, Color.BLACK);

		// guy
		if (initiator[0])
			shiftLeft(g);
		else if (initiator[1])
			shiftRight(g);
		else if (initiator[2])
			shiftDown(g);
		else if (initiator[3])
			shiftUp(g);
		else {
			if (moveLeft || moveRight || moveDown || moveUp) {
				if (moveLeft) {
					shiftLeft(g);
					initiator[0] = true;
					orientation = 1;
				} else if (moveRight) {
					shiftRight(g);
					initiator[1] = true;
					orientation = 0;
				} else if (moveDown) {
					shiftDown(g);
					initiator[2] = true;
					orientation = 2;
				} else {
					shiftUp(g);
					initiator[3] = true;
					orientation = 3;
				}
				steps--;
				if (steps <= 0) {
					nullify();
					game.setScreen(new GameoverScreen((MainActivity) game,
							level));
					return;
				}

			} else {
				g.save();
				if (orientation == 1)
					g.rotate(90, personX + 25, personY + 25);
				else if (orientation == 0)
					g.rotate(270, personX + 25, personY + 25);
				else if (orientation == 3)
					g.rotate(180, personX + 25, personY + 25);
				g.drawImage(Assets.pstill, personX, personY);
				g.restore();
			}
		}

		for (int i = 0; i < 3; i++) {
			if (!hideCoin[i])
				g.drawImage(coinAnim.getImage(), coinX[i], coinY[i]);
		}

		if (state == Gamestate.Resume)
			coinAnimate();

		// keys
		j = 0;
		for (Key key : keys)
			if (key.finished()) {
				if (j < keyCount)
					g.drawImage(Assets.key, posKX[j], posKY[j]);
				j++;
			} else {
				g.drawImage(Assets.key, key.getX(), key.getY());
			}

		if (level == 1) {
			if (inBounds(personX, personY, 240, 0, 160, 240)) {
				g.drawRect(400, 440, 390, 40, Color.WHITE);
				paint.setTextSize(30f);
				paint.setColor(Color.BLACK);
				g.drawString("Try to get to over here.", 420, 470, paint);
				g.drawRect(300, 425, 50, 30, Color.RED);
				int[] xcoord = { 250, 300, 300 }, ycoord = { 440, 410, 470 };
				g.drawPolygon(xcoord, ycoord, Color.RED);
			} else if (inBounds(personX, personY, 400, 0, 240, 240)) {
				g.drawRect(250, 440, 540, 40, Color.WHITE);
				paint.setTextSize(30f);
				paint.setColor(Color.BLACK);
				g.drawString("Try to collect as many of these coins.", 270,
						470, paint);
				g.drawRect(680, 345, 50, 30, Color.RED);
				int[] xcoord = { 630, 680, 680 }, ycoord = { 360, 330, 390 };
				g.drawPolygon(xcoord, ycoord, Color.RED);
			} else if (inBounds(personX, personY, 560, 240, 240, 240)) {
				g.drawRect(300, 440, 490, 40, Color.WHITE);
				paint.setTextSize(30f);
				paint.setColor(Color.BLACK);
				g.drawString("Don't let your energy deplete.", 320, 470, paint);
				g.drawRect(290, 15, 50, 30, Color.RED);
				int[] xcoord = { 240, 290, 290 }, ycoord = { 30, 0, 60 };
				g.drawPolygon(xcoord, ycoord, Color.RED);
			}
		} else if (level == 2) {
			if (inBounds(personX, personY, 480, 80, 560, 80)) {
				g.drawRect(300, 440, 490, 40, Color.WHITE);
				paint.setTextSize(30f);
				paint.setColor(Color.BLACK);
				g.drawString("Eat apples to increase energy.", 320, 470, paint);
				g.drawRect(370, 105, 50, 30, Color.RED);
				int[] xcoord = { 320, 370, 370 }, ycoord = { 120, 90, 150 };
				g.drawPolygon(xcoord, ycoord, Color.RED);
			} else if (inBounds(personX, personY, 240, 160, 560, 80)) {
				g.drawRect(250, 440, 540, 40, Color.WHITE);
				paint.setTextSize(30f);
				paint.setColor(Color.BLACK);
				g.drawString("Collect these keys to open grey doors.", 270,
						470, paint);
				g.drawRect(610, 265, 50, 30, Color.RED);
				int[] xcoord = { 560, 610, 610 }, ycoord = { 280, 250, 310 };
				g.drawPolygon(xcoord, ycoord, Color.RED);
				g.drawRect(370, 345, 50, 30, Color.GREEN);
				int[] xcoord2 = { 470, 420, 420 }, ycoord2 = { 360, 330, 390 };
				g.drawPolygon(xcoord2, ycoord2, Color.GREEN);
			} else if (inBounds(personX, personY, 480, 320, 320, 80)) {
				g.drawRect(250, 440, 540, 40, Color.WHITE);
				paint.setTextSize(30f);
				paint.setColor(Color.BLACK);
				g.drawString("You lose a key when opening a door.", 270, 470,
						paint);
			}
		}

		if (state == Gamestate.Pause) {
			g.drawARGB(100, 0, 0, 0);
			g.drawRect(60, 375, 125, 75, Color.YELLOW);
			g.drawRect(245, 375, 125, 75, Color.YELLOW);
			g.drawRect(430, 375, 125, 75, Color.YELLOW);
			g.drawRect(615, 375, 125, 75, Color.YELLOW);
			paint.setTextSize(30f);
			paint.setColor(Color.BLACK);
			g.drawString("Resume", 65, 425, paint);
			paint.setTextSize(35f);
			g.drawString("Retry", 265, 425, paint);
			g.drawString("Level", 450, 410, paint);
			g.drawString("Select", 442, 440, paint);
			g.drawString("Menu", 632, 425, paint);
		}
	}

	// Move left at speed
	private void shiftLeft(Graphics g) {
		// Check if crossing a wall
		boolean stuck = false;
		for (Wall w : walls)
			if (inBounds(personX - 5, personY, w.getX(), w.getY(),
					w.getWidth(), w.getHeight())) {
				stuck = true;
				break;
			}
		// check if crossing door
		for (Door d : doors)
			if (!d.isPassable())
				if (inBounds(personX - 5, personY, d.getX(), d.getY(),
						d.getWidth(), d.getHeight())) {
					if (keyCount > 0) {
						keyCount--;
						d.setPassable(true);
					} else {
						stuck = true;
						break;
					}
				}

		// if didnt cross wall move the direction
		if (!stuck)
			personX -= SPEED;

		// Check if sprite reaches next snap section
		moved += SPEED;
		if (moved == 80) {
			initiator[0] = false;
			moved = 0;
		}

		// Draw sprite and animate it
		g.save();
		g.rotate(90, personX + 25, personY + 25);
		g.drawImage(person.getImage(), personX, personY);
		g.restore();
		animate();
	}

	// refer to shiftLeft
	private void shiftRight(Graphics g) {
		boolean stuck = false;
		for (Wall w : walls)
			if (inBounds(personX + 55, personY, w.getX(), w.getY(),
					w.getWidth(), w.getHeight())) {
				stuck = true;
				break;
			}
		for (Door d : doors)
			if (!d.isPassable())
				if (inBounds(personX + 55, personY, d.getX(), d.getY(),
						d.getWidth(), d.getHeight())) {
					if (keyCount > 0) {
						keyCount--;
						d.setPassable(true);
					} else {
						stuck = true;
						break;
					}
				}

		if (!stuck)
			personX += SPEED;
		moved += SPEED;
		if (moved == 80) {
			initiator[1] = false;
			moved = 0;
		}
		g.save();
		g.rotate(270, personX + 25, personY + 25);
		g.drawImage(person.getImage(), personX, personY);
		g.restore();
		animate();
	}

	// refer to shiftLeft
	private void shiftUp(Graphics g) {
		boolean stuck = false;
		for (Wall w : walls)
			if (inBounds(personX, personY - 5, w.getX(), w.getY(),
					w.getWidth(), w.getHeight())) {
				stuck = true;
				break;
			}
		for (Door d : doors)
			if (!d.isPassable())
				if (inBounds(personX, personY - 5, d.getX(), d.getY(),
						d.getWidth(), d.getHeight())) {
					if (keyCount > 0) {
						keyCount--;
						d.setPassable(true);
					} else {
						stuck = true;
						break;
					}
				}

		if (!stuck)
			personY -= SPEED;
		moved += SPEED;
		if (moved == 80) {
			initiator[3] = false;
			moved = 0;
		}
		g.save();
		g.rotate(180, personX + 25, personY + 25);
		g.drawImage(person.getImage(), personX, personY);
		g.restore();
		animate();
	}

	// refer to shiftLeft
	private void shiftDown(Graphics g) {
		boolean stuck = false;
		for (Wall w : walls)
			if (inBounds(personX, personY + 55, w.getX(), w.getY(),
					w.getWidth(), w.getHeight())) {
				stuck = true;
				break;
			}
		for (Door d : doors)
			if (!d.isPassable())
				if (inBounds(personX, personY + 55, d.getX(), d.getY(),
						d.getWidth(), d.getHeight())) {
					if (keyCount > 0) {
						keyCount--;
						d.setPassable(true);
					} else {
						stuck = true;
						break;
					}
				}

		if (!stuck)
			personY += SPEED;
		moved += SPEED;
		if (moved == 80) {
			initiator[2] = false;
			moved = 0;
		}
		g.drawImage(person.getImage(), personX, personY);
		animate();
	}

	private void animate() {
		person.update(10);
	}

	private void coinAnimate() {
		coinAnim.update(10);
	}

	private void nullify() {
		initiator = null;
		coin = null;
		hideCoin = null;
		coinX = null;
		coinY = null;
		fcoinX = null;
		fcoinY = null;
		posX = null;
		posY = null;
		walls.clear();
		walls = null;
		System.gc();
	}

	@Override
	public void pause() {
		state = Gamestate.Pause;
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		pause();

	}

}

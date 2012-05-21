package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import level.Box;
import level.Loader;
import entities.Entity;

public class Game extends Canvas implements Runnable {

	private static Game instance = null;

	public static final int BLOCK_SIZE = 50;

	public static int FIELD_WIDTH = 16;
	public static int FIELD_HEIGHT = ((Game.FIELD_WIDTH * 3) / 4);

	public static int GAME_WIDTH = (Game.FIELD_WIDTH * Game.BLOCK_SIZE);
	public static int GAME_HEIGHT = (Game.FIELD_HEIGHT * Game.BLOCK_SIZE);

	public static CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
	public static CopyOnWriteArrayList<Entity> staticBackground = new CopyOnWriteArrayList<Entity>();
	public static CopyOnWriteArrayList<Entity> players = new CopyOnWriteArrayList<Entity>();
	private boolean running;

	private int maxUpdateRate = 50;
	private long frameTimeNs = 1000000000 / this.maxUpdateRate;
	private int minSleepTime = 10;
	public int fps_static = 0;
	public int fps = 0;

	public static InputHandler keys = new InputHandler();

	public static Game getInstance() {
		if (Game.instance == null) {
			Game.instance = new Game();
		}
		return Game.instance;
	}

	/**
	 * Constructor to set Canvas size and create important objects and add some
	 * Test objects
	 * 
	 */
	private Game() {
		Debug.setMode(Debug.DEBUG);
		this.init();
	}

	/**
	 * Game-Loop Check how long it took to render a frame and let the thread
	 * sleep some ns
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		long lastLoopTime = System.nanoTime();
		int lastFpsTime = 0;
		long sleepTime = 0;
		BufferStrategy bs = this.getBufferStrategy();
		while (this.running) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / this.frameTimeNs;

			lastFpsTime += updateLength;
			this.fps++;

			if (lastFpsTime >= 1000000000) {
				this.fps_static = this.fps;
				lastFpsTime = 0;
				this.fps = 0;
			}

			/**
			 * Move all objects
			 */
			this.action(delta);

			/**
			 * Redraw all objects
			 */
			Graphics g = bs.getDrawGraphics();
			this.draw(g);
			bs.show();
			Toolkit.getDefaultToolkit().sync();

			/**
			 * Let the thread sleep
			 */
			sleepTime = (lastLoopTime - System.nanoTime()) / this.frameTimeNs;
			if (sleepTime < this.minSleepTime) {
				sleepTime = this.minSleepTime;
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void init() {
		Game.entities = new CopyOnWriteArrayList<Entity>();
		Game.staticBackground = new CopyOnWriteArrayList<Entity>();
		Game.players = new CopyOnWriteArrayList<Entity>();

		Loader l1 = new Loader();
		l1.addWalls("Map2");
		Game.GAME_WIDTH = (Game.FIELD_WIDTH * Game.BLOCK_SIZE) + 1;
		Game.GAME_HEIGHT = (Game.FIELD_HEIGHT * Game.BLOCK_SIZE) + 1;

		Dimension d = new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);

		this.requestFocus();
		this.setFocusable(true);
		Game.keys = new InputHandler();
		this.addKeyListener(Game.keys);
	}

	/**
	 * Gets called form Launcher to start the game;
	 */
	public void start() {
		this.init();
		this.createBufferStrategy(2);

		this.running = true;

		this.run();
	}

	/**
	 * Stop the game
	 */
	public void stop() {
		this.running = false;
	}

	/**
	 * Move all entities
	 * 
	 * @param delta
	 */
	private void action(double delta) {

		for (Entity e : Game.entities) {
			if ((e.removed == false) && (e.needsStep == true)) {
				e.action(delta);
			}
		}

		for (Entity e : Game.players) {
			if (e.removed) {
				this.gameEnd(false);
				break;
			}
		}
	}

	/**
	 * Draw everything
	 * 
	 * @param g
	 */
	private void draw(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		for (Entity e : Game.staticBackground) {
			e.draw(g);
		}
		for (Entity e : Game.entities) {
			if (e.removed == false) {
				e.draw(g);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + this.fps_static, 0, 10);
	}

	/**
	 * Get all Entities in a Box
	 * 
	 * @return List<Entity> Found entities
	 */
	public static List<Entity> getEntities(Box b) {
		List<Entity> result = new ArrayList<Entity>();

		for (Entity e : Game.entities) {
			if (e.removed == false) {
				if (e.box.intersects(b)) {
					result.add(e);
				}
			}
		}
		return result;
	}

	public void gameEnd(boolean win) {
		this.stop();
		Object[] options = { "Neustart", "Beenden" };
		JOptionPane question;
		if (win == true) {
			question = new JOptionPane("Du hast gewonnen!");
		} else {
			question = new JOptionPane("Du hast verloren.");
		}
		question.setOptions(options);
		JDialog dialog = question.createDialog(new JFrame(), "Spielende");
		dialog.setVisible(true);
		Object obj = question.getValue();
		if (obj.equals(options[0])) {
			// Spiel neustarten
			this.start();
		} else {
			// Spiel beenden;
			System.exit(0);
		}
	}
}

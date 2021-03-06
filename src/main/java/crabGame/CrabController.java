package main.java.crabGame;

import main.java.crabGame.controller.CrabGameListeners;
import main.java.crabGame.model.CrabGameWorld;
import main.java.crabGame.model.Friend;
import main.java.crabGame.model.Question;
import main.java.crabGame.model.Utilities;
import main.java.menu.controller.MGController;
import main.java.menu.controller.MGView;
import main.java.menu.view.MenuScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alina on 2017-07-25.
 */

public class CrabController extends MGController {

	public static final int MAX_SCORE = 3500;
	MenuScreen menu;
	public static CrabGamePanel gamePanel;
	public static CrabGameWorld game; // Create the world

	private static Timer saltCloudMovementTimer; // Timer that moves saltClouds in the world
	private static Timer gameTimer; // gameTimer
	static Timer enemySpawnTimer;
	static Timer tutorialTimer;

	private static long lastTime;
	private static long currentTime;
	private static long deltaTime;

	public static boolean running = false;
	public static boolean paused = false;

	public static int score;
	public static int difficultyLevel = 1;

	public CrabController(MenuScreen menu) {
		this.menu = menu;
		gamePanel = new CrabGamePanel();
		game = new CrabGameWorld();
		Question.load();

		final int GAME_TIMER_DELAY_ = 17;
		final int SALT_CLOUD_MOVEMENT_TIMER_DELAY_ = 10000;

		gameTimer = new Timer(GAME_TIMER_DELAY_, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		saltCloudMovementTimer = new Timer(SALT_CLOUD_MOVEMENT_TIMER_DELAY_, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!paused) {
					CrabGameWorld.moveSaltClouds();
				}
			}
		});

		int ENEMY_SPAWN_TIMER_DELAY = 2500;
		enemySpawnTimer = new Timer(ENEMY_SPAWN_TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!paused) {
					CrabGameWorld.spawnEnemy();
				}
			}
		});

		int TUTORIAL_TIMER_DELAY = 7000;
		tutorialTimer = new Timer(TUTORIAL_TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Friend.friendCounter < Friend.facts.length) {
					CrabGameWorld.friends.add(new Friend(CrabGameWorld.WORLD_WIDTH, CrabGameWorld.WORLD_HEIGHT - 300));
				} else {
					endPlayEntry();
				}
			}
		});

		CrabGameListeners listeners = new CrabGameListeners();
		gamePanel.addKeyListener(listeners); // Input listeners
		gamePanel.addMouseListener(listeners); // Input listeners

		gamePanel.setBounds(0, 0, MenuScreen.frameWidth, MenuScreen.frameHeight);

		Utilities.loadFonts();
	}

	/**
	 * Starts the game's loop
	 */
	public static void startGame() {
		game.restartWorld();

		Friend.friendCounter = 0;
		enemySpawnTimer.stop();
		gameTimer.restart();
		saltCloudMovementTimer.restart();
		tutorialTimer.restart();
		tutorialTimer.setRepeats(true);

		lastTime = 0;
		currentTime = 0;
		deltaTime = 0;

		score = 0;
		running = true;
	}

	@Override
	public void update() {
		int minUpdateTime = 10;

		if (running) {
			currentTime = System.nanoTime();
			deltaTime = ((currentTime - lastTime) / 1000000);
			if (deltaTime > minUpdateTime) {
				// If at least a certain amount of time has passed
				lastTime = currentTime; // Update the time since the last update

				if (!paused) {
					game.update(deltaTime); // Update the world
				}
			}
		}

		CrabController.gamePanel.repaint();
	}

	@Override
	public void dispose() {
		System.out.println("Dispose Called");
		enemySpawnTimer.stop(); 
		//menu.hideMenuButton();
		reset();
	}

	public static void reset() {
		gamePanel.setVisible(false);
		running = false;
		game.restartWorld();
	}

	@Override
	public MGView getView() {
		return gamePanel;
	}

	/**
	 * What happens while a crabby overlaps a saltCloud
	 */
	public static void incrementScore() {
		if (score <= MAX_SCORE && game.checkGameStatePlay()) {
			score+=4;
		}
		if (score == MAX_SCORE / 4) {
			difficultyLevel++;
			System.out.println("difficulty level increased to " + difficultyLevel);
		}
		if (score == MAX_SCORE * 2 / 4) {
			difficultyLevel++;
			System.out.println("difficulty level increased to " + difficultyLevel);
		}
		if (score == MAX_SCORE * 3 / 4) {
			difficultyLevel++;
			System.out.println("difficulty level increased to " + difficultyLevel);
		}
		CrabGameWorld.crabbyScoring = true;
	}

	void endPlayEntry() {
		tutorialTimer.setRepeats(false);
		game.setGameStateToPlay();
		enemySpawnTimer.restart();
		enemySpawnTimer.start();
	}

}

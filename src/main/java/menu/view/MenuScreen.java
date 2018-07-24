package main.java.menu.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import main.java.boatGame.controller.BoatController;
import main.java.boatGame.view.BoatGameScreen;
import main.java.crabGame.CrabController;
import main.java.crabGame.CrabGamePanel;
import main.java.cubeGame.controller.CubeController;
import main.java.menu.controller.MGController;
import main.java.menu.enums.IMAGES;

public class MenuScreen extends JLayeredPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6097571045147215752L;   //added in PCM
	public static final ImageManager IMAGE = new ImageManager();
	//private static final Dimension size = new Dimension(1920, 1080);
	private static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final int frameWidth = size.width;
	public static final int frameHeight = size.height;
	public final double buttonScale = (MenuScreen.frameHeight *2.0) / 1340;
	JFrame frame = new JFrame();
	CrabController crabController;
	BoatController boatController;
	CubeController cubeController;
	MGController prevController;
	JPanel gameView = new JPanel();
	Wave wave;
	JPanel menu;
	JPanel scoreboard;
	JButton menuButton;
	Rectangle menuLoc = new Rectangle(10, 10, 125, 30);
	TutorialCrab tutorialCrab;
	TutorialCube tutorialCube;
	Tutorial tutorialBoat;
	JButton crabButton, boatButton, cubeButton, tutCrabButton, tutBoatButton, tutCubeButton;
	JLabel blankL = new JLabel("");
	JLabel blankR = new JLabel("");

	public MenuScreen() {
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this, BorderLayout.CENTER);
		frame.setSize(new Dimension(frameWidth, frameHeight));
		frame.setVisible(true);
		

		crabController = new CrabController();
		boatController = new BoatController();
		cubeController = new CubeController();


		wave = new Wave();
		buildMenu();
		this.add(wave,4,0);
		this.add(menu, 2, 0);
		tutorialBoat = new Tutorial((frameWidth - boatButton.getWidth()) / 2, (frameHeight - boatButton.getHeight()) / 2,
				boatButton.getX(), boatButton.getY(), boatButton.getWidth(), boatButton.getHeight());
		this.add(tutorialBoat, 1, 0);
		tutorialBoat.repaint();
		tutorialBoat.setVisible(false);
		
		tutorialCrab = new TutorialCrab((frameWidth - crabButton.getWidth()) / 2, (frameHeight - crabButton.getHeight()) / 2,
				crabButton.getX(), crabButton.getY(), crabButton.getWidth(), crabButton.getHeight());
		this.add(tutorialCrab, 1, 0);
		tutorialCrab.repaint();
		tutorialCrab.setVisible(false);
		
		tutorialCube = new TutorialCube((frameWidth - cubeButton.getWidth()) / 2, (frameHeight - cubeButton.getHeight()) / 2,
				cubeButton.getX(), cubeButton.getY(), cubeButton.getWidth(), cubeButton.getHeight());
		this.add(tutorialCube, 1, 0);
		tutorialCube.repaint();
		tutorialCube.setVisible(false);
	}

	private void printCountFile(int[] counters) throws Exception{
		PrintWriter writer = new PrintWriter("Total Count of Games Ran.txt", "UTF-8");
		writer.println("|------------------------------------|");
		writer.println("| Name of Game  | Total Times Played |");
		writer.println("|------------------------------------|");
		writer.println("|   Crab Game   |         " + counters[0] + "          |");
		writer.println("|------------------------------------|");
		writer.println("|   Boat Game   |         " + counters[1] + "          |");
		writer.println("|------------------------------------|");
		writer.println("|   Cube Game   |         " + counters[2] + "          |");
		writer.println("|------------------------------------|");
		writer.close();
	}

	private void buildMenu() {
		final int[] counters = {0,0,0};
		menu = new JPanel(new GridBagLayout());
		menu.setBounds(0, 0, frameWidth, frameHeight);
		menu.setOpaque(false);
		
		crabButton = new JButton(new ImageIcon(ImageManager.scaleButton(IMAGES.CRAB_BUTTON, buttonScale)));
		crabButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Crab button pushed");
				counters[0]++;
				try {
					printCountFile(counters);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				switchGame(crabController);
			}
		});
		ImageManager.tailorButton(crabButton);
		
		boatButton = new JButton(new ImageIcon(ImageManager.scaleButton(IMAGES.BOAT_BUTTON, buttonScale)));
		boatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				counters[1]++;
				try {
					printCountFile(counters);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				switchGame(boatController);
			}
		});
		ImageManager.tailorButton(boatButton);
		
		cubeButton = new JButton(new ImageIcon(ImageManager.scaleButton(IMAGES.CUBE_BUTTON, buttonScale)));
		cubeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				counters[2]++;
				try {
					printCountFile(counters);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				switchGame(cubeController);
			}
		});
		ImageManager.tailorButton(cubeButton);
		
		tutBoatButton = new JButton(new ImageIcon(ImageManager.scaleButton(IMAGES.QUESTION_MARK, buttonScale)));
		ImageManager.tailorButton(tutBoatButton);
		
		tutCrabButton = new JButton(new ImageIcon(ImageManager.scaleButton(IMAGES.QUESTION_MARK, buttonScale)));
		ImageManager.tailorButton(tutCrabButton);
		
		tutCubeButton = new JButton(new ImageIcon(ImageManager.scaleButton(IMAGES.QUESTION_MARK, buttonScale)));
		ImageManager.tailorButton(tutCubeButton);
		
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = 0;
		cons.weighty = .5;
		cons.weightx = .1;
		cons.anchor = GridBagConstraints.SOUTHEAST;
		cons.gridx = 0;
		menu.add(crabButton, cons);
		
		cons.gridx = 1;
		cons.anchor = GridBagConstraints.SOUTH;
		menu.add(boatButton, cons);
		
		cons.gridx = 2;
		cons.anchor = GridBagConstraints.SOUTHWEST;
		menu.add(cubeButton, cons);
		
		cons.gridy = 1;
		cons.weighty = .5;
		cons.anchor = GridBagConstraints.NORTH;
		cons.gridx = 1;

		menu.add(tutBoatButton, cons);
		
		cons.gridy = 1;
		cons.weighty = .5;
		cons.anchor = GridBagConstraints.NORTHEAST;
		//cons.anchor = GridBagConstraints.NORTH;
		cons.gridx = 0;
		
		menu.add(tutCrabButton, cons);
		
		cons.gridy = 1;
		cons.weighty = .5;
		cons.anchor = GridBagConstraints.NORTHWEST;
		//cons.anchor = GridBagConstraints.NORTH;
		cons.gridx = 2;
		
		menu.add(tutCubeButton, cons);
		
		
		cons.gridx = 2;
		menu.add(blankR, cons);
		
		cons.gridx = 0;
		menu.add(blankL, cons);
		
		menuButton = new JButton("Menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				menuButtonActionPerformed();
			}
		});

		menuButton.setBounds(menuLoc);
		this.add(menuButton, 4, 0);
		menuButton.setVisible(false);
		
		tutBoatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tutBoatButtonActionPerformed();
			}
		});
		
		tutCrabButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tutCrabButtonActionPerformed();
			}
		});
		
		tutCubeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tutCubeButtonActionPerformed();
			}
		});
	}

	protected void menuButtonActionPerformed() {
		// TODO Auto-generated method stub
		if (prevController != null) prevController.dispose();
		menuButton.setVisible(false);
		tutorialBoat.setVisible(false);
		tutorialCrab.setVisible(false);
		tutorialCube.setVisible(false);
		crabButton.setVisible(true);
		boatButton.setVisible(true);
		cubeButton.setVisible(true);
		tutBoatButton.setVisible(true);
		tutCrabButton.setVisible(true);
		tutCubeButton.setVisible(true);
		
	}

	protected void tutBoatButtonActionPerformed() {
		// TODO Auto-generated method stub
		menuButton.setVisible(true);
		crabButton.setVisible(false);
		cubeButton.setVisible(false);
		boatButton.setVisible(true);
		tutBoatButton.setVisible(false);
		tutCrabButton.setVisible(false);
		tutCubeButton.setVisible(false);
		//tutorial.setButton(boatButton.getX(), boatButton.getY(), boatButton.getWidth(), boatButton.getHeight());
		tutorialBoat.setVisible(true);
		tutorialCrab.setVisible(false);
		tutorialCube.setVisible(false);
	}
	
	protected void tutCrabButtonActionPerformed() {
		// TODO Auto-generated method stub
		menuButton.setVisible(true);
		crabButton.setVisible(true);
		cubeButton.setVisible(false);
		boatButton.setVisible(false);
		tutBoatButton.setVisible(false);
		tutCrabButton.setVisible(false);
		tutCubeButton.setVisible(false);
		//tutorial.setButton(boatButton.getX(), boatButton.getY(), boatButton.getWidth(), boatButton.getHeight());
		tutorialBoat.setVisible(false);
		tutorialCrab.setVisible(true);
		tutorialCube.setVisible(false);
	}
	
	protected void tutCubeButtonActionPerformed() {
		// TODO Auto-generated method stub
		menuButton.setVisible(true);
		crabButton.setVisible(false);
		cubeButton.setVisible(true);
		boatButton.setVisible(false);
		tutBoatButton.setVisible(false);
		tutCrabButton.setVisible(false);
		tutCubeButton.setVisible(false);
		//tutorial.setButton(boatButton.getX(), boatButton.getY(), boatButton.getWidth(), boatButton.getHeight());
		tutorialBoat.setVisible(false);
		tutorialCrab.setVisible(false);
		tutorialCube.setVisible(true);
	}

	private void switchGame(MGController controller) {
		//TODO wave.run
		if (prevController != null) {
			prevController.dispose();
		}

		prevController = controller;

		if (prevController instanceof BoatController) {
			//System.out.println("boat controller");
			BoatController temp = (BoatController) prevController;
			temp.timer.start();
		} else if (prevController instanceof CrabController) {
			//System.out.println("crab controller");
		} else if (prevController instanceof CubeController) {
			CubeController temp = (CubeController) prevController;
			temp.getTimer().start();
			temp.getWorld().rollDice();
		}

		gameView = prevController.getView();

		if (gameView instanceof BoatGameScreen) {
			//System.out.println("boat crabGamePanel");
		} else if (gameView instanceof CrabGamePanel) {
			//System.out.println("crab panel");
		}

		gameView.setVisible(true);
		menuButton.setVisible(true);
		this.add(gameView, 3, 0);
	}

}

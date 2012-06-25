package game;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Launcher extends JFrame {

	public Game game;

	/**
	 * Create Window with Game Canvas Element
	 */
	public Launcher() {
		this.setTitle("Bomberman");
		this.game = Game.getInstance();
		JPanel panel = new JPanel(new GridLayout());
		panel.add(this.game);
		this.setLocation(500, 100);

		new GUI(this);

		this.setContentPane(panel);

		this.setResizable(false);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.game.start();
	}

}

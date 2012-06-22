package game.highscore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighscoreManager {
	// An arraylist of the type "score" we will use to work with the scores
	// inside the class
	private ArrayList<Score> scores;

	// The name of the file where the highscores will be saved
	private static final String HIGHSCORE_FILE = "src/ressources/scores.dat";

	// Initialising an in and outputStream for working with the file
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;

	public HighscoreManager() {
		// initialising the scores-arraylist
		this.scores = new ArrayList<Score>();
	}

	public ArrayList<Score> getScores() {
		this.loadScoreFile();
		this.sort();
		return this.scores;
	}

	private void sort() {
		ScoreComparator comparator = new ScoreComparator();
		Collections.sort(this.scores, comparator);
	}

	public void addScore(String name, int score) {
		this.loadScoreFile();
		// if (score >= this.scores.) {
		this.scores.add(new Score(name, score));

		this.updateScoreFile();
	}

	public void loadScoreFile() {
		try {
			this.inputStream = new ObjectInputStream(new FileInputStream(
					HighscoreManager.HIGHSCORE_FILE));
			this.scores = (ArrayList<Score>) this.inputStream.readObject();
		} catch (FileNotFoundException e) {
			System.out.println("[Laad] FNF Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("[Laad] IO Error: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("[Laad] CNF Error: " + e.getMessage());
		} finally {
			try {
				if (this.outputStream != null) {
					this.outputStream.flush();
					this.outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Laad] IO Error: " + e.getMessage());
			}
		}
	}

	public void updateScoreFile() {
		try {
			this.outputStream = new ObjectOutputStream(new FileOutputStream(
					HighscoreManager.HIGHSCORE_FILE));
			this.outputStream.writeObject(this.scores);
		} catch (FileNotFoundException e) {
			System.out.println("[Update] FNF Error: " + e.getMessage()
					+ ",the program will try and make a new file");
		} catch (IOException e) {
			System.out.println("[Update] IO Error: " + e.getMessage());
		} finally {
			try {
				if (this.outputStream != null) {
					this.outputStream.flush();
					this.outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Update] Error: " + e.getMessage());
			}
		}
	}

	public String getHighscoreString() {
		String highscoreString = "";
		int max = 10;

		ArrayList<Score> scores;
		scores = this.getScores();

		int i = 0;
		int x = scores.size();
		if (x > max) {
			x = max;
		}
		while (i < x) {
			highscoreString += (i + 1) + ".\t" + scores.get(i).getNaam()
					+ "\t\t" + scores.get(i).getScore() + "\n";
			i++;
		}
		return highscoreString;
	}
}
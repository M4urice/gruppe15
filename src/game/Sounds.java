package game;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sounds extends Thread {
	public Sounds(String filename) {
		this.filename = filename;

	}

	private String filename;

	@Override
	public void run() {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(Main.class.getResource("/ressources/"
							+ this.filename));
			AudioFormat af = audioInputStream.getFormat();
			int size = (int) (af.getFrameSize() * audioInputStream
					.getFrameLength());
			byte[] audio = new byte[size];
			DataLine.Info info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);

			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(af, audio, 0, size);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
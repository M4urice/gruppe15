package entities;

import game.Game;
import graphics.Sprite;

import java.awt.Graphics;

public class Wall extends Entity {

	public Wall(int x, int y) {
		super(x, y);
		this.images = Sprite.load("brick_solid_small.png", 100, 100);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage((this.images[0][0]).image, this.x, this.y, Game.BLOCK_SIZE, Game.BLOCK_SIZE, null);
	}

}

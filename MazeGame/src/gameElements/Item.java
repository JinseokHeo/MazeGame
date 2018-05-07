package gameElements;

import java.awt.Graphics;

public abstract class Item{

	private int locX, locY;
	
	
	public Item(int locX, int locY) {
		this.locX = locX;
		this.locY = locY;
	}
	
	public abstract void consume(Sprite sprite);
	
	public abstract void draw(Graphics g);
}

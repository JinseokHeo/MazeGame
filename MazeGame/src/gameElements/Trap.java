package gameElements;
import java.awt.Graphics;
import java.awt.Image;

public abstract class  Trap {
	
	private int x, y;
	private int width = 15;
	private int height = 10;
	private static Image image;
	
	public Trap() {
		x = -1;
		y = -1;
		
		this.image = image;
	}
	
	public Trap(int x, int y, Image image) {
		this.x = x;
		this.y = y;
		
		this.image = image;
	}
	
	public void draw(Graphics g) {
		if(!(x == -1 || y == -1)) {
			g.drawImage(image, x, y, width, height, null);
		}
	}
	
	public void setTrap(int x, int y) {
		this.x = x-width/2;
		this.y = y-height/2;
	}

	public boolean isCollided(int SpriteX, int SpriteY) {
		if(SpriteX < x || SpriteX >= x + width || SpriteY < y || SpriteY >= y + height) {
			return true;
		}
		return false;
	}
}

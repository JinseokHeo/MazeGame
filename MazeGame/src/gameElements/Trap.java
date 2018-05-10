package gameElements;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class  Trap {
	
	private int x, y;
	private int width = 15;
	private int height = 10;
	private static Image image;
	
	public Trap() {
		x = -1;
		y = -1;
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

	public void setImage(Image image) {
		this.image = image;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }
}

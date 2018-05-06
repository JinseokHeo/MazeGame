
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Maze {

	public static final int THICKNESS = 7;
	private BufferedImage image;
	
	
	public Maze(){
		try {
			image = ImageIO.read(new File("maze.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void draw(Graphics g){
		g.drawImage(image, 0, 0, null);
		
	}
	
	

	
	
	public boolean hitWall(int x, int y){
		if(x<0 || x>=image.getWidth() || y<0 || y>=image.getHeight()) return false;
		
		if((image.getRGB(x, y)>>>24)>128) return true;
		//if(((image.getRGB(x, y)>>24) & 0xff)>128) return true;
		return false;
	}

	
	public void setPixel(int x, int y, int alpha, int red, int green, int blue){
		image.setRGB(x, y, (alpha<<24) | (red<<16) | (green<<8) | (blue<<0)); 
	}
	
	
	public void setPixelsInBox(int x, int y, int w, int h, int alpha, int red, int green, int blue){
		int color = (alpha<<24) | (red<<16) | (green<<8) | (blue<<0);
		int i, j;
		for(i=x; i<=x+w; i++){
			for(j=y; j<=y+h; j++){
				if(i>=0 && i<image.getWidth() && j>=0 && j<image.getHeight()) image.setRGB(i, j, color); 
			}
		}
	}
}




















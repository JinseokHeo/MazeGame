
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;


public class Sprite implements KeyListener, Runnable {

	public static Image imageE, imageW, imageS, imageN;
	public static int size;
	
	private int x, y; // center of the mouse
	private int stepSize; // speed of mouse movement.
	private int xSpeed, ySpeed; // +1, 0, -1.
	private Image image; 
	
	private MessageSender mSender;
	private boolean shouldBreakWall;
	
	
	static {
		imageE = new ImageIcon("tinyMouseE.png").getImage();
		imageW = new ImageIcon("tinyMouseW.png").getImage();
		imageS = new ImageIcon("tinyMouseS.png").getImage();
		imageN = new ImageIcon("tinyMouseN.png").getImage();
		size = imageN.getWidth(null);

	}
	 
	
	
	public Sprite(int x, int y, int xSpeed, int ySpeed, int stepSize){
		this.x = x;
		this.y = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.stepSize = stepSize;
		
		image = imageN;
		selectImage();
	}
	
	
	public void setMessageSender(MessageSender mSender){ this.mSender = mSender; }
	
	
	private void selectImage(){
		if(xSpeed>0) image = imageE;
		else if(xSpeed<0) image = imageW;
		else if(ySpeed>0) image = imageS;
		else if(ySpeed<0) image = imageN;
	}
	
	
	public void move(){
		x += xSpeed*stepSize;
		y += ySpeed*stepSize;
	}
	
	
	public void draw(Graphics g){
		g.drawImage(image, x-size/2, y-size/2, null);
	}


	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
			xSpeed = -1;
			ySpeed = 0;
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			xSpeed = 1;
			ySpeed = 0;
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP){
			xSpeed = 0;
			ySpeed = -1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			xSpeed = 0;
			ySpeed = 1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_B){
			shouldBreakWall = true;
		}
		
		selectImage();
	}
	
	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	
	public void update(String message){
		int commaPos1, commaPos2, commaPos3, commaPos4;
		commaPos1 = message.indexOf(',');
		x = Integer.parseInt(message.substring(0, commaPos1));
		commaPos2 = message.indexOf(',', commaPos1+1);
		y = Integer.parseInt(message.substring(commaPos1+1, commaPos2));
		commaPos3 = message.indexOf(',', commaPos2+1);
		xSpeed = Integer.parseInt(message.substring(commaPos2+1, commaPos3));
		commaPos4 = message.indexOf(',', commaPos3+1);
		ySpeed = Integer.parseInt(message.substring(commaPos3+1, commaPos4));
		shouldBreakWall = Boolean.parseBoolean(message.substring(commaPos4+1));
		
		
		selectImage();
	}
	
	
	
	
	
	
	public void run(){
		while(true){
			x+=xSpeed;
			y+=ySpeed;
				
			//send x, y, xspeed, ySpeed over the network.
			mSender.send(x + "," + y + "," + xSpeed + "," + ySpeed + "," + shouldBreakWall);

			
			// check collision any prize.
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {}
		}
	}
	
	
}











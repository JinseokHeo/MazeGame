package gameElements;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AgilityPotion extends Item implements ActionListener{

	private double quality;
	private Timer timer;
	private Sprite sprite;
	
	public AgilityPotion(int locX, int locY) {
		super(locX, locY);
		quality=Math.random()*5+1;
		
	}

	
	public void consume(Sprite sprite) {
		this.sprite = sprite;
		sprite.increaseStep(quality);
		timer = new Timer(5000, this);
		timer.start();
	}



	public void actionPerformed(ActionEvent arg0) {
		sprite.increaseStep(-quality);
		timer=null;
	}


	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}

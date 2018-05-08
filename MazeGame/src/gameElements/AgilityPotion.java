package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AgilityPotion extends Item{

	private double quality;
	
	public AgilityPotion(int locX, int locY) {
		super(locX, locY);
		quality=Math.random()*5+1;
	}

	
	public void consume(Sprite sprite) {
		int delay = 5000; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
//				System.out.println("5 second has passed");
				sprite.increaseStep(quality);

			}
		};
		Timer timer = new Timer(delay, taskPerformer);
		timer.start();
		timer.setRepeats(false);
		
	}





	@Override
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.drawOval(5, 5, 5, 5);
		
	}

}

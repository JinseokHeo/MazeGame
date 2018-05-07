package general;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;


public class FancyDrawingBoard extends JPanel {

	private JFrame frame;
	private int width, height;
	
	private BufferedImage bImage, bImage2;
	private Graphics canvas, canvas2;
	private Image bgImage;
	
	
	public FancyDrawingBoard(String title, int x, int y, Image backgroundImage){
		this(title, x, y, backgroundImage.getWidth(null), backgroundImage.getHeight(null));
		bgImage = backgroundImage;
	}
	
	
	
	public FancyDrawingBoard(String title, int x, int y, int width, int height){
	
		this.width = width;
		this.height = height;
		
		frame = new JFrame(title);
		//frame.setLocation(x, y);
		frame.setBounds(x, y, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
		
		this.setBounds(0, 0, width, height);
		frame.getContentPane().add(this); 
		this.setLayout(null); 
		
		frame.pack(); 
		frame.setVisible(true);
		
		//for(int i=0; i<buttons.length; i++) buttons[i].setFocusable(false);
		
		frame.setFocusable(true);
		frame.requestFocus();
		
		
		bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		canvas = bImage.getGraphics();
		((Graphics2D)canvas).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		bImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		canvas2 = bImage2.getGraphics();
		((Graphics2D)canvas2).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	
	
	public void registerKeyListener(KeyListener listener){
		frame.addKeyListener(listener);
	}
	
	
	public void clearUpperLayer(){
		int i, j;
		for(i=0; i<width; i++){
			for(j=0; j<height; j++){
				bImage2.setRGB(i, j, 0x00000000);
			}
		}
	}
	
	public void clear(){
		if(bgImage==null){
			canvas.setColor(Color.white);;
			canvas.fillRect(0, 0, width, height);
		}
		else {
			canvas.drawImage(bgImage, 0, 0, null);
		}
	}
	
	
	
	public Graphics getLowerCanvas(){
		return canvas;
	}
	
	
	public Graphics getUpperCanvas(){
		return canvas2;
	}
	
	
	public void paintComponent(Graphics g){
		g.drawImage(bImage, 0,  0, this);
		g.drawImage(bImage2, 0,  0, this);
	}
	
	
}





























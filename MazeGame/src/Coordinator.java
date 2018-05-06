
import java.awt.Graphics;


public class Coordinator {


	public static void main(String[] args) {

		FancyDrawingBoard board = 
				new FancyDrawingBoard("Maze and Mouse", 200, 100, 500, 500);
		Graphics g = board.getLowerCanvas();
		Sprite myMouse = new Sprite(200, 200, 1, 0, 1);
		Sprite yourMouse = new Sprite(40, 40, 0, 0, 1);
		
		board.registerKeyListener(myMouse);
		
		Thread mouseThread = new Thread(myMouse);
		mouseThread.start();
		
		while(true){
			
			board.clear();
			myMouse.draw(g); 
			yourMouse.draw(g);
			board.repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {} 
		}

	}

}






























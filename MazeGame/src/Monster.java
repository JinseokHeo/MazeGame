
import java.awt.Graphics;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Monster {

	private static int portNumber = 5454;
	private static MessageSender mSender;
	private static MessageReceiver mReceiver;
	
	
	private static void connect(){
		
		try {
			Socket s = new ServerSocket(portNumber).accept();
			mSender = new MessageSender(s.getOutputStream());
			mReceiver = new MessageReceiver(s.getInputStream());
			new Thread(mReceiver).start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		
		connect();
		
		Sprite myMouse = new Sprite(175, 350, 0, 0, 1);
		Sprite enemyMouse = new Sprite(275, 200, 0, 0, 1);
		Sprite s = new Sprite(275, 200, 0, 0, 1);
		
		FancyDrawingBoard board = new FancyDrawingBoard("Server", 200, 50, 500, 500);
		board.registerKeyListener(myMouse);
		Graphics g = board.getLowerCanvas();
		
		
		myMouse.setMessageSender(mSender);
		mReceiver.setEnemyMouse(enemyMouse);
		mReceiver.setEnemyMouse(s);

		
		new Thread(myMouse).start();
		
		while(true){
			
			board.clear();
			myMouse.draw(g);
			enemyMouse.draw(g);
			s.draw(g);
			board.repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}













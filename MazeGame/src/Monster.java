
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
	
	
	
	public Monster() {
		
		connect();
		
		Sprite monster = new Sprite(275, 25, 0, 0, 1, false);
		Sprite player = new Sprite(275, 475, 0, 0, 1, true);
		
		FancyDrawingBoard board = new FancyDrawingBoard("Monster", 200, 50, 500, 500);
		board.registerKeyListener(monster);
		Graphics g = board.getLowerCanvas();
		
		Maze maze = new Maze();
		monster.setMaze(maze);
		player.setMaze(maze);
		
		monster.setMessageSender(mSender);
		mReceiver.setEnemy(player);

		
		new Thread(monster).start();
		
		while(true){
			board.clear();
			monster.draw(g);
			player.draw(g);
			maze.draw(g);

			board.repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}













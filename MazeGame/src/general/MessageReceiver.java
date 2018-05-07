package general;

import java.io.*;

import gameElements.Sprite;


public class MessageReceiver implements Runnable {

	private Sprite enemy;
	private BufferedReader br;
	
	
	public MessageReceiver(InputStream is){ // socketInputStream
		br = new BufferedReader(new InputStreamReader(is));
	}
	
	
	public void setEnemy(Sprite enemy){ this.enemy = enemy; }
	
	
	public void run(){
		String message;
		while(true){
			try {
				message = br.readLine();
				if(enemy!=null) enemy.update(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}

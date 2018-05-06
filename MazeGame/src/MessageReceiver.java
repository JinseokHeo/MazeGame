
import java.io.*;


public class MessageReceiver implements Runnable {

	private Sprite enemyMouse;
	private BufferedReader br;
	
	
	public MessageReceiver(InputStream is){ // socketInputStream
		br = new BufferedReader(new InputStreamReader(is));
	}
	
	
	public void setEnemyMouse(Sprite enemyMouse){ this.enemyMouse = enemyMouse; }
	
	
	public void run(){
		String message;
		while(true){
			try {
				message = br.readLine();
				if(enemyMouse!=null) enemyMouse.update(message);
			} catch (IOException e) {
				//e.printStackTrace();
			}
			
		}
	}
	
	
}

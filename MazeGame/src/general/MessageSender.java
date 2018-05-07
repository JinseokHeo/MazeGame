package general;


import java.io.*;


public class MessageSender {

	private PrintWriter pw;
	
	public MessageSender(OutputStream os){ // socketOutputStream
		pw = new PrintWriter(new OutputStreamWriter(os));
	}
	
	
	public void send(String message){
		pw.println(message);
		pw.flush();
	}
	
}

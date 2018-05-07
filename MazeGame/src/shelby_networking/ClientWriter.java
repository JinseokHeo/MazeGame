/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shelby_networking;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author john_shelby
 */
public class ClientWriter implements Runnable{

	private static final int RETRY_TIMEOUT = 10;
	private static final int WAIT_TIME = 50;
	
    private Socket s;
    private InetAddress host;
    private ObjectOutputStream out;
    private Queue<Object[]> messageQueue;
    private boolean looping;

    public ClientWriter(Socket s) {
        this.s = s;
        host = s.getInetAddress();
        messageQueue = new LinkedList<Object[]>();
        try {
            BufferedOutputStream os = new BufferedOutputStream(s.getOutputStream());
            out = new ObjectOutputStream(os);
            out.flush();
            new Thread(this).start();
        } catch(IOException e) {
            System.err.println("Error connecting output stream.");
            e.printStackTrace();
        }
    }
    
    
    public InetAddress getHost() {
    	return host;
    }
    
    public void sendMessage(Object... message) {
    	messageQueue.add(message);
    }
    
    public void stop() {
    	looping = false;
    }
    
    public boolean isConnected() {
    	return looping;
    }

    public void run() {

    	looping = true;
        try {
        	int tries = 0;
            while(looping) {
            	long startTime = System.currentTimeMillis();
            	
            	try {
            		while (!messageQueue.isEmpty())
            			out.writeObject(messageQueue.poll());
                    out.flush();
                    out.reset();
                    tries = 0;
                } catch (IOException e) {
                	tries++;
                	if (tries >= RETRY_TIMEOUT) {
                		looping = false;
                	}
                    e.printStackTrace();
                }
            	
                long waitTime = WAIT_TIME - (System.currentTimeMillis() - startTime);
                
                if (waitTime > 0) {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {}
                } else Thread.yield();
                
            }
        } finally {
            try {
                if (out != null)
                    out.close();
                if (!s.isClosed())
                    s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }



}

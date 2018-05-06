/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 *
 * @author john_shelby
 */
public class ClientReader implements Runnable{

	private static final int RETRY_TIMEOUT = 10;
	
    private Socket s;
    private ObjectInputStream in;
    private InetAddress host;
    private boolean looping;
    
    private ArrayList<NetworkListener> listeners;
    

    public ClientReader(Socket s, ArrayList<NetworkListener> listeners) {
        this.s = s;
        this.listeners = listeners;
        host = s.getInetAddress();
        
        try {
            in = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
            new Thread(this).start();
        } catch(IOException e) {
            System.err.println("Error connecting input stream.");
            e.printStackTrace();
        }
    }
    
    public InetAddress getHost() {
    	return host;
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

                try {
                	Serializable data = (Serializable) in.readObject();
                    if (data instanceof Object[]) {
                    	synchronized(listeners) {
	                    	for (NetworkListener nl : listeners) {
	                    		SwingUtilities.invokeLater(new Runnable() {
	                    		    public void run() {
	                    		    	nl.receiveUpdate(host.getHostAddress(),(Object[])data);
	                    		    }
	                    		});
	                    	}
                    	}
                    }

                    tries = 0;
                } catch (IOException e) {
                	tries++;
                	if (tries >= RETRY_TIMEOUT) {
                		looping = false;
                	}
                    e.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    System.exit(0);
                }

            }
            for (NetworkListener nl : listeners)
        		nl.receiveUpdate(host.getHostAddress(),new Object[] {NetworkListener.DISCONNECT});

        } finally {
            try {
                if (in != null)
                    in.close();
                if (!s.isClosed())
                    s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

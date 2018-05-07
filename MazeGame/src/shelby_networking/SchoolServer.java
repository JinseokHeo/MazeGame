/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shelby_networking;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


/**
 *
 * @author john_shelby
 */
public class SchoolServer {

	private int port;
	
    private boolean listening;
    private boolean peerToPeerMode;
    
    private List<ClientWriter> writers;
    private List<ClientReader> readers;
    private ArrayList<NetworkListener> listeners;

    public SchoolServer(boolean peerToPeerMode, int port) {
    	this.port = port;
    	this.peerToPeerMode = peerToPeerMode;
        listening = false;
        this.writers = new ArrayList<ClientWriter>();
        this.readers = new ArrayList<ClientReader>();
        listeners = new ArrayList<NetworkListener>();
        addNetworkListener(new NetworkListener() {
        	@Override
        	public void receiveUpdate(String hostname, Object[] message) {
        		
        		new Thread(new Runnable() {

        			@Override
        			public void run() {

        				if (message[0].equals(NetworkListener.DISCONNECT)) {
        					synchronized(SchoolServer.this) {
        						try {
        							InetAddress address = InetAddress.getByName(hostname);
        							for (int i = writers.size()-1; i >= 0; i--) {	
        								if (writers.get(i).getHost().equals(address))
        									writers.remove(i).stop();
        							}
        							for (int i = readers.size()-1; i >= 0; i--) {
        								if (readers.get(i).getHost().equals(address))
        									readers.remove(i).stop();
        							}
        						} catch (UnknownHostException e) {
        							// TODO Auto-generated catch block
        							e.printStackTrace();
        						}
        					}
        				} else if (message[0].equals(NetworkListener.HANDSHAKE) && SchoolServer.this.peerToPeerMode) {
        					for (int i = 1; i < message.length; i++) {
        						String host = (String)message[i];
        						connect(host);
        					}
        				}
        			}

        		}).start();
        		
        	}
        });
    }
    
    public synchronized void sendMessage(Object... message) {
		for (ClientWriter cw: writers) {
			cw.sendMessage(message);
		}
    }
    
    public void addNetworkListener(NetworkListener nl) {
    	synchronized(listeners) {
    		listeners.add(nl);
    	}
    }
    
    public synchronized InetAddress[] getConnectedHosts() {
    	ArrayList<InetAddress> ips = new ArrayList<InetAddress>();
    	for (ClientReader cr : readers) {
    		if (cr.isConnected())
    			ips.add(cr.getHost());
    	}
    	return ips.toArray(new InetAddress[ips.size()]);
    }
    
    		
    public synchronized void disconnectFromClient(InetAddress host) {

		for (ClientWriter cw : writers) {
    		if (cw.getHost().equals(host))
    			cw.stop();
    	}
    	for (ClientReader cr : readers) {
    		if (cr.getHost().equals(host))
    			cr.stop();
    	
    	}
    }
    		
    public synchronized void disconnectFromClient(String host) {
    	
		try {
			InetAddress hostAdd = InetAddress.getByName(host);
			disconnectFromClient(hostAdd);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public synchronized void disconnectFromAllClients() {
    	for (ClientWriter cw : writers)
    		cw.stop();
    	for (ClientReader cr : readers)
    		cr.stop();
    }
    
    
    
    public synchronized void shutdownServer() {
    	listening = false;
    }
    
    public boolean connect(InetAddress host) {
    	try {
        	
        	if (peerToPeerMode) {
        		if (InetAddress.getLocalHost().equals(host) || InetAddress.getLoopbackAddress().equals(host)) 
        			return false;
    			synchronized(this) {
					for (ClientReader cr : readers) {
						if (cr.getHost().equals(host)) {	
							return false;
						}
					}
    			}
        	}
        	
        	Socket s = new Socket(host, port);
            s.setKeepAlive(true);
            
            System.out.println("Client connected to " + s.getInetAddress().getHostAddress());
            
            ClientReader cr = new ClientReader(s,listeners);
            ClientWriter cw = new ClientWriter(s);
            
        	sendHandshake(cw);
            
	        synchronized(this) {    
	        	writers.add(cw);
	        	readers.add(cr);
            }
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }
    

    public boolean connect(String host) {
    	try {
			return connect(InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    

    public void waitForConnections() {
        
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			ServerSocket serverSocket = null;
    	        listening = true;
    	        
    	        try {
    	            serverSocket = new ServerSocket(port);

    	            while (listening) {
    	                Socket s = serverSocket.accept();
    	                
    	                System.out.println("Server connected to " + s.getInetAddress().getHostAddress());
    	                
    	            	ClientWriter cw = new ClientWriter(s);
    	            	ClientReader cr = new ClientReader(s,listeners);
    	            	
    	            	if (peerToPeerMode) {
    	                	sendHandshake(cw);
    	                }
    	            	
    	            	synchronized(this) {
    	                	writers.add(cw);
    	                	readers.add(cr);
    	                }
    	            }
    	                
    	            serverSocket.close();

    	        } catch (IOException e) {
    	            System.err.println("Could not connect to client on port: 4444.");
    	        }
    			
    		}
    	}).start();
    }


    protected synchronized void sendHandshake(ClientWriter cw) {
    	Object[] message = new Object[readers.size()+1];
    	message[0] = NetworkListener.HANDSHAKE;
    	for (int i = 0; i < readers.size(); i++)
    		message[i+1] = readers.get(i).getHost().getHostAddress();
    	cw.sendMessage(message);
    }
	


}

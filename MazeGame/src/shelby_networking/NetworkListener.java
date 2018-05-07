package shelby_networking;

public interface NetworkListener {

	public static final String HANDSHAKE = "H";
	public static final String DISCONNECT = "D";
	public static final String MESSAGE = "M";
	
	
	public void receiveUpdate(String hostname, Object[] message);
	
}

package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import Networking.NetworkListener;
import Networking.PeerDiscovery;
import Networking.SchoolServer;

public class NetworkManagementPanel extends JPanel implements ActionListener, NetworkListener
{

	private static final int TCP_PORT = 4444;
	private static final int BROADCAST_PORT = 4444;
	private static final boolean PEER_TO_PEER_MODE = true;
	private static final int DISCOVERY_TIMEOUT = 15;

	private JTextArea statusText;
	private JList<InetAddress> hostList, connectedList;

	private JProgressBar discoveryProcess; 

	private JButton connectButton;
	private JButton discoverButton;
	private JButton disconnectButton;

	private JButton connectCustomButton;

	private InetAddress myIP;
	private PeerDiscovery discover;
	private SchoolServer ss;

	private Timer refreshTimer;
	private int timeOut;

	public NetworkManagementPanel () {
		refreshTimer = new Timer(1000, this);
		setLayout(new BorderLayout());

		JPanel cPanel = new JPanel();
		statusText = new JTextArea();
		statusText.setEditable(false);
		JScrollPane pane = new JScrollPane(statusText);
		pane.setPreferredSize(new Dimension(100,100));
		pane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		cPanel.setLayout(new BorderLayout());
		cPanel.add(pane, BorderLayout.NORTH);
		add(cPanel, BorderLayout.CENTER);

		JPanel cssPanel = new JPanel();
		cssPanel.setLayout(new GridLayout(1,2));
		cPanel.add(cssPanel,BorderLayout.CENTER);

		JPanel cnPanel = new JPanel();
		cnPanel.setLayout(new BorderLayout());
		hostList = new JList<InetAddress>();
		cnPanel.add(hostList,BorderLayout.CENTER);
		JLabel ah = new JLabel("Available Hosts");
		ah.setHorizontalAlignment(JLabel.CENTER);
		cnPanel.add(ah,BorderLayout.NORTH);
		cnPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		cssPanel.add(cnPanel);

		JPanel csPanel = new JPanel();
		csPanel.setLayout(new BorderLayout());
		connectedList = new JList<InetAddress>();
		csPanel.add(connectedList,BorderLayout.CENTER);
		JLabel ch = new JLabel("Connected Hosts");
		ch.setHorizontalAlignment(JLabel.CENTER);
		csPanel.add(ch,BorderLayout.NORTH);
		csPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		cssPanel.add(csPanel);

		JPanel bPanel = new JPanel();
		bPanel.setLayout(new GridLayout(2,1));

		JPanel ePanel = new JPanel();
		ePanel.setLayout(new GridLayout(1,5,15,15));
		discoverButton = new JButton("Discover");
		discoverButton.addActionListener(this);
		connectButton = new JButton("Connect to Selected");
		connectButton.addActionListener(this);
		disconnectButton = new JButton("Disconnect All");
		disconnectButton.addActionListener(this);
		connectCustomButton = new JButton("Connect to Custom IP");
		connectCustomButton.addActionListener(this);
		ePanel.add(discoverButton);
		ePanel.add(connectButton);
		ePanel.add(connectCustomButton);
		ePanel.add(disconnectButton);

		discoveryProcess = new JProgressBar();
		bPanel.add(ePanel);
		bPanel.add(discoveryProcess);

		cPanel.add(bPanel,BorderLayout.SOUTH);


		try {
			myIP = InetAddress.getLocalHost();
			statusText.append("Your Hostname/IP address is " + myIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			statusText.append("Error getting your IP address!");
		}

		try {
			discover = new PeerDiscovery(InetAddress.getByName("255.255.255.255"),BROADCAST_PORT);
			statusText.append("\nBroadcast discovery server running on " + BROADCAST_PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
			statusText.append("\nError starting broadcast discovery server on port " + BROADCAST_PORT);
		}

		ss = new SchoolServer(PEER_TO_PEER_MODE,TCP_PORT);
		ss.addNetworkListener(this);
		ss.waitForConnections();
		statusText.append("\nTCP server running on " + TCP_PORT);

		statusText.append("\nPeer-to-peer mode is " + (PEER_TO_PEER_MODE ? "ON" : "OFF"));


		JFrame window = new JFrame("Network Management");
		window.setBounds(300, 300, 700, 480);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(this);
		window.setVisible(true);
	}


	public SchoolServer getMessageServer() {
		return ss;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == discoverButton) {
			try {
				statusText.append("\nSending broadcast packet...");
				discover.sendDiscoveryPacket();
				timeOut = DISCOVERY_TIMEOUT;
				refreshTimer.restart();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} else if (source == connectButton) {
			if (PEER_TO_PEER_MODE && ss.getConnectedHosts().length > 0) {
				int answer = JOptionPane.showConfirmDialog(this, "Because peer-to-peer mode is on, this will cause you to connect to ALL of this clients other connections (and visa-versa). This will essentially join your networks. Is this what you want?","Join Networks?",JOptionPane.YES_NO_OPTION);
				if (answer != JOptionPane.YES_OPTION)
					return;
			}
			InetAddress host = hostList.getSelectedValue();
			if (host != null) {
				boolean success = ss.connect(host);
				if (!success) {
					JOptionPane.showMessageDialog(this, "Could not connect!");
				}
			}
		} else if (source == connectCustomButton) {
			String host = JOptionPane.showInputDialog("What IP?");
			if (host != null) {
				boolean success = ss.connect(host);
				if (!success) {
					JOptionPane.showMessageDialog(this, "Could not connect!");
				}
			}
		} else if (source == disconnectButton) {
			ss.disconnectFromAllClients();
		} else if (source == refreshTimer) {
			timeOut--;
			discoveryProcess.setValue((int)((double)(DISCOVERY_TIMEOUT-timeOut)/DISCOVERY_TIMEOUT*100));
			if (timeOut <= 0) {
				refreshTimer.stop();
				statusText.append("\nFinished discovery.");
			}
			hostList.setListData(discover.getPeers());
		}


	}


	@Override
	public void receiveUpdate(String hostname, Object[] message) {
		if (message[0].equals(NetworkListener.HANDSHAKE)) {
			statusText.append("\nConnected to " + hostname);
			connectedList.setListData(ss.getConnectedHosts());
		} else if (message[0].equals(NetworkListener.DISCONNECT)) {
			statusText.append("\nDisconnected from " + hostname);
			connectedList.setListData(ss.getConnectedHosts());
		}

	}
}
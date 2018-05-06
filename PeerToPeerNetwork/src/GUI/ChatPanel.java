package GUI;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Networking.NetworkListener;
import Networking.SchoolServer;

import java.util.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ChatPanel extends JPanel implements ActionListener, NetworkListener
{
  
  private JTextArea inText;
  private JTextField outText;
  private JButton sendButton;
  
  private SchoolServer ss;
	
  public ChatPanel (SchoolServer ss) {
	  this.ss = ss;
	  inText = new JTextArea();
	  inText.setEditable(false);
	  outText = new JTextField();
	  sendButton = new JButton("Send");
	  sendButton.addActionListener(this);
	  
	  setLayout(new BorderLayout());
	  JScrollPane scroll = new JScrollPane(inText);
	  add(scroll,BorderLayout.CENTER);
	  
	  JPanel bottomPanel = new JPanel();
	  bottomPanel.setLayout(new BorderLayout());
	  bottomPanel.add(outText, BorderLayout.CENTER);
	  bottomPanel.add(sendButton, BorderLayout.EAST);
	  
	  add(bottomPanel,BorderLayout.SOUTH);
	  
	  outText.addActionListener(this);
	  ss.addNetworkListener(this);
	  
	  JFrame window = new JFrame("Peer Chat");
	  window.setBounds(300, 300, 600, 480);
	  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  window.add(this);
	  window.setVisible(true);
	  
  }
  
  public void actionPerformed(ActionEvent e) {
	  Object source = e.getSource();
	  if (source == outText || source == sendButton) {
		if (!outText.getText().trim().equals("")) {
			String toGo = outText.getText().trim();
			ss.sendMessage(NetworkListener.MESSAGE,toGo);
			inText.append("\nYou: "+toGo);
			outText.setText("");
		}
	  }
	
	
  }
    


	@Override
	public void receiveUpdate(String hostname, Object[] message) {
		if (message[0].equals(NetworkListener.MESSAGE))
			inText.append("\n" + hostname + ": " + message[1]);
		else if (message[0].equals(NetworkListener.HANDSHAKE))
			inText.append("\n" + hostname + " connected. " + (message.length - 1) + " more incoming.");
		else if (message[0].equals(NetworkListener.DISCONNECT))
			inText.append("\n" + hostname + " disconnected. ");
		
	}
}

package general;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import gameElements.Maze;
import gameElements.Sprite;

public class Player {

	private static String IPAddress = "localhost";
	private static int portNumber = -1;
	private static MessageSender mSender;
	private static MessageReceiver mReceiver;

	private void getInfo() {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter monster IP address");
		IPAddress = in.nextLine();
		System.out.println("Enter in monster port number");
		portNumber = in.nextInt();
		connect();
		

		//		JFrame window = new JFrame("Enter game info");
		//
		//		window.setLayout(null);
		//		window.setBounds(300, 300, 300, 200);
		//
		//		JLabel hostNameLabel = new JLabel("Hostname: ");
		//		hostNameLabel.setBounds(50, 50, 75, 30);
		//		window.add(hostNameLabel);
		//
		//		JTextField hostNameTextField = new JTextField();
		//		hostNameTextField.setBounds(150, 50, 100, 30);
		//		window.add(hostNameTextField);
		//
		//		JLabel ipAddressLabel = new JLabel("IP Address: ");
		//		ipAddressLabel.setBounds(50, 25, 75, 30);
		//		window.add(ipAddressLabel);
		//
		//		JTextField ipAddressTextField = new JTextField();
		//		ipAddressTextField.setBounds(150, 25, 100, 30);
		//		window.add(ipAddressTextField);
		//
		//		JButton button = new JButton();
		//		button.setText("Enter");
		//		button.setBounds(50, 100, 100, 30);
		//		window.add(button);	
		//		button.addActionListener(new ActionListener() {
		//		    public void actionPerformed(ActionEvent e) {
		//		        if (button.isEnabled()) {
		//		        	IPAddress = ipAddressTextField.getText();
		//		        	portNumber = Integer.parseInt(hostNameTextField.getText());
		//		        	connect();
		//		        }
		//		    }
		//		});
		//		
		//		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		//		
		//		window.setVisible(true);
		//		while(true) {
		//			if(portNumber!=-1) break;
		//		}

	}

	private static void connect(){
		try {
			Socket s = new Socket(IPAddress, portNumber);
			mSender = new MessageSender(s.getOutputStream());
			mReceiver = new MessageReceiver(s.getInputStream());
			new Thread(mReceiver).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Player() {
		getInfo();

		System.out.println(portNumber);
		Sprite player = new Sprite(275, 475, 0, 0, 1, true); 
		Sprite monster = new Sprite(275, 25, 0, 0, 1, false);

		FancyDrawingBoard board = new FancyDrawingBoard("Player", 200, 50, 500, 500);
		board.registerKeyListener(player);
		Graphics g = board.getLowerCanvas();

		Maze maze = new Maze();
		player.setMaze(maze);
		monster.setMaze(maze);

		player.setMessageSender(mSender);
		mReceiver.setEnemy(monster);

		new Thread(player).start();

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



























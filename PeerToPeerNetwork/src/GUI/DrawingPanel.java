package GUI;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Networking.NetworkListener;
import Networking.SchoolServer;
import java.util.*;
import java.io.Serializable;


public class DrawingPanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener, NetworkListener, Runnable
{
  
  private SchoolServer ss;
  private JButton colorButton;
  
  private ArrayList<Cursor> cursors;
  private ArrayList<Cursor> trails;
  
  private Cursor me;
  
  private static final String messageTypeInit = "CREATE_CURSOR";
  private static final String messageTypeMove = "MOUSE_MOVE";
  private static final String messageTypePress = "MOUSE_PRESS";
  private static final String messageTypeColor = "COLOR_SWITCH";
	
  public DrawingPanel (SchoolServer ss) {
	  this.ss = ss;
	  setLayout(null);
	  setBackground(Color.WHITE);
	  
	  colorButton = new JButton("Color");
	  colorButton.addActionListener(this);
	  colorButton.setBounds(15, 35, 100, 25);
	  add(colorButton);
	  
	  addMouseMotionListener(this);
	  addMouseListener(this);
	  
	  cursors = new ArrayList<Cursor>();
	  trails = new ArrayList<Cursor>();
	  
	  me = new Cursor();
	  me.color = Color.BLACK;
	  me.host = "me!";
	  cursors.add(me);
	  
	  new Thread(this).start();
	  ss.addNetworkListener(this);
	  
	  JFrame window = new JFrame("Peer Draw");
	  window.setBounds(300, 300, 600, 480);
	  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  window.add(this);
	  window.setVisible(true);
	  
  }
  
  public void paintComponent(Graphics g) {
	 super.paintComponent(g);
	 
	 synchronized(trails) {
		 for (Cursor c : trails) {
			 g.setColor(new Color(c.color.getRed(), c.color.getGreen(), c.color.getBlue(), c.timeOut*255/Cursor.TIMEOUT_MAX));
			 g.fillOval(c.x-5, c.y-5, 10, 10);
		 }
	 }
	 
	 synchronized(cursors) {
		 for (Cursor c : cursors) {
			 g.setColor(c.color);
			 g.fillOval(c.x-5, c.y-5, 10, 10);
		 }
		 g.setColor(Color.BLACK);
		 g.drawString("Connected users: ", 10, 25);
		 int x = g.getFontMetrics().stringWidth("Connected users: ") + 25;
		 
		 for (Cursor c : cursors) {
			 g.setColor(c.color);
			 g.fillRect(x, 15, 10, 10);
			 x += 25;
		 }
	 }
	  
	  
  }
  
  public void actionPerformed(ActionEvent e) {
	  Object source = e.getSource();
	  if (source == colorButton) {
		  Color out = JColorChooser.showDialog(this, "Choose a color!", me.color);
		  if (out != null)
			  me.color = out;
		  ss.sendMessage(NetworkListener.MESSAGE, messageTypeColor, me.color );
	  }
	
	
  }
    


	@Override
	public void receiveUpdate(String hostname, Object[] message) {
		
		if (message[0].equals(NetworkListener.MESSAGE)) {
			if (message[1].equals(messageTypeMove)) {
				synchronized(cursors) {
					for (Cursor c : cursors) {
						if (c.host.equals(hostname)) {
							c.x = (Integer)message[2];
							c.y = (Integer)message[3];
						}
					}
				}
			} else if (message[1].equals(messageTypePress)) {
				synchronized(cursors) {
					for (Cursor c : cursors) {
						if (c.host.equals(hostname)) {
							c.x = (Integer)message[2];
							c.y = (Integer)message[3];
							c.makeTrailCopy();
						}
					}
				}
			} else if (message[1].equals(messageTypeInit)) {
				Cursor c = new Cursor();
				c.x = (Integer)message[2];
				c.y = (Integer)message[3];
				c.color = (Color)message[4];
				c.host = hostname;
				synchronized(cursors) {
					cursors.add(c);
				}
			} else if (message[1].equals(messageTypeColor)) {
				synchronized(cursors) {
					for (Cursor c : cursors) {
						if (c.host.equals(hostname)) {
							c.color = (Color)message[2];
						}
					}
				}
			}
		} else if (message[0].equals(NetworkListener.HANDSHAKE)) {
			ss.sendMessage(NetworkListener.MESSAGE, messageTypeInit, me.x, me.y, me.color);
		} else if (message[0].equals(NetworkListener.DISCONNECT)) {
			synchronized(cursors) {
				for (int i = cursors.size()-1; i >= 0; i--)
					if (cursors.get(i).host.equals(hostname))
						cursors.remove(i);
			}
		}
		repaint();
		
	}
	


	@Override
	public void run() {
		do {
			long startTime = System.currentTimeMillis();
			
			synchronized(trails) {
				for (int i = trails.size()-1; i >= 0; i--) {
					Cursor c = trails.get(i);
					c.timeOut--;
					if (c.timeOut <= 0)
						trails.remove(i);
				}
			}
			
			repaint();
			
			long endTime = System.currentTimeMillis();
			long waitTime = 200-(endTime-startTime);
			
			if (waitTime > 0) {
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else Thread.yield();
			
		} while (true);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		me.x = x;
		me.y = y;
		me.makeTrailCopy();
		ss.sendMessage(NetworkListener.MESSAGE, messageTypePress, x, y);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		me.x = x;
		me.y = y;
		ss.sendMessage(NetworkListener.MESSAGE, messageTypeMove, x, y);
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		me.x = x;
		me.y = y;
		me.makeTrailCopy();
		ss.sendMessage(NetworkListener.MESSAGE, messageTypePress,x,y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	class Cursor implements Serializable {
		private static final long serialVersionUID = 1651417152103363037L;

		public static final int TIMEOUT_MAX = 25;
		
		public int x, y;
		public String host;
		public Color color;
		public int timeOut;
		
		public void makeTrailCopy() {
			Cursor c = new Cursor();
			c.x = x;
			c.y = y;
			c.host = host;
			c.color = color;
			c.timeOut = TIMEOUT_MAX;
			synchronized(trails) {
				trails.add(c);
			}
		}
		
		
	}



}

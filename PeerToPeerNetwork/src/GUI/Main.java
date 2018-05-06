package GUI;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	  public static void main(String[] args)
	  {
			NetworkManagementPanel nmp = new NetworkManagementPanel();
			
			Object[] options = { "Chat", "Draw" };
			int demo = JOptionPane.showOptionDialog(null, "Which demo?", "Networking Demo", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			
			if (demo == 0) {
				ChatPanel panel = new ChatPanel(nmp.getMessageServer());
			} else if (demo == 1) {
				DrawingPanel panel = new DrawingPanel(nmp.getMessageServer());
			}
	  }
}

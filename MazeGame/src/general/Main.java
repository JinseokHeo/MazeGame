

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	  public static void main(String[] args)
	  {
			
			Object[] options = { "Monster", "Player" };
			int demo = JOptionPane.showOptionDialog(null, "Who are you?", "MAZE GAME", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			
			if (demo == 0) {
				new Monster();
			} else if (demo == 1) {
				new Player();
			}
	  }
}

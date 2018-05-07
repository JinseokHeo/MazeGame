import java.awt.Image;

import javax.swing.ImageIcon;

public class MonsterTrap extends Trap{

	private static Image trapImage = new ImageIcon("trap.png").getImage();;
	
	public MonsterTrap() {
		super(-1, -1, trapImage);
	}
	public MonsterTrap(int x, int y) {
		super(x, y, trapImage);
	}
}

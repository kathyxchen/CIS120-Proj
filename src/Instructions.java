import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
/* the instructions page! */
public class Instructions extends GenericState {
		
	public static final String imgFile = "paris.jpg";
	public static final String pointerFile = "arrow.png";
	
	private Font font;
	private Font startFont;
	
	private BufferedImage pointer;
	private BufferedImage bg;
		
	public Instructions(StateController sc) {
		super(sc);
		try {
			this.bg = ImageIO.read(new File(imgFile));
			this.pointer = ImageIO.read(new File(pointerFile));
			this.font = new Font("Copperplate Gothic Bold", Font.PLAIN, 35);
			this.startFont = new Font("Copperplate Gothic Bold", Font.PLAIN, 15);
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}
		
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, null);		
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Back", 105, 430);
		
		g.setFont(startFont);
		g.setColor(Color.WHITE);
		g.drawString("(or press SPACE to start)", 145, 450);
		
		g.drawImage(pointer, 73, 404, null);
	}
	
	public void handle() {
		if (Listeners.getPressed() != null && Listeners.accessed()) {
			if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_ENTER) {
				sc.change(States.MENU);
			}
			if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_SPACE) {
				sc.change(States.GAME);
			}
			Listeners.setAccessed();
		}
	}
}


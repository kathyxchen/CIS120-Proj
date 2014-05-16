import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class StartMenu extends GenericState {
	
	private Font title;
	private Font font;
	
	// just for fun--three different possible backgrounds!
	// because they all just looked cool
	private int bgNum;
	private BufferedImage all;
	private BufferedImage[] each;
	public static final String imgFile = "bgtile.png";
	
	private BufferedImage pointer;
	public static final String pterFile = "arrow.png";
	
	private int choose = 0;
	private States[] select = {States.GAME, States.INSTRUCT, States.END};
	
	public StartMenu(StateController sc) {
		super(sc);
		try {
			this.pointer = ImageIO.read(new File(pterFile));
			this.all = ImageIO.read(new File(imgFile));
			this.title = new Font("Century Gothic", Font.PLAIN, 35);
			this.font = new Font("Copperplate Gothic Bold", Font.PLAIN, 25);
			this.bgNum = (int) (Math.random() * 3);
			this.each = new BufferedImage[3];
			loadBackgrounds();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadBackgrounds() {
		for (int i = 0; i < each.length; i++) {
			each[i] = all.getSubimage(i*640, 0, 640, 480);
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(each[bgNum], 0, 0, null);
		g.setFont(title);
		g.setColor(Color.WHITE);
		g.drawString("Walk the Line", 200, 220);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Start", 200, 420);
		g.drawString("Instructions", 230, 440);
		g.drawString("Quit", 280, 460);
		
		g.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		g.setColor(Color.WHITE);
		g.drawString("(Press Enter to Select)", 290, 475);
		// draw pointer at the correct spot
		if (choose == 0) {
			g.drawImage(pointer, 170, 395, null);
		}
		else if (choose == 1) {
			g.drawImage(pointer, 200, 415, null);
		}
		else if (choose == 2) { 
			g.drawImage(pointer, 250, 435, null);
		}
	}

	private void select(States c) {
		if (c == States.END) {
			System.exit(0);
		}
		else {
			sc.change(c);
		}
	}
	
	public void handle() {
		// move the pointer between the options
		if (Listeners.getPressed() != null && Listeners.accessed()) {
			if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_ENTER) {
				select(select[choose]);
			}
			if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_UP) {
				if (choose > 0) {
					choose--;
				}
			}
			if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_DOWN) {
				if (choose < 2) {
					choose++;
				}
			}
			Listeners.setAccessed();
		}
	}
}

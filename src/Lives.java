import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Lives {
	public static final String healthImg = "health.png";
	public static final String livesImg = "lives.png";
	
	private int numHealth;
	private int numLives;
	private int ammoCount;
	private int score;
	
	private Font font;
	private Player p;
	
	public Lives(Player p) {
		this.font = new Font("Copperplate Gothic Bold", Font.PLAIN, 14);
		this.p = p;
	}
	
	public void update() {
		numHealth = p.getHealth();
		numLives = p.getLives();
		ammoCount = p.getAmmo();
		score = p.getScore();
	}
	
	public void draw(Graphics2D g) {
		g.setFont(font);
		g.setColor(Color.GRAY);
		g.drawString("LIVES: " + numLives, 490, 20);
		
		g.setFont(font);
		g.setColor(Color.GRAY);
		g.drawString("HEALTH: " + numHealth, 490, 40);
		
		g.setFont(font);
		g.setColor(Color.GRAY);
		if (p.isFatigued()) {
			g.drawString("FATIGUED", 490, 60);
		}
		else {
			g.drawString("ATTACKS LEFT: " + ammoCount, 490, 60);
		}
		
		g.setFont(font);
		g.setColor(Color.GRAY);
		g.drawString("SCORE: " + score, 490, 80);
	}

}

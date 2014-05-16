
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spider extends GameObj {
	private Player p;
	
	private static final String imgFile = "SPIDER.png";
	
	private int hits;
	private int maxHits;
	
	// all present but not moving unless visible on screen
	private boolean onScreen; 
	private boolean dead;
	
	// after a certain amount of time, if you still are not finished with the game, 
	// the spiders get harder to kill 
	// (because i'm a nice person)
	private double faster;
	private boolean stronger;
	
	public Spider(TileSet ts, Player p) {
		super(ts);
		try {
			this.objImg = ImageIO.read(new File(imgFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.p = p;
		this.dead = false;
		this.maxHits = 3;
		this.hits = 0;
		this.faster = 1.5;
		this.stronger = false;
		this.width = 30;
		this.height = 30;
		this.bdWidth = 35;
		this.bdHeight = 35;
		this.objVel = 2;
		this.onScreen = false;
		this.right = false;
		this.left = true;
		this.onScreen = false;
	}
	
	public int numHits() {
		return hits;
	}
	
	public boolean isStronger() {
		return stronger;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public boolean onScreen() {
		return onScreen;
	}
	
	public void setStronger() {
		this.stronger = true;
		this.objVel = 3.2;
		this.maxHits = 5;
	}
	
	public void hit() {
		// if the spider is hit, determine where the player is
		// and start moving towards the player instead
		if (p.getX() < this.getX()) {
			left = true;
			right = false;
		}
		else {
			right = true;
			left = false;
		}
		hits++;
		// start moving faster when nearing end of life
		if (hits == maxHits - 1) {
			dx = faster * dx;
		}
		if (hits == maxHits) {
			dead = true;
			onScreen = false;
		}
	}
	
	public void move() {
		if (right) {
			dx = objVel;
		}
		else if (left) {
			dx = -objVel;
		}
		else {
			dx = 0;
		}
	}
	
	public void update() {
		if (p.getTime() > 5000) {
			setStronger();
		}
		if (!onScreen) {
			if ((Math.abs(xPos - p.getX()) < GameFrame.WIDTH)
				&& (Math.abs(yPos - p.getY()) < GameFrame.HEIGHT)) {
				onScreen = true;
			}
			return;
		}
		if (onScreen) {
			move(); // spider should move on its own
			canMove(); // but make sure it can move
			
			// if there's no tile underneath it, change direction!
			anticipate(xPos + dx, yPos + 1); 
			if (bl) {
				left = false;
				right = true;
			}
			if (br) {
				left = true;
				right = false;
			}
			
			setPos(toX, toY);
			
			if (dx == 0) {
				left = !left;
				right = !right;
			}
		}
	}
		
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}

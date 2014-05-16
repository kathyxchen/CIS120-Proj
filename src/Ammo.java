import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Ammo extends GameObj {
	
	private String imgFile = "ink.png";

	double ammoVel;
	double ammoMaxVel;
	double ammoSlowVel;
	
	boolean remove;
	
	boolean isRight;
	
	public Ammo(TileSet ts, boolean isRight, double xPos, double yPos) {
		super(ts);
		try {
			this.objImg = ImageIO.read(new File(imgFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.ammoVel = 2;
		this.ammoMaxVel = 4; 
		this.ammoSlowVel = 1;
		this.isRight = isRight;
		this.remove = false;
		this.width = 30;
		this.height = 30;
		this.bdWidth = 20;
		this.bdHeight = 20;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public void update() {
		if (isRight) {
			dx += ammoVel;
			if (dx > ammoMaxVel) {
				dx += ammoSlowVel;
			}
		}
		else {
			dx -= ammoVel;
			if (dx < -ammoMaxVel) {
				dx -= ammoSlowVel;
			}
		}
		canMove();
		setPos(toX, toY);
	} 
	
	public void remove() {
		remove = true;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
}

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class GameObj {
	
	public BufferedImage objImg;
	
	// track movements
	public boolean left;
	public boolean right;
	public boolean down;
	public boolean fall;
	
	// track speeds
	public double objVel;
	public double objMaxVel;
	
	// track positions & changes in position
	public double xPos;
	public double yPos;
	public double dx;
	public double dy;
	public double toX;
	public double toY;
	
	// width and height of the object
	public int width;
	public int height;
	
	// the bounding box width and height
	// in case you wanted them to be different
	public int bdWidth;
	public int bdHeight;
	
	// for the game layout!
	public TileSet tileSet;
	public int tileSize;
	public double xMapFoc;
	public double yMapFoc;
	
	// determine which row and col the obj is on
	public int onRow;
	public int onCol;
	
	// check edges 
	public boolean tl;
	public boolean tr;
	public boolean bl;
	public boolean br;
	
	public GameObj(TileSet ts) { 
		this.tileSet = ts;
		this.tileSize = ts.getTileSize();
	}
	
	/* getters and setters */
	public int getX() { 
		return (int) xPos; 
	}
	
	public int getY() { 
		return (int) yPos; 
	}
	
	public int getWidth() { 
		return width; 
	}
	
	public int getHeight() { 
		return height; 
	}
	
	public int getBdWidth() { 
		return bdWidth;
	}
	
	public int getBdHeight() { 
		return bdHeight; 
	}
	
	public void setPos(double x, double y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public void setMapFocus() {
		this.xMapFoc = tileSet.getX();
		this.yMapFoc = tileSet.getY();
	}
	
	public void isLeft(boolean dir) { 
		this.left = dir; 
	}

	public void isRight(boolean dir) { 
		this.right = dir; 
	}
	
	public void isDown(boolean dir) {
		this.down = dir;
	}
	
	public boolean getFall() {
		return fall;
	}
	
	public double getVelY() {
		return dy;
	}
	
	public Rectangle collisionBds() {
		return new Rectangle((int) xPos - (bdWidth / 2), (int) yPos - (bdHeight / 2), bdWidth, bdHeight);
	}
	
	public boolean collisionOccurred(GameObj o) {
		Rectangle objRect = o.collisionBds();
		return this.collisionBds().intersects(objRect);
	}
	
	// this is a function that essentially checks the position you want to move to 
	// and determines whether there is a tile in the way or not.
	public void anticipate(double x, double y) {	
		if (x <= 0 || y <= 0 || 
			x >= tileSet.getMapWidth() || y >= tileSet.getMapHeight()) {
			this.tl = this.tr = this.bl = this.br = false;
			return;
		}
		int leftmost = (int) (x - (bdWidth / 2)) / tileSize;
		int rightmost = (int) (x + (bdWidth / 2) - 1) / tileSize;
		int topmost = (int) (y - (bdHeight / 2)) / tileSize;
		int bottommost = (int) (y + (bdHeight / 2) - 1) / tileSize;
		if (topmost < 0 || leftmost < 0
			|| rightmost >= tileSet.getTotCols()) {
			this.tl = this.tr = this.bl = this.br = false;
			return;
		}
		else if (bottommost >= tileSet.getTotRows()) {
			this.tl = this.tr = this.bl = this.br = true;
		}
		else {
			this.tl = tileSet.isNotTile(topmost, leftmost);
			this.tr = tileSet.isNotTile(topmost, rightmost);
			this.bl = tileSet.isNotTile(bottommost, leftmost);
			this.br = tileSet.isNotTile(bottommost, rightmost);
		}
	}
	
	
	// so this actually sets up the movement
	public void canMove() { 
		// store current positions
		toX = xPos; 
		toY = yPos;
		
		// check where you're about to go
		double goingX = xPos + dx;
		double goingY = yPos + dy;
		
		anticipate(goingX, yPos);
		moveX();
		anticipate(xPos, goingY);
		moveY();
	}
	
	// sets up where your x is moving to
	private void moveX() {
		// if left and there are left tiles present
		// or right and right tiles present
		// don't move anymore
		if ((dx < 0 && (!tl || !bl)) || (dx > 0 && (!tr || !br))) {
			dx = 0;
		}
		else { 
			toX += dx;
		}
	}
	
	// sets up where your y is moving to 
	private void moveY() {
		// if up and there are tiles above 
		// or down and tiles below 
		// don't move anymore
		if ((dy < 0 && (!tl || !tr)) || (dy > 0 && (!bl || !br))) { 
			dy = 0;
		}
		else {		
			toY += dy;
		}
	}
	
	// draw accordingly
	public void draw(Graphics2D g) {
		setMapFocus();
		g.drawImage(objImg, (int) (xPos + xMapFoc - width / 2), (int) (yPos + yMapFoc - height / 2), null);
	}
	
}


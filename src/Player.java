import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player extends GameObj {
	private static final String imgFile = "owl.png";
	
	// jump movement
	protected boolean jump;
	protected boolean jumpTwice;
	protected double jumpInitVel;
	protected double jumpMaxVel;
	private int jumpCount; // how many times you've jumped
	
	// fall movement
	protected double fallVel;
	protected double fallMaxVel;
	
	private long time; // how long you've been playing
	
	private boolean attacking; // whether attacking or not
	
	private boolean faceRight;
	
	// if attacked too many times, player is "fatigued" for a period of time
	// (cannot attack, moves slower)
	private boolean inFatigue; 
	private int fatigueCount;
	
	private int health;
	private int maxHealth;
	private int lives;
	private boolean dead;
	private double yPosPrev; // stores position before you lose a life
	
	private double slowDown;
	
	private int ammoCount;
	private ArrayList<Ammo> ammo;
	private ArrayList<Spider> spiders;
	private ArrayList<Coin> coins;
	private int score;
	
	public Player(TileSet ts) {
		super(ts);
		try {
			this.objImg = ImageIO.read(new File(imgFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.jumpCount = 0;
		this.jumpTwice = false;
		this.jumpInitVel = 0.50;
		this.jumpMaxVel = 5;
		
		this.fallVel = 0.50;
		this.fallMaxVel = 4;
		
		this.fatigueCount = 0;
		this.inFatigue = false;
		this.health = 10;
		this.maxHealth= 10;
		this.lives = 3;
		this.dead = false;
		
		this.ammoCount = 8;
		this.ammo = new ArrayList<Ammo>();
		
		this.faceRight = true;
		this.left = false;
		this.right = false;
		this.down = false;
		this.fall = false;
		this.jump = false;
		
		this.objVel = 2;
		this.objMaxVel = 10;
		this.slowDown = 0.80;
		
		this.width = 30;
		this.height = 30;
		this.bdWidth = 30;
		this.bdHeight = 30;
		
		this.score = 0;
	}
	
	// allows interactivity with other objects
	public void initState(ArrayList<Spider> s, ArrayList<Ammo> a) { 
		this.spiders = s;
		this.ammo = a;
		this.coins = tileSet.getCoins();
	}
	
	/* lots of getters and setters: */
	public int getScore() {
		return score;
	}
	
	public void setScore() {
		score++;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public boolean getJumping() {
		return jump;
	}
	
	public int ammoLeft() {
		return ammo.size();
	}
	
	public int getAmmo() {
		return ammoCount;
	}
	
	public void resetAmmoCount() {
		ammoCount = 8;
	}
	
	public boolean isFatigued() {
		return inFatigue;
	}
	
	public void incFatigueCount() {
		fatigueCount++;
	}
	
	public boolean jumpTwice() {
		return jumpTwice;
	}
	
	public void resetJumpCount() {
		jumpCount = 0;
	}
	
	public void incJumpCount() {
		jumpCount++;
	}
	
	public void isJumping(boolean dir) {
		this.jump = dir;
	}
	
	public int getJumps() {
		return jumpCount;
	}
	
	public void setJumpTwice(boolean twice) {
		this.jumpTwice = twice;
	}
	
	public void incNumLives() {
		lives++;
	}
	
	public void decNumLives() {
		lives--;
		this.yPosPrev = yPos;
		reset();
		if (lives == 0) {
			this.health = 0;
			this.dead = true;
		}
	}
	
	public void decHealth() {
		health--;
		if (health == 0) {
			decNumLives();
			reset();
		}
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setAttack() {
		this.attacking = true;
	}
	
	public void setFacing(boolean f) {
		this.faceRight = f;
	}
	
	public boolean facingRight() {
		return faceRight;
	}
	
	public long getTime() { 
		return time; 
	}
	
	public void setTime(long t) { 
		this.time = t; 
	}
	
	public void hit(int n) {
		health -= n;
		if (health < 0) {
			health = 0;
		}
		if (faceRight) {
			dx = -1;
		}
		else {
			dx = 1;
		}
		fall = true;
		jump = false;
	}
	
	public void reset() {
		resetAmmoCount();
		health = maxHealth;
		faceRight = true;
		left = right = down = jump = fall = attacking = jumpTwice = false; 
		// check at what place you died, and reset to a position based on that
		if (yPosPrev < 1000) {
			setPos(30, 30);
		}
		else if (yPosPrev > 1000 && yPosPrev < 2000) {
			setPos(200, 920);
		}
		else {
			setPos(250, 1430);
		}
	}

	public void move() {
		if (attacking && (!jump || !fall)) {
			dx = 0; 
		}

		if (left) {
			faceRight = false;
			dx -= objVel;
			if (dx < -objMaxVel) {
				dx = -objMaxVel;
			}
		}
		
		else if (right) {
			faceRight = true;
			dx += objVel;
			if (dx > objMaxVel) {
				dx = objMaxVel;
			}
		}
		
		else if (down) {
			dx = 0;
			jump = false;
			jumpTwice = false;
			fall = true;
			isDown(false);
		}
		
		// you can only jump twice in a row
		if (jump && !fall) {
			if (jumpTwice) {
				dy -= 1.2*jumpInitVel;
				if (dy < -1.5*jumpMaxVel) {
					fall = true;
					jump = false;
					jumpTwice = false;
				}
			}
			else {
				dy -= jumpInitVel;
				if (dy < -jumpMaxVel) {
					fall = true;
					jump = false;
				}
			}
		}
		
		if (jump && fall) {
			if (jumpTwice) {
				fall = false;
				dy -= jumpInitVel;
				if (dy < -jumpMaxVel) {
					fall = true;
					jumpTwice = false;
				}
			}
		}
		
		if (fall) {
			dy += fallVel;
			if (dy > fallMaxVel) { 
				dy = fallMaxVel; 
			}
		}
		
		// fatigue slows you down for a time
		if (inFatigue) {
			double dxBefore = dx;
			double dyBefore = dy;
			if (fatigueCount < 100) {
				dx = slowDown * dx;
				dy = slowDown * dy;
			}
			else {
				inFatigue = false;
				fatigueCount = 0;
				dx = dxBefore;
				dy = dyBefore;
			}
		}
		
	}
	
	public void update() {
		time++;
		if (isFatigued()) {
			incFatigueCount();
		}
		move(); 
		canMove(); // checks if tiles/walls encountered after move()
		setPos(toX, toY); // actually moves if possible
		// these ensure that you don't get stuck if you
		// try to go past the window edges
		if (xPos < (bdWidth/4)) {
			xPos = bdWidth/2;
		}
		else if (xPos > (640 - bdWidth/4)) {
			xPos = 640 - bdWidth/2;
		}
		if (yPos < (bdHeight/4)) {
			yPos = bdHeight/2;
		}
		// RIGHT before the player goes beyond the last tile
		else if (yPos > (2878)) {  
			dead = true;
		}
		if (attacking) {
			if (faceRight && !inFatigue) {
				Ammo a = new Ammo(tileSet, true, xPos + 20, yPos); 
				ammo.add(a);
				ammoCount--;
			}
			else if (!faceRight && !inFatigue) {
				Ammo a = new Ammo(tileSet, false, xPos - 40, yPos); 
				ammo.add(a);
				ammoCount--;
			}
			if (ammoCount == 0) {
				attacking = false;
				inFatigue = true;
				resetAmmoCount();
			}
			attacking = false;
		}
		
		/* collision detection between objects: */
		for (int i = 0; i < ammo.size(); i++) {
			Ammo blot = ammo.get(i);
			blot.update();
			for (int j = 0; j < spiders.size(); j++) {
				Spider spid = spiders.get(j);
				if (blot.collisionOccurred(spid)) {
					blot.remove();
					spid.hit();
					if (spid.isDead()) {
						spiders.remove(j);
					}
				}
			}
			if (blot.shouldRemove()) {
				ammo.remove(i);
			}
			
		}
		
		for (int j = 0; j < spiders.size(); j++) {
			if (this.collisionOccurred(spiders.get(j))) {
				decHealth();
				// if to the left of the spider, colliding with it means 
				// you bounce back left slightly
				if (xPos < spiders.get(j).getX()) { 
					setPos(xPos - (bdWidth/3), yPos);
				}
				// and vice versa for right
				else {
					setPos(xPos + (bdWidth/3), yPos);
				}
			}
		}
		
		for (int j = 0; j < coins.size(); j++) {
			if (this.collisionOccurred(coins.get(j))) {
				coins.remove(j);
				setScore();
			}
		}
	}
	
	public void draw(Graphics2D g) {
		for(int i = 0; i < ammo.size(); i++) {
			ammo.get(i).draw(g);
		}	
		super.draw(g);
	}
	
	public void canMove() { 
		// store current positions
		toX = xPos; 
		toY = yPos;
		
		// check where you're about to go
		double goingX = xPos + dx;
		double goingY = yPos + dy;
		
		// check when you aren't moving at all
		anticipate(xPos, yPos);
		checkStill(); // used to detect if should be falling through space
		anticipate(xPos, goingY);
		// if there are bottom tiles, you aren't falling anymore
		if ((!bl || !br) && dy > 0) { 
			dy = 0;
			fall = false; 
		}
		// jumping
		if (dy <= 0 && jump) { 
			toY += dy;
		}

		// check moving left or right
		anticipate(goingX, yPos);
		moveX();
		// move and then check if you might fall (moved to area with no tiles)
		anticipate(xPos, goingY);
		// if there's tiles below you, 
		// again, you aren't falling as you are moving
		if (!bl || !br && dy > 0) { 
			dy = 0;
			fall = false;
		}
		if (dy <= 0 && jump) {
			if (!tl && !tr) {
				toY += dy;
			}
		}
		
		// check moving up or down
		anticipate(xPos, goingY);
		if (jump && !fall) { 
			// if you are jumping, you want to anticipate tiles above you too
			// and start falling before you hit them
			anticipate(xPos, goingY - 2); 
			if (!tl || !tr) {
				fall = true;
			}
		}
		moveY();
	}
	
	/* these are overrided functions because player movement 
	 * is very different from the other objects */
	private void moveX() {
		// if moving and there are tiles on the respective side you're moving towards
		if ((dx < 0 && (!tl)) || (dx > 0 && (!tr))) {
			if (dx < 0) {
				// slight rebound if you hit walls/objects
				toX += bdWidth/6;
			}
			else {
				// again, rebound
				toX -= bdWidth/6;
			}
			dx = 0;
		}
		// otherwise just move
		// using "while" ensures that it only moves once for every time I press a key
		else { 
			if (dx < 0) {
				while (left) { 
					toX += dx;
					isLeft(false);
				}
			}
			if (dx > 0) {
				while (right) {
					toX += dx;
					isRight(false);
				}
			}	
			// now check if you are going to fall after you've moved
			checkStill();
		}
	}
		
	private void moveY() {
		// if jumping but there are tiles above you
		if (dy < 0 && (!tl || !tr)) {
			dy = 0;
			fall = true;
			jump = false;
			jumpTwice = false;
		}
		// if falling and there are tiles below you
		else if (dy > 0 && (!bl || !br)) { 
			dy = 0;
			fall = false;
			jump = false;
			jumpTwice = false;
		}
		// otherwise, move as normally
		else {
			toY += dy;
		}
	}
	
	// checks if there are tiles underneath you
	// when you aren't moving
	private void checkStill() {
		if ((br && bl) && dy >= 0) {
			fall = true;
		}
	}
	
}

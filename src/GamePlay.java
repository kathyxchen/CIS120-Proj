import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GamePlay extends GenericState {
	// this is the actual game
	
	private BufferedImage bg;
	
	private TileSet tileSet;
	private int[][] tileMap;
	
	private ArrayList<Spider> spiders;
	private ArrayList<Ammo> ammo;
	
	private Player p;
	private Lives l;
	
	private Rectangle endDetection; // used to determine if you've reached the end

	public GamePlay (StateController sc) {
		super(sc);
		
		// load background 
		try {
			this.bg = ImageIO.read(new File("background.png"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// load tile set 
		this.tileSet = new TileSet(20);
		tileSet.getTiles("trial.png");
		tileSet.getEachTile();
		tileSet.getMap("trial.txt");
		tileSet.setPos(0, 480);
		tileSet.setBds(0, tileSet.getMapWidth(), 0, tileSet.getMapHeight());
	
		// get the tile map
		this.tileMap = tileSet.getTileMap();
		
		/* set up all the game objects: */
		this.p = new Player(tileSet);
		p.setPos(30, 30);
		p.setFacing(true);
		
		// populate the game with spiders
		this.spiders = new ArrayList<Spider>();
		addSpiders();
		
		this.ammo = new ArrayList<Ammo>();
		
		this.l = new Lives(p);
		p.initState(spiders, ammo);
		
		// set end game 
		this.endDetection = new Rectangle(300, 2820, 20, 40);
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, null);
		
		tileSet.draw(g);
		if (!p.isDead()) {
			p.draw(g);
		}
		
		for (Spider i : spiders) {
			i.draw(g);
		}
		
		l.draw(g);
		
		// if you die
		if (p.isDead()) {
			g.setFont(new Font("Arial", Font.PLAIN, 50));
			g.setColor(Color.GRAY);
			g.drawString("GAME OVER", 170, GameFrame.HEIGHT / 2 - 30);
			
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.setColor(Color.GRAY);
			g.drawString("(press space to return to main menu)", 158, GameFrame.HEIGHT / 2);
		}
		
		// if you are in end game zone
		if (p.getY() > 2820) {	
			if (p.collisionBds().intersects(endDetection)) {
				g.setFont(new Font("Arial", Font.PLAIN, 20));
				g.setColor(Color.WHITE);
				g.drawString("THE END.", 158, GameFrame.HEIGHT / 2 - 50);
			
				g.setFont(new Font("Arial", Font.PLAIN, 20));
				g.setColor(Color.WHITE);
				g.drawString("FINAL SCORE: " + p.getScore(), 170, GameFrame.HEIGHT / 2 - 30);
				
				g.setFont(new Font("Arial", Font.PLAIN, 20));
				g.setColor(Color.WHITE);
				g.drawString("(press space to return to main menu)", 158, GameFrame.HEIGHT / 2 + 20);
			}
		}
	}
	
	public void handle() {
		if (Listeners.getPressed() != null && Listeners.accessed()) {
			// if still in play, detect key presses
			if (!p.isDead() && !p.collisionBds().intersects(endDetection)) {
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_UP) {
					if (!p.isFatigued()) {
						if (p.getJumps() == 1) { 
							p.incJumpCount();
							p.isJumping(true);
							p.setJumpTwice(true);
						}
						else if (p.getJumps() > 1) {
							if (p.getVelY() == 0) {
								p.resetJumpCount();
							}	
						}
						else if (p.getJumps() == 0) {
							p.incJumpCount();
							p.isJumping(true);
						}
					} 
				}
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_DOWN) {
					p.isDown(true);
				}
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_LEFT) {
					p.isLeft(true);
				}
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_RIGHT) {
					p.isRight(true);
				}
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_SPACE) {
					if (!p.isFatigued()) {
						p.setAttack();
					}
				}
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_ESCAPE) {
					sc.change(States.MENU);
				}
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_R) {
					sc.change(States.GAME);
				}
				Listeners.setAccessed();
			}
			// if end game, only detect space to go back to menu
			else if (p.isDead() || p.collisionBds().intersects(endDetection)) {
				if (Listeners.getPressed().getKeyCode() == KeyEvent.VK_SPACE) {
					sc.change(States.MENU);
				}
			}
		}
		p.update();
		l.update();
		tileSet.setPos(GameFrame.WIDTH / 2 - p.getX(), GameFrame.HEIGHT / 2 - p.getY());
		tileSet.fix();
		if (!p.collisionBds().intersects(endDetection)) {
			for (Spider i : spiders) {
				i.update();
			}
		}
		
	}
	
	// private function to populate game with spiders
	private void addSpiders() {
		
		// added based on modulus:
		for (int i = 0; i < tileMap.length; i++) {
			for (int j = 0; j < tileMap[0].length; j++) {
				int num = tileMap[i][j];
				int xPos = 0;
				int yPos = 0;
				Spider s = new Spider(tileSet, p);
				if ((num != 0) && (num % 25 == 0) && (num > 20) && num < 1000) {
					xPos = (int) tileSet.getX() + (j * tileSet.getTileSize());
					yPos = (int) tileSet.getY() + (i * tileSet.getTileSize());
					s.setPos(xPos, yPos-17);
					spiders.add(s);
				}
				else if ((num != 0) && (num % 85 == 0) && (num > 1000)) {
					xPos = (int) tileSet.getX() + (j * tileSet.getTileSize());
					yPos = (int) tileSet.getY() + (i * tileSet.getTileSize());
					s.setPos(xPos, yPos-17);
					spiders.add(s);
				}
			}
		}
		
		// a few added manually:
		Spider s;
		
		s = new Spider(tileSet, p);
		s.setPos(370,  743);
		spiders.add(s);
		
		s = new Spider(tileSet, p);
		s.setPos(150,  2503);
		spiders.add(s);
		
		s = new Spider (tileSet, p);
		s.setPos(310, 1723);
		
		s = new Spider(tileSet, p);
		s.setPos(200,  2503);
		spiders.add(s);
		
		s = new Spider(tileSet, p);
		s.setPos(400,  2183);
		spiders.add(s);
		
		s = new Spider(tileSet, p);
		s.setPos(200,  1223);
		spiders.add(s);
		
		s = new Spider(tileSet, p);
		s.setPos(300,  1723);
		spiders.add(s);
		
		s = new Spider(tileSet, p);
		s.setPos(100,  1383);
		spiders.add(s);
		
	}
	

}

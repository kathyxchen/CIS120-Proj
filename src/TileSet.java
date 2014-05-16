import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TileSet {
	// get the image being used for tiles
	private BufferedImage tilesImg;
	private BufferedImage[][] tilesArr;
	
	// height and width of the tilesArr
	private int w;
	private int h;

	// this is essentially the game layout
	private int[][] tileMap;
	private int tileSize;
	private int mapW;
	private int mapH;
	
	// position of the top corner of the tile map
	private double x;
	private double y;

	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	// only draw what is in the bounds of our GameFrame
	private int rowCorrection;
	private int colCorrection;
	private int rowsInMap;
	private int colsInMap;
	private int rowsInView;
	private int colsInView;
	
	// draw all the coins
	private ArrayList<Coin> coins;
	
	public TileSet(int size) {
		this.tileSize = size;
		this.rowsInView = (GameFrame.HEIGHT / tileSize) + 2; // + 2 just for padding
		this.colsInView = (GameFrame.WIDTH / tileSize);  
		this.coins = new ArrayList<Coin>();
	}
	
	// load the tiles image
	public void getTiles(String s) {
		try {
			this.tilesImg = ImageIO.read(new File(s)); 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// get the sub-images desired
	public void getEachTile() {
		this.h = tilesImg.getHeight() / tileSize;
		this.w = tilesImg.getWidth() / tileSize;
		this.tilesArr = new BufferedImage[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				tilesArr[i][j] = tilesImg.getSubimage(tileSize * j, tileSize * i, tileSize, tileSize);
			}
		}
	}
	
	// load your map from the input file
	// this determines which individual tile goes where
	public void getMap(String s) {
		try {
			FileInputStream input = new FileInputStream(s); 
			BufferedReader b = new BufferedReader(new InputStreamReader(input));
			// in my input file, I specify cols in fst line and rows in snd line
			this.colsInMap = Integer.parseInt(b.readLine());
			this.rowsInMap = Integer.parseInt(b.readLine()); 
			// read in the game layout!
			this.tileMap = new int[rowsInMap][colsInMap];
			for (int i = 0; i < rowsInMap; i++) {
				String mapLine = b.readLine();
				String[] inMapLine = mapLine.split("\\s+");
				for (int j = 0; j < colsInMap; j++) {
					tileMap[i][j] = Integer.parseInt(inMapLine[j]);
				}
			}
			b.close();
			
			this.mapW = colsInMap * tileSize;
			this.mapH = rowsInMap * tileSize;
			
			// bounds--because scrolling upward, anticipate they will be negative
			this.xmin = GameFrame.WIDTH - mapW;
			this.xmax = 0;
			this.ymin = GameFrame.HEIGHT - mapH;
			this.ymax = 0; 
			
			// private function to add coins to the layout
			addCoins();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* getters and setters: */
	public ArrayList<Coin> getCoins() {
		return coins;
	}
	
	public int getTileSize() { 
		return tileSize; 
	}
	
	public double getX() { 
		return x; 
	}
	
	public double getY() { 
		return y; 
	}
	
	public int getMapWidth() { 
		return mapW; 
	}
	
	public int getMapHeight() { 
		return mapH; 
	}
	
	public int getTotRows() { 
		return rowsInMap; 
	}
	
	public int getTotCols() { 
		return colsInMap; 
	}
	
	public boolean isNotTile(int r, int c) {
		return (tileMap[r][c] == 0); 
	}
	
	public void setBds(int xO, int xF, int yO, int yF) {
		xmax = xO;
		xmin = GameFrame.WIDTH - xF;
		ymax = yO;
		ymin = GameFrame.HEIGHT - yF;
	}
	
	public int[][] getTileMap() {
		return tileMap;
	}
	
	// Note x and y are the top left corner of the game layout
	public void setPos(double xNew, double yNew) { 
		// first, update x & y, then check if within bounds
		this.x += (xNew - this.x); 
		this.y += (yNew - this.y); 
		fix(); // ensure they are within bounds
		this.colCorrection = (int) -(this.x / tileSize);
		this.rowCorrection = (int) -(this.y / tileSize);
	}
	
	public void fix() {
		if (x < xmin) {
			x = xmin;
		}
		else if (x > xmax) {
			x = xmax;
		}
		
		if (y < ymin) {
			y = ymin;
		}
		else if (y > ymax) {
			y = ymax;
		}
	}
	
	public void draw(Graphics2D g) {
		for (int i = rowCorrection; i < rowCorrection + rowsInView; i++) {
			if (i >= rowsInMap) break; 
			else {
				for (int j = colCorrection; j < colCorrection + colsInView; j++) {
					if (j >= colsInMap) break;
					else {
						if (tileMap[i][j] != 0) {
							// this is all based on the way I made my map in the input file
							String tmString = Integer.toString(tileMap[i][j]);
							String[] stringArr = new String[2];
							stringArr[0] = Character.toString(tmString.charAt(0));
							stringArr[1] = tmString.substring(1);
							if (stringArr[1].length() > 2) {
								stringArr[0] = tmString.substring(0,2);
								stringArr[1] = tmString.substring(2,tmString.length()-1);
							}

							int row = Integer.parseInt(stringArr[0]);
							int col = Integer.parseInt(stringArr[1]);

							int xPos = (int) x + (j * tileSize);
							int yPos = (int) y + (i * tileSize);
							
							g.drawImage(tilesArr[row-1][col-1], xPos, yPos, null);  
							
							for (Coin c : coins) {
								c.draw(g);
							}
						}
					}
				}
			}
		}
	}

	
	private void addCoins() {
		// added based on modulus
		for (int i = 0; i < rowsInMap ; i++) {
			for (int j = 0; j < colsInMap; j++) {
				int num = tileMap[i][j];
				int xPos = 0;
				int yPos = 0;
				Coin c = new Coin(this);
				if ((num != 0) && ((num)%10 == 0)
						&& (num < 1000)) {
					xPos = (int) x + (j * tileSize);
					yPos = (int) y + (i * tileSize);
					c.setPos(xPos, yPos-10);
					coins.add(c);
				}
				else if ((num != 0) && ((num)%120 == 0)
						&& (num > 1000)) {
					xPos = (int) x + (j * tileSize);
					yPos = (int) y + (i * tileSize);
					c.setPos(xPos, yPos-10);
					coins.add(c);		
				}
			}
		}
		
		// some added manually:
		Coin c;
		c = new Coin(this);
		c.setPos(240, 1890);
		coins.add(c);

		c = new Coin(this);
		c.setPos(40, 1946);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(220, 2010);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(480, 2010);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(500, 2110);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(200, 2310);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(80, 2410);
		coins.add(c);

		c = new Coin(this);
		c.setPos(60, 1450);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(490, 648);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(420, 1128);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(120, 1388);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(340, 1728);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(410, 2188);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(50, 2668);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(520, 2788);
		coins.add(c);
		
		c = new Coin(this);
		c.setPos(550, 2788);
		coins.add(c);
		
	}

}

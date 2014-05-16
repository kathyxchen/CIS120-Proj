
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// coins! for scores but that's all
public class Coin extends GameObj {
	
	private static final String imgFile = "coin.png";
	
	public Coin(TileSet ts) {
		super(ts);
		try {
			objImg = ImageIO.read(new File(imgFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.width = 15;
		this.height = 15;
		this.bdWidth = 15;
		this.bdHeight = 15;
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}


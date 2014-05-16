import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameFrame extends JPanel implements Runnable {
	
	// this is just the "frame" where everything is going to take place
	private BufferedImage img; 
	private Graphics2D g;
	
	// dimensions of window
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	// thread continuously updates everything
	private Thread animator;
	private boolean playing;
	private long DELAY = 20;
	
	private StateController sc;
	
	public GameFrame() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if (animator == null) {
			this.animator = new Thread(this);
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					// store what was pressed into my Listeners class
					Listeners.store(e); 
				}
			});
			animator.start();
		}
	}
	
	public void run() {
		long timeInit, timePassed, timeDelay;
		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.g = (Graphics2D) img.getGraphics();
		this.playing = true;
		this.sc = new StateController();
		sc.init(); // sets initial, main menu state
		while (playing) {
			render();
			sc.update();
			sc.draw(g);
			timeInit = System.nanoTime();
			timePassed = System.nanoTime() - timeInit;
			timeDelay = DELAY - timePassed / 1000000; 
			try {
				Thread.sleep(timeDelay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void render() {
		Graphics g2 = getGraphics();
		g2.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		g2.dispose();
	}

}

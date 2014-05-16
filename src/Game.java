import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game implements Runnable {
	
	public void run() {
		JFrame frame = new JFrame("Walk the Line");
		frame.setLocation(300, 300);
		frame.add(new GameFrame());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
	
}

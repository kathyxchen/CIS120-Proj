import java.awt.Graphics2D;
/* this is the superclass that I use for all the game states/pages */

public class GenericState {
	protected StateController sc;
	
	public GenericState(StateController sc) {
		this.sc = sc;
	}
	public void draw(Graphics2D g) {	
	}
	public void handle() {
	}
	
}

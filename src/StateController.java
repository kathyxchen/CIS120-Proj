import java.awt.Graphics2D;
import java.util.Map;
import java.util.TreeMap;

// StateController switches between pages
public class StateController {
	private Map<States, GenericState> pages;
	private States inState;
	private States[] allStates = {States.MENU, States.GAME, States.INSTRUCT};
	
	public StateController() {
		this.pages = new TreeMap<States, GenericState>();
		for (States i : allStates) {
			pages.put(i, null);
		}
	}
	
	// always open with the start menu
	public void init() { 
		this.inState = States.MENU;
		load(inState);
	}

	// load new states! 
	private void load(States s) {
		if (s == States.MENU) {
			pages.put(s, new StartMenu(this));
		}
		else if (s == States.INSTRUCT) {
			pages.put(s, new Instructions(this));
		}
		else if (s == States.GAME) {
			pages.put(s, new GamePlay(this));
		}
	}
	
	// set the previous state to null and then
	// place the new state into the map
	public void change(States s) {
		pages.put(inState, null);
		this.inState = s;
		load(inState);
	}
	
	public void update() {
		if (pages.get(inState) != null) {
			pages.get(inState).handle();
		}
	}
	
	public void draw(Graphics2D g) {
		if (pages.get(inState) != null) {
			pages.get(inState).draw(g);
		}
		else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GameFrame.WIDTH, GameFrame.HEIGHT);
		}
	}
	
	public States getState() {
		return inState;
	}

}

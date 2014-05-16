import java.awt.event.KeyEvent;

/* Stores what was just pressed and what was previously pressed. 
 * Also determines if a key was accessed or not--this is useful for
 * ensuring that in our KeyListeners if-statement that it will not keep 
 * registering the key as being held down/true
 */

public class Listeners {
	public static KeyEvent pressed; 
	public static KeyEvent prev;
	public static boolean acc;
	
	public Listeners() {
		pressed = null;
		prev = null;
		acc = false;
	}
	
	public static void store(KeyEvent e) {
		setAccessed();
		if (pressed != null) {
			prev = pressed;
		}
		pressed = e;
	}
	
	public static void setAccessed() {
		acc = !acc;
	}
	
	public static boolean accessed() {
		return acc;
	}
	
	public static KeyEvent getPressed() {
		return pressed;
	}
	
	public static KeyEvent getPrev() {
		return prev;
	}
	
	public static void clearPressed() {
		 pressed = null;
	}
	
	public static void clearPrev() {
		prev = null;
	}

}

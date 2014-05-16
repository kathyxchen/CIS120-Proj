THE CLASSES:

GAME:
Holds our main function and essentially just sets up the JFrame as necessary and creates an instance of our GameFrame object. 

GAMEFRAME:
The place where we "play" our game: everything is displayed here. There is a thread that will continually check for new input/any changes to objects, and update the view accordingly.

TILESET:
1. Loads a tileset and creates our game layout. 
2. First, it gets the tileset and reads it in as a BufferedImage. Then, it will separate each tile based on the tile size (we are assuming each tile is the same height and width). 
3. The getMap method then takes an input file that is exactly what I would like my game to look like. This is a platform game, so what I mean by this is that I have assigned a number to all the lines that I want to lay out tiles on--my platforms. The map is larger than the window because I want it to be scrolling.
4. Now, in terms of drawing, I don't want to draw the entire map since the whole map isn't on the screen all at one time, so I track the location of where I am in this map layout and only draw the parts needed in the window. 
	This is done by setting the "LAST line" of the tileMap 2-D array that is ON the screen as (0,0) and the very bottom as (0,-2400). This is because I have 144 lines of 20x20px tiles and my window height is 480. 0 - [(144 * 20) - 480] = -2400. There is no need to correct for the columns/window width because I am not scrolling from side to side. 
(Note that instances of Coin objects are positioned in the TileSet class, simply because no motion or anything is involved--I treat it like part of the default game layout)

STATECONTROLLER: Switches between the menu, instructions, and game itself. (Also controls the exit "page"/button.) Uses a map data structure to associate the STATES (enum) with the actual GenericState object itself. Note that the load method is private while the change method, which unloads the current state and loads the new one, is the one available for client use. 

STATES: the enum I referred to above--contains MENU, INSTRUCT, GAME, and EXIT

GENERICSTATE: The superclass I use for all possible states.

STARTMENU: A standard main menu. For fun, I randomly switch between three different backgrounds (courtesy of tumblr). Has the options of playing the game, going to the instructions page, or exiting.

INSTRUCTIONS: The instructions page. Not much else to say. There's the option of playing or going back to main menu. 

GAMEPLAY: The actual game state! I load a lot of things, like the background, tile/game layout (& coins), player & ammo, spiders, etc. Conditions to check during the game are what keys are being pressed, all the objects that must be updated, player statistics, and whether the player is alive or dead or has won yet. 

GAMEOBJ: The superclass for all the objects interacting in the game. Accounts for collision detection by creating a rectangle bounding box, and checking if those rectangles ever intersect. There are methods to determine if tile interaction occurs at all four corners, along with ones that actually move the object in a standard way. 

PLAYER: 
1. Has more specific movements, such as jump and fall. Also comes with attack mode, fatigue, health, lives, and ammo.
2. When losing lives, store the position at which the player dies and then re-start the player at the closest "save" points, rather than just from the very beginning. 
3. Movement has to account for whether the player is in the air--if there are no tiles below, player should be falling. (This is the checkStill method.) Hence, overrided moveX(), moveY(), and canMove() methods. 
4. Can only jump twice in a row. 
5. Note that fatigue is a setting that occurs when you've used up all your "ammo". Cannot jump or attack, and you move slower for a period of time.
6. Accounts for collision detection between all the other objects in the game as well, and decrements health/lives/etc. accordingly.

SPIDER: Move back and forth on the platforms. They start moving in the direction of the player when hit. If the time, which is incremented by every call to the player's update function, is greater than 5000, the spiders move faster and get harder to kill. 

AMMO: Player shoots these to kill spiders. No physics here--just moves horizontally. Accelerates towards the end just because. (Why not.)

COIN: Coins! Collect them to increase scores. Objective is to get all of them. Disappear when player collides with them.

LISTENERS: Stores the current key pressed and the previous one. Good for detecting whether jump has been called twice already, and preventing the handlers from being stuck on a certain movement if the listener was only accessed once (i.e. if you press the left button and release it, the player should only move left by dx once and then stop moving)

LIVES: Called "Lives" class, but should have been renamed to statistics or something. (Laziness takes over around now.) Tracks and displays lives, health, ammo left, and scores. 

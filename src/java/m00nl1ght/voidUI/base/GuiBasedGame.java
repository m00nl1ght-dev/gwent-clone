package m00nl1ght.voidUI.base;

import java.util.HashMap;
import java.util.Iterator;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;

/**
 * A modified Game class based on the state based slick class
 *
 * @author kevin, m00nl1ght
 */
public abstract class GuiBasedGame implements Game, InputListener {

	/** The list of states making up this game */
	private HashMap<Integer, GuiState> states = new HashMap<Integer, GuiState>();
	/** The current state */
	private GuiState currentState;
	/** The games loader state */
	private ResourceLoader loaderState;
	/** The next state we're moving into if not null */
	private GuiState nextState;
	/** The container holding this game */
	private GameContainer container;
	/** The title of the game */
	private String title;
	/** The game resource folder location */
	private String sourcePath = "";
	/** Wether the games custom loader state has finished loading all the resources */
	private boolean loaderFinished = false;
	
	/**
	 * Create a new gui based game
	 * 
	 * @param name The name of the game
	 */
	public GuiBasedGame(String name) {
		this.title = name; 
	}
	
	/**
	 * Set the game resource folder location.
	 * If no one is set, "" will be used
	 * 
	 * @param state The resource location path
	 */
	public void setResourceLocation(String path) {
		if (path==null) {throw new IllegalArgumentException("resource location can not be set to null!");} 
		sourcePath=path;
	}
	
	/**
	 * Get the game resource folder location.
	 */
	public String getResourceLocation() {
		return sourcePath;
	}
	
	/**
	 * Get the number of states that have been added to this game
	 * 
	 * @return The number of states that have been added to this game
	 */
	public int getStateCount() {
		return states.keySet().size();
	}
	
	/**
	 * Get the ID of the state the game is currently in
	 * 
	 * @return The ID of the state the game is currently in
	 */
	public int getCurrentStateID() {
		return currentState.getID();
	}
	
	/**
	 * Get the state the game is currently in
	 * 
	 * @return The state the game is currently in
	 */
	public GuiState getCurrentState() {
		return currentState;
	}
	
	/**
	 * Add a state to the game. The state will be updated and maintained
	 * by the game
	 * 
	 * @param state The state to be added
	 */
	public void addState(GuiState state) {
		states.put(new Integer(state.getID()), state);
	}
	
	/**
	 * Set the loader state of the game.
	 * If no one is set, the default loader will be used
	 * 
	 * @param state The resource loader
	 */
	public void setLoader(ResourceLoader state) {
		if (state==null) {throw new IllegalArgumentException("loader state can not be set to null!");} 
		loaderState=state;
	}
	
	/**
	 * Get a state based on it's identifier
	 * 
	 * @param id The ID of the state to retrieve
	 * @return The state requested or null if no state with the specified ID exists
	 */
	public GuiState getState(int id) {
		return (GuiState) states.get(new Integer(id));
	}

	/**
	 * Enter a particular game state with no transition
	 * 
	 * @param id The ID of the state to enter
	 */
	public void enterState(int id) {
		nextState = getState(id);
		if (nextState == null) {
			throw new RuntimeException("No game state registered with the ID: "+id);
		}
	}
	
	/**
	 * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
	 */
	public final void init(GameContainer container) throws SlickException {
		this.container = container;
		initStatesList(container);
		if (loaderState==null) {loaderState = new DefaultResourceLoader(container, this, sourcePath);} //use default resource loader
		currentState=loaderState;
		loaderState.renderPre();
		try {loaderState.preload();} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
		currentState.init(container, this);
		currentState.enter(container, this);
	}
	
	/**
	 * Call this from your loader state to initialise all game states (and load their resources)
	 */
	protected final void loaderFinished() throws SlickException {
		if (loaderFinished) {throw new SlickException("ERROR: loaderFinished() called when the loader was already finished before!");}
		loaderFinished=true;
		Iterator<GuiState> gameStates = states.values().iterator();
		while (gameStates.hasNext()) {
			GuiState state = (GuiState) gameStates.next();
			state.init(container, this);
		}
	}

	/**
	 * Initialise the list of states making up this game
	 * 
	 * @param container The container holding the game
	 * @throws SlickException Indicates a failure to initialise the state based game resources
	 */
	public abstract void initStatesList(GameContainer container) throws SlickException;
	
	/**
	 * @see org.newdawn.slick.Game#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
	 */
	public final void render(GameContainer container, Graphics g) throws SlickException {
		preRenderState(container, g);
		
		currentState.render(container, this, g);
		
		postRenderState(container, g);
	}
	
	/**
	 * User hook for rendering at the before the current state
	 * and/or transition have been rendered
	 * 
	 * @param container The container in which the game is hosted
	 * @param g The graphics context on which to draw
	 * @throws SlickException Indicates a failure within render
	 */
	protected void preRenderState(GameContainer container, Graphics g) throws SlickException {
		// NO-OP
	}
	
	/**
	 * User hook for rendering at the game level after the current state
	 * and/or transition have been rendered
	 * 
	 * @param container The container in which the game is hosted
	 * @param g The graphics context on which to draw
	 * @throws SlickException Indicates a failure within render
	 */
	protected void postRenderState(GameContainer container, Graphics g) throws SlickException {
		// NO-OP
	}
	
	/**
	 * @see org.newdawn.slick.BasicGame#update(org.newdawn.slick.GameContainer, int)
	 */
	public final void update(GameContainer container, int delta) throws SlickException {
		preUpdateState(container, delta);
		
		if (nextState != null) {
			currentState.leave(container, this);
			currentState = nextState;
			nextState = null;
			currentState.enter(container, this);
		}
		
		currentState.update(container, this, delta);
		postUpdateState(container, delta);
	}

	/**
	 * User hook for updating at the game before the current state
	 * and/or transition have been updated
	 * 
	 * @param container The container in which the game is hosted
	 * @param delta The amount of time in milliseconds since last update
	 * @throws SlickException Indicates a failure within render
	 */
	protected void preUpdateState(GameContainer container, int delta) throws SlickException {
		// NO-OP
	}
	
	/**
	 * User hook for rendering at the game level after the current state
	 * and/or transition have been updated
	 * 
	 * @param container The container in which the game is hosted
	 * @param delta The amount of time in milliseconds since last update
	 * @throws SlickException Indicates a failure within render
	 */
	protected void postUpdateState(GameContainer container, int delta) throws SlickException {
		// NO-OP
	}
	
	/**
	 * @see org.newdawn.slick.Game#closeRequested()
	 */
	public boolean closeRequested() {
		return true;
	}

	/**
	 * @see org.newdawn.slick.Game#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the container holding this game
	 * 
	 * @return The game container holding this game
	 */
	public GameContainer getContainer() {
		return container;
	}
	
	/**
	 * @see org.newdawn.slick.InputListener#controllerButtonPressed(int, int)
	 */
	public void controllerButtonPressed(int controller, int button) {
		currentState.controllerButtonPressed(controller, button);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerButtonReleased(int, int)
	 */
	public void controllerButtonReleased(int controller, int button) {
		currentState.controllerButtonReleased(controller, button);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerDownPressed(int)
	 */
	public void controllerDownPressed(int controller) {
		currentState.controllerDownPressed(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerDownReleased(int)
	 */
	public void controllerDownReleased(int controller) {
		currentState.controllerDownReleased(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerLeftPressed(int)
	 */
	public void controllerLeftPressed(int controller) {
		currentState.controllerLeftPressed(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerLeftReleased(int)
	 */
	public void controllerLeftReleased(int controller) {
		currentState.controllerLeftReleased(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerRightPressed(int)
	 */
	public void controllerRightPressed(int controller) {
		currentState.controllerRightPressed(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerRightReleased(int)
	 */
	public void controllerRightReleased(int controller) {
		currentState.controllerRightReleased(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerUpPressed(int)
	 */
	public void controllerUpPressed(int controller) {
		currentState.controllerUpPressed(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#controllerUpReleased(int)
	 */
	public void controllerUpReleased(int controller) {
		currentState.controllerUpReleased(controller);
	}

	/**
	 * @see org.newdawn.slick.InputListener#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		currentState.keyPressed(key, c);
	}

	/**
	 * @see org.newdawn.slick.InputListener#keyReleased(int, char)
	 */
	public void keyReleased(int key, char c) {
		currentState.keyReleased(key, c);
	}

	/**
	 * @see org.newdawn.slick.InputListener#mouseMoved(int, int, int, int)
	 */
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		currentState.mouseMoved(oldx, oldy, newx, newy);
	}

	/**
	 * @see org.newdawn.slick.InputListener#mouseDragged(int, int, int, int)
	 */
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		currentState.mouseDragged(oldx, oldy, newx, newy);
	}
	/**
	 * @see org.newdawn.slick.InputListener#mouseClicked(int, int, int, int)
	 */
	public void mouseClicked(int button, int x, int y, int clickCount) {
		currentState.mouseClicked(button, x, y, clickCount);
	}
	
	/**
	 * @see org.newdawn.slick.InputListener#mousePressed(int, int, int)
	 */
	public void mousePressed(int button, int x, int y) {
		currentState.mousePressed(button, x, y);
	}

	/**
	 * @see org.newdawn.slick.InputListener#mouseReleased(int, int, int)
	 */
	public void mouseReleased(int button, int x, int y) {
		currentState.mouseReleased(button, x, y);
	}
	
	/**
	 * @see org.newdawn.slick.InputListener#mouseWheelMoved(int)
	 */
	public void mouseWheelMoved(int newValue) {
		currentState.mouseWheelMoved(newValue);
	}
	
	/**
	 * @see org.newdawn.slick.InputListener#setInput(org.newdawn.slick.Input)
	 */
	public void setInput(Input input) {
	}

	/**
	 * @see org.newdawn.slick.InputListener#isAcceptingInput()
	 */
	public boolean isAcceptingInput() {		
		return currentState.isAcceptingInput();
	}
	
	/**
	 * @see org.newdawn.slick.ControlledInputReciever#inputStarted()
	 */
	public void inputStarted() {}
	
	/**
	 * @see org.newdawn.slick.InputListener#inputEnded()
	 */
	public void inputEnded() {}
	
}

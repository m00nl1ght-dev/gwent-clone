package m00nl1ght.voidUI.base;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class EmptyState implements GuiState {

	@Override
	public int getID() {
		return -2;
	}

	@Override
	public void init(GameContainer container, GuiBasedGame game) throws SlickException {

	}

	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {

	}

	@Override
	public void update(GameContainer container, GuiBasedGame game, int delta) throws SlickException {

	}

	@Override
	public void enter(GameContainer container, GuiBasedGame game) throws SlickException {

	}

	@Override
	public void leave(GameContainer container, GuiBasedGame game) throws SlickException {

	}
	
	@Override public void controllerLeftPressed(int controller) {}
	@Override public void controllerLeftReleased(int controller) {}
	@Override public void controllerRightPressed(int controller) {}
	@Override public void controllerRightReleased(int controller) {}
	@Override public void controllerUpPressed(int controller) {}
	@Override public void controllerUpReleased(int controller) {}
	@Override public void controllerDownPressed(int controller) {}
	@Override public void controllerDownReleased(int controller) {}
	@Override public void controllerButtonPressed(int controller, int button) {}
	@Override public void controllerButtonReleased(int controller, int button) {}
	
	@Override public void keyPressed(int key, char c) {}
	@Override public void keyReleased(int key, char c) {}
	@Override public void mousePressed(int button, int x, int y) {}
	@Override public void mouseMoved(int oldx, int oldy, int newx, int newy) {}
	@Override public void mouseDragged(int oldx, int oldy, int newx, int newy) {}
	@Override public void mouseWheelMoved(int change) {}
	@Override public void mouseClicked(int button, int x, int y, int clickCount) {}
	@Override public void mouseReleased(int button, int x, int y) {}

	@Override
	public void setInput(Input input) {}

	@Override
	public boolean isAcceptingInput() {return false;}

	@Override
	public void inputEnded() {}

	@Override
	public void inputStarted() {}

}

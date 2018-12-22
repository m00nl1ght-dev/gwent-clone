package m00nl1ght.voidUI.gui;

import m00nl1ght.gwent.Main;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.GuiState;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public abstract class GuiScreen extends GuiContainer<GuiElement> implements GuiState {
	
	protected int ID;
	protected SequenceHandler sequenceContext = new SequenceHandler();
	
	protected GuiScreen(int id, float x, float y, float w, float h) {
		super(null, new ElementPos.ParentElementPos(x, y, w, h));
		this.ID=id;
	}
	
	protected GuiScreen(int id, GameContainer gc) {
		this(id, 0, 0, gc.getWidth(), gc.getHeight());
	}

	@Override
	public void init(GameContainer container, GuiBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		pos.update(); this.resetClipping(g);
		super.render(container, game, g);
		sequenceContext.render(g);
	}
	
	@Override
	public void update(GameContainer container, GuiBasedGame game, int delta) throws SlickException {
		sequenceContext.update();
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (key==Input.KEY_ESCAPE) {
			Main.appgc().exit(); return;
		}
		super.keyPressed(key, c);
	}

	@Override
	public void enter(GameContainer container, GuiBasedGame game) throws SlickException {}

	@Override
	public void leave(GameContainer container, GuiBasedGame game) throws SlickException {}
	
	@Override public int getID() {return ID;}
	
	public SequenceHandler seqctx() {return this.sequenceContext;}
	
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
	
	
}

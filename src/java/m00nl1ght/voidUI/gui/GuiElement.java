package m00nl1ght.voidUI.gui;

import m00nl1ght.voidUI.base.GuiBasedGame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;

public abstract class GuiElement implements MouseListener, KeyListener {
	
	protected final ElementPos pos;
	protected boolean visible = true, enabled = true;
	protected final GuiContainer<? extends GuiElement> base;
	
	public GuiElement(GuiContainer<? extends GuiElement> base, ElementPos position) {
		this.base=base; this.pos=position; this.visible=true;
	}
	
	public boolean checkSelected(int mx, int my) {
		if (visible && pos.within(mx, my)) {return true;}
		return false;
	}
	
	public abstract void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException;
	
	public boolean hasFocus() {return base.getSelectedElement()==this;}
	public boolean hasKeyboardFocus() {return base.getKeyboardFocus()==this;}
	public void gainFocus() {}
	public void leaveFocus() {}
	public void gainKeyboardFocus() {}
	public void leaveKeyboardFocus() {}
	
	public void mousePressed(int button, int x, int y) {}
	public void mouseReleased(int button, int x, int y) {}
	public void mouseClicked(int button, int x, int y, int clickCount) {}
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {}
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {}
	public void mouseWheelMoved(int change) {}
	public void keyPressed(int key, char c) {}
	public void keyReleased(int key, char c) {}
	public void setInput(Input input) {}
	public boolean isAcceptingInput() {return true;}
	public void inputEnded() {}
	public void inputStarted() {}
	
	public void setVisible(boolean flag) {visible=flag;}
	public boolean isVisible() {return visible;}
	public void setEnabled(boolean flag) {enabled=flag;}
	public boolean isEnabled() {return enabled;}
	public ElementPos getPos() {return pos;}

}

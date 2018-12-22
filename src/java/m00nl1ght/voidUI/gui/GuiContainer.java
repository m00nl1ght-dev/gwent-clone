package m00nl1ght.voidUI.gui;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

import m00nl1ght.gwent.Main;
import m00nl1ght.voidUI.base.GuiBasedGame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class GuiContainer<T extends GuiElement> extends GuiElement {
	
	protected static final Comparator<GuiElement> DEPTH_COMPARATOR = new Comparator<GuiElement>() {
		public int compare(GuiElement o1, GuiElement o2) {
			int i = Float.compare(o1.pos.depth, o2.pos.depth);
			return i==0?Integer.compare(o1.hashCode(), o2.hashCode()):i;
		}
	};
	
	protected NavigableSet<T> elements = new TreeSet<T>(DEPTH_COMPARATOR);
	protected NavigableSet<T> reverseSet = elements.descendingSet();
	protected T selected, keyfocus = null;
	
	public GuiContainer(GuiContainer<? extends GuiElement> base, ElementPos position) {
		super(base, position);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		for (T e : elements) {
			e.pos.update();
			if (e.isVisible()) {e.render(container, game, g);}
		}
	}
	
	protected void updateSel(int mx, int my) {
		T pre = selected; selected=null;
		for (T e : reverseSet) {
			if (e.checkSelected(mx, my)) {
				selected=e; break;
			}
		} 
		if (pre!=selected) {
			if (selected!=null) {selected.gainFocus();}
			if (pre!=null) {pre.leaveFocus();}
		}
	}
	
	public void updateSelection() {
		updateSel(Main.appgc().getInput().getAbsoluteMouseX(), Main.appgc().getInput().getAbsoluteMouseY());
	}
	
	public T getBelowSelected() {
		int[] m = Main.getMousePos();
		for (T e : reverseSet) {
			if (e==selected) {continue;}
			if (e.checkSelected(m[0], m[1])) {
				return e; 
			}
		} return null;
	}
	
	public void resetClipping(Graphics g) {
		g.setWorldClip(pos.x(), pos.y(), pos.w(), pos.h());
	}
	
	public T addElement(T e) {
		if (!elements.add(e)) {throw new IllegalStateException("GuiContainer already contains item "+e);} this.updateSelection(); return e;
	}
	
	public boolean removeElement(T element) {
		if (elements.remove(element)) {
			if (element==selected) {selected=null; updateSelection();}
			if (element==keyfocus) {keyfocus=null;}
			return true;
		}
		return false;
	}
	
	public void clearElements() {
		elements.clear(); selected=null; keyfocus=null;
	}
	
	public T getSelectedElement() {return selected;}
	public T getKeyboardFocus() {return keyfocus;}
	
	@SuppressWarnings("unchecked")
	public void setSelectedElement(GuiElement element) {
		if (elements.contains(element)) {
			selected=(T)element;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setKeyboardFocus(GuiElement element) {
		if (elements.contains(element)) {
			keyfocus=(T)element;
		}
	}
	
	public void resetSelection() {selected=null; keyfocus=null;}
	
	public void elementCallback(GuiElement element, int eventID, int data) {}
	
	@Override
	public void keyPressed(int key, char c) {
		if (keyfocus!=null) {
			keyfocus.keyPressed(key, c);
		}
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (keyfocus!=null) {
			keyfocus.keyReleased(key, c);
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		updateSel(newx, newy);
		if (selected!=null) {selected.mouseMoved(oldx, oldy, newx, newy);}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		updateSel(oldx, oldy);
		if (selected!=null) {selected.mouseDragged(oldx, oldy, newx, newy);}
	}


	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		updateSel(x, y); keyfocus=selected;
		if (selected!=null) {selected.mouseClicked(button, x, y, clickCount);}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		updateSel(x, y);
		if (selected!=null) {selected.mousePressed(button, x, y);}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		updateSel(x, y);
		if (selected!=null) {selected.mouseReleased(button, x, y);}
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (selected!=null) {selected.mouseWheelMoved(change);}
	}

}

package m00nl1ght.voidUI.gui;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.Main;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos.ScrollableElementPos;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GuiList extends GuiContainer<GuiListEntry> {
	
	private Image iBase, iCover;
	
	private ElementPos clip;
	private ScrollableElementPos items;
	private float nextY = 0F;
	private float spaceY = 0F;

	public GuiList(GuiContainer<? extends GuiElement> parent, ElementPos position, float itemX, float itemY, float scrollDec, float scrollMntmThres, Image iBase, Image iCover) {
		super(parent, position); this.iBase=iBase; this.iCover=iCover; items=new ScrollableElementPos(position, itemX, itemY, position.depth, scrollDec, scrollMntmThres); clip=pos; items.init(0, 0, 0);
	}
	
	public GuiList(GuiContainer<? extends GuiElement> parent, ElementPos position , float itemX, float itemY, float scrollMntmThres, float scrollDec, Image iBase) {
		this(parent, position, itemX, itemY, scrollDec, scrollMntmThres, iBase, null);
	}
	
	public GuiList(GuiContainer<? extends GuiElement> parent, ElementPos position, Image iBase, Image iCover) {
		this(parent, position, 0F, 0F, 0.9F, 0.05F, iBase, iCover);
	}
	
	public GuiList(GuiContainer<? extends GuiElement> parent, ElementPos position, Image iBase) {
		this(parent, position, iBase, null);
	}
	
	public GuiList(GuiContainer<? extends GuiElement> parent, ElementPos position) {
		this(parent, position, Loader.iList1B);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		//Toolkit.drawBase("scroll v: "+items.scroll.val+" min: "+items.scroll.min, 10, 10, Loader.fontB);
		iBase.draw(pos.x(), pos.y(), pos.w(), pos.h());
		this.resetClipping(g);
		items.scroll.updateOnRender();
		items.update();
		super.render(container, game, g);
		base.resetClipping(g);
		if (iCover!=null) iCover.draw(pos.x(), pos.y(), pos.w(), pos.h());
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (selected!=null) {
			items.scroll.drag(oldy, newy);
		}
	}
	
	@Override
	public void mouseWheelMoved(int change) {
		if (selected!=null) {
			items.scroll.applyMomentum(change/5F); //adjust
		}
	}

	@Override
	protected void updateSel(int mx, int my) {
		for (GuiListEntry e : reverseSet) {
			if (e.checkSelected(mx, my)) {
				selected=e; return;
			}
		} selected=null;
	}
	
	@Override
	public void elementCallback(GuiElement element, int eventID, int data) {
		base.elementCallback(element, eventID, data);
	}
	
	@Override
	public GuiListEntry getBelowSelected() {
		int[] m = Main.getMousePos();
		for (GuiListEntry e : reverseSet) {
			if (e==selected) {continue;}
			if (e.checkSelected(m[0], m[1])) {
				return e; 
			}
		} return null;
	}

	@Override
	public GuiListEntry addElement(GuiListEntry element) {
		super.addElement(element);
		nextY+=element.pos.srcH()+spaceY;
		items.scroll.setProperties(Math.min(clip.srcH()+spaceY-nextY, 0), 0, items.scroll.val);
		return element;
	}
	
	@Override
	public boolean removeElement(GuiListEntry element) {
		throw new UnsupportedOperationException("removing list entries is not yet implemented."); //TODO implement (maybe?)
	}
	
	@Override
	public void clearElements() {
		super.clearElements();
		items.scroll.setProperties(0, 0, 0); nextY=0;
	}
	
	@Override
	public void resetClipping(Graphics g) {
		g.setWorldClip(clip.x(), clip.y(), clip.w(), clip.h());
	}
	
	public ElementPos getNextElementPos(float width, float height, float depth) {
		return new ElementPos(items, 0F, nextY, width, height, depth);
	}
	
	@Override
	public void leaveFocus() {
		super.leaveFocus();
		selected=null;
	}
	
	public void setVerticalSpacing(float space) {this.spaceY=space*pos.scaleY;} //correct without scale?
	public void setClipping(float x1, float y1, float x2, float y2) {
		this.clip=new ElementPos(pos, x1, y1, pos.srcW()-x2, pos.srcH()-y2, 0);
	}

}

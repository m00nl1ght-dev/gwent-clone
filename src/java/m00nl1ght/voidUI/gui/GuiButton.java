package m00nl1ght.voidUI.gui;

import m00nl1ght.gwent.Loader;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GuiButton extends GuiElement {
	
	protected Image iActive, iSelected, iDisabled;
	protected String string = "";
	
	public GuiButton(GuiContainer<? super GuiButton> parent, ElementPos position) {
		this(parent, position, "", Loader.iBtn1A, Loader.iBtn1S, Loader.iBtn1D);
	}
	
	public GuiButton(GuiContainer<? super GuiButton> parent, ElementPos position, String str) {
		this(parent, position, str, Loader.iBtn1A, Loader.iBtn1S, Loader.iBtn1D);
	}

	public GuiButton(GuiContainer<? super GuiButton> parent, ElementPos position, Image iActive, Image iSelected, Image iDisabled) {
		this(parent, position, "", iActive, iSelected, iDisabled);
	}
	
	public GuiButton(GuiContainer<? super GuiButton> parent, ElementPos position, String str, Image iActive, Image iSelected, Image iDisabled) {
		super(parent, position);
		this.iActive=iActive; this.iSelected=iSelected; this.iDisabled=iDisabled; this.string =str;
	}
	
	@Override
	public void mouseClicked(int button, int x2, int y2, int clickCount) {
		base.elementCallback(this, 0, button);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (enabled) {
			if (base.getSelectedElement()==this) {
				iSelected.draw(pos.x(), pos.y(), pos.w(), pos.h());
			} else {
				iActive.draw(pos.x(), pos.y(), pos.w(), pos.h());
			}
		} else {
			iDisabled.draw(pos.x(), pos.y(), pos.w(), pos.h());
		}
		Toolkit.drawBaseCentered(string, pos.x()+pos.w()/2, pos.y()+pos.h()/2, Loader.fontB);
	}
	
	public void setString(String str) {this.string=str;}

}

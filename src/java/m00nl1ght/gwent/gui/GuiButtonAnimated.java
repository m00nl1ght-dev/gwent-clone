package m00nl1ght.gwent.gui;

import m00nl1ght.gwent.Loader;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiButton;
import m00nl1ght.voidUI.gui.GuiContainer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GuiButtonAnimated extends GuiButton {
	
	protected float a = 0F;
	protected int am = 0;

	public GuiButtonAnimated(GuiContainer<? super GuiButton> parent, ElementPos position, String str, Image iActive, Image iSelected, Image iDisabled) {
		super(parent, position, str, iActive, iSelected, iDisabled);
	}
	
	public GuiButtonAnimated(GuiContainer<? super GuiButton> parent, ElementPos elementPos, String string) {
		super(parent, elementPos, string);
	}

	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (am==1) {a+=0.1F; if (a>=1F) {a=1F; am=0;}} else if (am==2) {a-=0.1F; if (a<=0F) {a=0F; am=0;}}
		Color.setBlendFactor(a);
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
		Color.resetBlendFactor();
	}
	
	public void show() {am=1;}
	public void hide() {am=2;}
	
	@Override
	public void mouseClicked(int button, int x2, int y2, int clickCount) {
		if (a>=1F) {
			base.elementCallback(this, 0, button);
		}
	}

}

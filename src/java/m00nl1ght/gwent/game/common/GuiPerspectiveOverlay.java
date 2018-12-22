package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.Loader;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiPopup;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GuiPerspectiveOverlay extends GuiPopup {
	
	private int ani = 0;
	private String text = "";
	private final boolean rotated;

	public GuiPerspectiveOverlay(GuiContainer<? extends GuiElement> base, ElementPos position, boolean rotated) {
		super(base, position); this.alpha=0F; this.rotated=rotated;
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		switch (ani) {
		case 1: if (alpha<1F) {alpha+=0.05F;} else {alpha=1F; ani=0;} break;
		case 2: if (alpha>0F) {alpha-=0.05F;} else {alpha=0F; ani=0; text="";} break;
		}
		super.render(container, game, g);
		Color.setBlendFactor(alpha);
		if (rotated) {
			Loader.iOverlay.setRotation(180);
			Loader.iOverlay.draw(pos.x(), pos.y(), pos.x()+pos.w(), pos.y()+pos.h(), 65, 10, 65+pos.w(), 10+pos.h());
			Loader.iOverlay.setRotation(0);
		} else {
			Loader.iOverlay.draw(pos.x(), pos.y(), pos.x()+pos.w(), pos.y()+pos.h(), 65, 10, 65+pos.w(), 10+pos.h());
		}
		Toolkit.drawBaseCentered(text, pos.cX(), pos.cY()+(rotated?-10:10), Loader.fontUX, Color.white);
		Color.setBlendFactor(1F);
	}
	
	@Override
	public boolean checkSelected(int mx, int my) {
		if (alpha>0 && pos.within(mx, my)) {return true;}
		return false;
	}
	
	public boolean isVisible() {return alpha>0 || ani!=0;}
	
	public void alphaMode(int m) {
		if (m<0 || m>4) {m=0; return;}
		if (m==3) {this.alpha=1F; ani=0; return;}
		if (m==4) {this.alpha=0F; ani=0; return;}
		ani=m;
	}
	
	public void setText(String text) {this.text=text;}

}

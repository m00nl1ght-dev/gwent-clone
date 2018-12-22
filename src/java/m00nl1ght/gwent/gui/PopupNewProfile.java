package m00nl1ght.gwent.gui;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.Profile;
import m00nl1ght.gwent.StateSplash;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.ElementPos.AlignType;
import m00nl1ght.voidUI.gui.GuiButton;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiInput;
import m00nl1ght.voidUI.gui.GuiPopup;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class PopupNewProfile extends GuiPopup {
	
	private GuiButton btnClose, btnUp, btnDown, btnConfirm;
	private GuiInput inName;
	private int charID = 0, charCnt = 3;

	public PopupNewProfile(GuiContainer<? extends GuiElement> base, ElementPos position, float shadow) {
		super(base, position, Loader.iWoodflat, shadow);
		addElement(btnClose=new GuiButton(this, new ElementPos(pos, -10, -10, 82, 82, 0, AlignType.TOPRIGHT), Loader.iCloseB, Loader.iCloseS, Loader.iCloseD));
		addElement(btnUp=new GuiButton(this, new ElementPos(this, 105, 30, 40, 31, 0), Loader.iArrowU, Loader.iArrowU, Loader.iArrowU));
		addElement(btnDown=new GuiButton(this, new ElementPos(this, 105, 160, 40, 31, 0), Loader.iArrowD, Loader.iArrowD, Loader.iArrowD));
		addElement(btnConfirm=new GuiButton(this, new ElementPos(this, 0, 340, 441, 98, 0, AlignType.CENTEREDX), "create profile"));
		addElement(inName=new LowerInput(this, new ElementPos(this, 50, 50, 436, 66, 0, AlignType.CENTEREDX), Loader.iInputF, Loader.iInputS, Loader.iInputF));
		inName.setHint("enter name ...");
		inName.setFont(Loader.fontW, new Color(1F,1F,1F,0.8F));
		inName.setHintFont(Loader.fontW, new Color(1F,1F,1F,0.4F));
		inName.setOffset(25F, 0, 10);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		this.resetClipping(g);
		if (alpha<=0F) {return;}
		if (alpha<1F) Color.setBlendFactor(alpha);
		if (overlayColor!=null) {
			g.setColor(overlayColor);
			g.fillRect(base.getPos().x(), base.getPos().y(), base.getPos().w(), base.getPos().h());
		}
		if (iBase!=null) iBase.draw(pos.x(), pos.y(), pos.w(), pos.h(), Color.white);
		Loader.iChar[charID].draw(pos.x()+67, pos.y()+53, 115, 115);
		Loader.iCframe[0].draw(pos.x()-25, pos.y()-40, 300, 300);
		for (GuiElement e : elements) {
			e.getPos().update();
			if (e.isVisible()) {e.render(container, game, g);}
		}
		Color.resetBlendFactor();
	}
	
	@Override
	public void elementCallback(GuiElement element, int eventID, int data) {
		if (element==btnClose) {
			this.setVisible(false);
			return;
		}
		if (element==btnUp) {
			charID -= 1; if (charID<0) charID=charCnt;
			return;
		}
		if (element==btnDown) {
			charID += 1; if (charID>charCnt) charID=0;
			return;
		}
		if (element==btnConfirm) {
			if (inName.getString().length()<3) return;
			Profile.getList().add(new Profile(inName.getString(), charID));
			((StateSplash)base).updateProfileList();
			this.setVisible(false); return;
		}
		super.elementCallback(element, eventID, data);
	}
	
	public void reset() {
		inName.setString("");
		charID=0;
	}
	
	@Override
	public boolean checkSelected(int mx, int my) {
		return this.visible;
	}

}

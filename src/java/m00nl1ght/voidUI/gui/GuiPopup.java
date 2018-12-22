package m00nl1ght.voidUI.gui;

import m00nl1ght.voidUI.base.GuiBasedGame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GuiPopup extends GuiContainer<GuiElement> {
	
	protected Image iBase;
	protected Color overlayColor = null;
	protected float alpha = 1F;

	public GuiPopup(GuiContainer<? extends GuiElement> base, ElementPos position, Image iBase, float shadow) {
		this(base, position); this.iBase=iBase; if (shadow>0F) overlayColor = new Color(0F, 0F, 0F, shadow);
	}
	
	public GuiPopup(GuiContainer<? extends GuiElement> base, ElementPos position) {
		super(base, position);
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
		if (iBase!=null) iBase.draw(pos.x(), pos.y(), pos.w(), pos.h(), overlayColor);
		super.render(container, game, g);
		Color.resetBlendFactor();
	}

	@Override
	public void resetClipping(Graphics g) {
		base.resetClipping(g);
	}

	@Override
	public void setVisible(boolean flag) {super.setVisible(flag); selected=null; base.setSelectedElement(this);}
	
	public void setAlpha(float a) {this.alpha=Math.min(1F, Math.max(0F, a));}
	
}

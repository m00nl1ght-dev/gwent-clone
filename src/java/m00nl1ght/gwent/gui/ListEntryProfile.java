package m00nl1ght.gwent.gui;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.Profile;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.gui.GuiList;
import m00nl1ght.voidUI.gui.GuiListEntry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ListEntryProfile extends GuiListEntry {
	
	public final Profile profile;
	int strOffset;
	private float shiftH = 0F;

	public ListEntryProfile(GuiList base, Profile profile, float width, float height, float depth, int strOffset) {
		super(base, width, height, depth); this.profile=profile; this.strOffset=strOffset;
	}
	
	public ListEntryProfile setShiftOnHover(float onHover) {
		shiftH=onHover;
		return this;
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		Loader.iFtile[1].draw(pos.x() + (base.getSelectedElement()==this ? shiftH : 0F), pos.y(), pos.w(), pos.h());
		Toolkit.drawBaseCenteredH(profile.getName().toLowerCase(), pos.x()+strOffset*pos.scaleX() + (base.getSelectedElement()==this ? shiftH : 0F), pos.y() + pos.h() / 2, Loader.fontB);
	}

}

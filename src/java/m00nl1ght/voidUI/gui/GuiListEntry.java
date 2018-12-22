package m00nl1ght.voidUI.gui;


public abstract class GuiListEntry extends GuiElement {

	public GuiListEntry(GuiList base, float width, float height, float depth) {
		super(base, base.getNextElementPos(width, height, depth));
	}
	
	@Override
	public void mouseClicked(int button, int x2, int y2, int clicks) {
		base.elementCallback(this, 0, button);
	}

}

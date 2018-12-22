package m00nl1ght.gwent.gui;

import m00nl1ght.voidUI.gui.GuiPopup;
import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class OpenPopupTask extends SequenceTask {
	
	private GuiPopup popup;
	private float a = 0F;
	private final float spd;

	public OpenPopupTask(SequenceHandler container, int priority, GuiPopup popup, float spd) {
		super(container, priority); this.popup=popup; this.spd=spd;
	}

	public void update() {
		a+=spd;
		popup.setAlpha(a);
		if (a>=1F) {completed();}
	}
	
	public boolean start() {
		return true;
	}
	
	public String toString() {
		return "OpenPopup {a:"+a+"}";
	}

	
}

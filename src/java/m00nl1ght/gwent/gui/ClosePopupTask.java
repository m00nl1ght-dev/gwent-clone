package m00nl1ght.gwent.gui;

import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.voidUI.gui.GuiPopup;
import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class ClosePopupTask extends SequenceTask {

	private GuiPopup popup;
	private float a = 1F;
	private final float spd;
	private boolean remove;

	public ClosePopupTask(SequenceHandler container, int priority, GuiPopup popup, float spd, boolean remove) {
		super(container, priority); this.popup=popup; this.spd=spd; this.remove=remove;
	}

	public void update() {
		a-=spd;
		popup.setAlpha(a);
		if (a<=0F) {completed();}
	}
	
	public boolean start() {
		return true;
	}

	public void completed() {
		if (remove) {StateGame.get().removeElement(popup);} else {popup.setVisible(false);}
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "ClosePopup {a:"+a+"}";
	}

}

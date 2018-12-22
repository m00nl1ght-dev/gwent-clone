package m00nl1ght.gwent.game.common;

import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceTask;

public class CardChooserCloseTask extends SequenceTask {

	private ICardChooserUI chooser;
	private float a = 1F;
	private final float spd;

	public CardChooserCloseTask(SequenceHandler container, int priority, ICardChooserUI chooser, float spd) {
		super(container, priority); this.chooser=chooser; this.spd=spd;
	}

	public void update() {
		a-=spd;
		chooser.setAlpha(a<0?0:a);
		if (a<=0F) {completed();}
	}
	
	public boolean start() {
		return true;
	}

	public void completed() {
		chooser.disable();
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "CloseCardChooser {a:"+a+"}";
	}

}

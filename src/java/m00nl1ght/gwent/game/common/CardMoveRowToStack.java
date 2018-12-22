package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceTask;

public class CardMoveRowToStack extends CardMoveTask {
	
	protected CardRow row;
	protected CardStack stack;
	protected SequenceTask ani; 
	protected int wait;
	private final boolean useACANI;

	public CardMoveRowToStack(SequenceHandler container, int priority, Card card, CardRow row, CardStack stack, int frames, int wait, boolean useACANI) {
		super(container, priority, card); this.row=row; this.stack=stack; this.frames=frames; this.wait=wait; this.useACANI=useACANI;
	}
	
	public boolean start() {
		if (row.isPushing()) {return false;}
		if (wait>0) {wait--; return false;}
		if (useACANI) {
			ani = row.new RemoveCardAnimation(handler, this.getPriority(), card, 0.1F);
		} else {
			row.remove(card);
		}
		initCardMove(stack.getPos().x(), stack.getPos().y(), stack.getPos().w(), stack.getPos().h(), frames);
		return true;
	}
	
	public void completed() {
		stack.add(card);
		this.unregister();
	}
	
	public boolean isProgressing() {
		return true;
	}
	
	@Override
	public String toString() {
		return "MoveRowToStack {c:"+card+"}";
	}

}

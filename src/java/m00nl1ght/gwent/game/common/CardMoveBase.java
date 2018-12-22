package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class CardMoveBase extends CardMoveTask {
	
	protected CardContainer target;
	protected float tX, tY, tW, tH;

	public CardMoveBase(SequenceHandler container, int priority, float tX, float tY, float tW, float tH, CardContainer target, Card card, int frames) {
		super(container, priority, card); this.frames=frames; this.target=target;
		this.tX=tX; this.tY=tY; this.tW=tW; this.tH=tH;
	}
	
	@Override
	public boolean start() {
		initCardMove(tX, tY, tW, tH, frames);
		return true;
	}
	
	@Override
	public void update() {
		if (updateCardMove()) {this.completed();}
	}
	
	@Override
	public void completed() {
		target.add(card);
		this.unregister();
	}
	
	public String toString() {
		return "CardMove {c:"+card+"}";
	}


}

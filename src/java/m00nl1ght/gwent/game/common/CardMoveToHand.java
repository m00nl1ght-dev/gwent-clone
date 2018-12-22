package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardRow.AddCardAnimation;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class CardMoveToHand extends CardMoveTask {
	
	private int frames = 0;
	private CardRow hand;
	private AddCardAnimation acani;
	private float aspd = 0.1F;
	
	public CardMoveToHand(SequenceHandler container, int priority, CardRow hand, Card card, int frames, float pushSpd, int cardVariantAni) {
		super(container, priority, card, cardVariantAni);
		this.card=card; this.hand=hand; this.frames=frames; this.aspd=pushSpd;
	}
	
	public CardMoveToHand(SequenceHandler container, int priority, CardRow hand, Card card, int frames, float pushSpd) {
		this(container, frames, hand, card, frames, pushSpd, 0);
	}
	
	@Override
	public boolean start() {
		if (hand.isPushing()) {return false;}
		acani = hand.new AddCardAnimation(null, this.getPriority(), card, aspd);
		initCardMove(hand.getPos().x()+hand.requestCardPosition(acani.getPushIdx()), hand.getPos().y(), 100F, 123F, frames);
		return true;
	}
	
	@Override
	public void update() {
		if (step==frames-(1F/aspd)) {acani.register(handler);}
		if (updateCardMove()) {this.completed();}
	}
	
	@Override
	public void completed() {
		acani.completed();
		hand.updateSelection(true);
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "MoveToHand {c:"+card+"}";
	}
	
}

package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardRow.AddCardAnimation;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class CardDrawToHand extends CardMoveTask {
	
	protected int frames1 = 25, frames2 = 25, frames3 = 25;
	protected float aspd = 0.1F;
	protected ElementPos tar;
	private int state = 1;
	private CardRow hand;
	private AddCardAnimation acani;
	
	public CardDrawToHand(SequenceHandler container, int priority, CardRow hand, Card card, ElementPos tar, int cardVariantAni) {
		super(container, priority, card, cardVariantAni);
		this.card=card; this.hand=hand; this.tar=tar;
	}
	
	public CardDrawToHand(SequenceHandler container, int priority, CardRow hand, Card card, ElementPos tar) {
		this(container, priority, hand, card, tar, 0);
	}
	
	@Override
	public boolean start() {
		if (hand.isPushing()) {return false;}
		if (this.card==null) {this.unregister(); return false;}
		this.card.w=100F; this.card.h=123F;
		acani = hand.new AddCardAnimation(null, this.getPriority(), this.card, aspd);
		initCardMove(tar, frames1);
		return true;
	}
	
	@Override
	public void update() {
		switch (state) {
		case 1:
			if (updateCardMove()) {state++; step=1; this.cardVariantAni=0;} break;
		case 2:
			if (step>=frames2) {
				state++; // init state 3
				initCardMove(hand.getPos().x()+hand.requestCardPosition(acani.getPushIdx()), hand.getPos().y(), 100F, 123F, frames3);
			} else {step++;} break;
		case 3:
			if (step==frames3-(1F/aspd)) {acani.register(handler);}
			if (updateCardMove()) {this.completed();} break;
		}
		
	}
	
	@Override
	public void completed() {
		acani.completed();
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "DrawToHand {s:"+state+" c:"+card+"}";
	}

}

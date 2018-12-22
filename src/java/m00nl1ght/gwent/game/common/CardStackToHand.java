package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardRow.AddCardAnimation;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Graphics;

public class CardStackToHand extends CardMoveTask {
	
	protected int frames1 = 25, frames2 = 25, frames3 = 25;
	protected float aspd = 0.1F;
	protected ElementPos tar;
	private int state = 1;
	private CardStack stack;
	private CardRow hand;
	private AddCardAnimation acani;
	
	public CardStackToHand(SequenceHandler container, int priority, CardStack stack, CardRow hand, Card card, ElementPos tar) {
		super(container, priority, card);
		this.card=card; this.hand=hand; this.tar=tar; this.stack=stack;
	}
	
	@Override
	public void render(Graphics g) {
		card.draw(g, 1, -1);
	}
	
	@Override
	public boolean start() {
		if (hand.isPushing()) {return false;}
		if (this.card==null) {this.card=stack.get(); if (this.card==null) {this.unregister(); return false;}}
		this.card.x=stack.getPos().x(); this.card.y=stack.getPos().y(); this.card.w=100F; this.card.h=123F;
		acani = hand.new AddCardAnimation(null, this.getPriority(), this.card, aspd);
		initCardMove(tar, frames1);
		stack.remove(card);
		return true;
	}
	
	@Override
	public void update() {
		switch (state) {
		case 1:
			if (updateCardMove()) {state++; step=1;} break;
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
		return "StackToHand {s:"+state+" c:"+card+"}";
	}

}

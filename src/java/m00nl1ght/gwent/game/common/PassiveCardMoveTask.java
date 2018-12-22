package m00nl1ght.gwent.game.common;

import org.newdawn.slick.Graphics;

import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public abstract class PassiveCardMoveTask extends SequenceTask {
	
	protected float stepx, stepy, stepw, steph, x, y, w, h;
	protected int step = 0, frames;
	protected Card card;

	public PassiveCardMoveTask(SequenceHandler container, int priority, Card card) {
		super(container, priority); this.card=card;
	}
	
	@Override
	public void render(Graphics g) {
		card.drawIn(g, x, y, w, h, 1, -1, false);
	}
	
	public void initPos(float x, float y, float w, float h) {
		this.x=x; this.y=y; this.w=w; this.h=h;
	}
	
	public void initCardMove(float tx, float ty, float tw, float th, int frames) {
		stepw = (tw-w)/frames;
		steph = (th-h)/frames;
		stepx = (tx-x)/frames;
		stepy = (ty-y)/frames;
		this.frames = frames;
		step=1;
	}
	
	public boolean updateCardMove() {
		x+=stepx; y+=stepy; w+=stepw; h+=steph;
		if (step>=frames) {return true;}
		step++; return false;
	}
	
	public Card getCard() {return card;}


}

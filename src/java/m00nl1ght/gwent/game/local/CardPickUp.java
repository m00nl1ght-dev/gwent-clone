package m00nl1ght.gwent.game.local;

import org.newdawn.slick.Graphics;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardMoveTask;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class CardPickUp extends CardMoveTask {
	
	protected float tW, tH;
	protected DraggedCard dragged;

	public CardPickUp(SequenceHandler container, int priority, Card card, DraggedCard dragged, float tW, float tH, int frames) {
		super(container, priority, card); this.dragged=dragged; this.tW=tW; this.tH=tH; this.frames=frames;
	}
	
	@Override
	public boolean start() {
		ElementPos pos = dragged.getPos();
		card.x=pos.x(); card.y=pos.y(); card.w=pos.w(); card.h=pos.h();
		initCardMove(card.x-(tW-card.w)/2, card.y-(tH-card.h)/2, tW, tH, frames);
		return true;
	}
	
	@Override
	public void render(Graphics g) {}
	
	@Override
	public void update() {
		dragged.getPos().add(stepx, stepy, stepw, steph);
		if (step>=frames) {completed(); return;}
		step++;
	}
	
	@Override
	public String toString() {
		return "CardPickUp {c:"+card+"}";
	}

}

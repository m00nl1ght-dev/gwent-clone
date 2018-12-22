package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.sequence.Interactable;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceTask;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class CardMoveTask extends SequenceTask {
	
	protected float stepx, stepy, stepw, steph;
	protected int step = 0, frames;
	protected Card card;
	protected int cardVariantAni;

	public CardMoveTask(SequenceHandler container, int priority, Card card, int cardVariantAni) {
		super(container, priority); this.card=card; this.cardVariantAni=cardVariantAni;
	}
	
	public CardMoveTask(SequenceHandler container, int priority, Card card) {
		this(container, priority, card, 0);
	}
	
	@Override
	public void render(Graphics g) {
		switch (cardVariantAni) {
		case 1:
			float a = step/(float)frames;
			Color.setBlendFactor(1F-a);
			card.draw(g, 1, -1);
			Color.setBlendFactor(a);
			card.drawFull(g, 1, -1);
			Color.resetBlendFactor();
			break;
		case 2:
			float a1 = step/(float)frames;
			Color.setBlendFactor(a1);
			card.draw(g, 1, -1);
			Color.setBlendFactor(1F-a1);
			card.drawFull(g, 1, -1);
			Color.resetBlendFactor();
			break;
		default:
			card.draw(g, 1, -1); break;
		}
	}
	
	@Override
	public void update() {
		if (updateCardMove()) {completed();}
	}
	
	public void initCardMove(ElementPos pos, int frames) {
		this.initCardMove(pos.x(),  pos.y(), pos.w(), pos.h(), frames);
	}
	
	public void initCardMove(float tx, float ty, float tw, float th, int frames) {
		stepw = (tw-card.w)/frames;
		steph = (th-card.h)/frames;
		stepx = (tx-card.x)/frames;
		stepy = (ty-card.y)/frames;
		this.frames = frames;
		step=1;
	}
	
	public boolean updateCardMove() {
		card.x+=stepx; card.y+=stepy; card.w+=stepw; card.h+=steph;
		if (step>=frames) {return true;}
		step++; return false;
	}
	
	public Card getCard() {return card;}
	
	public interface CardCallback extends Interactable {
		
		public void result(Card card);
		public void cancel();
		public void exception(int i);
		
	}
	
	public static abstract class CardCallbackBase implements CardCallback {
		
		protected final SequenceHandler seqctx;
		public CardCallbackBase(SequenceHandler seqctx) {this.seqctx=seqctx;}
		public final SequenceHandler getHandler() {
			return seqctx;
		}
		public int getPriority() {return -1;}
		
	}
	
	public interface CardStackCallback extends Interactable {
		
		public abstract void result(Card[] card);
		public abstract void cancel();
		public abstract void exception(int i);
		
	}
	
	public static abstract class CardStackCallbackBase implements CardStackCallback {
		
		protected final SequenceHandler seqctx;
		public CardStackCallbackBase(SequenceHandler seqctx) {this.seqctx=seqctx;}
		public final SequenceHandler getHandler() {
			return seqctx;
		}
		public int getPriority() {return -1;}
		
	}

}

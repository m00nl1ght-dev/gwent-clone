package m00nl1ght.gwent.game.opponent;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.common.CardMoveTask;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class OppCardMoveToPending extends CardMoveTask {
	
	private int frames = 0;
	private Perspective<?> persp;
	
	public OppCardMoveToPending(SequenceHandler container, int priority, Perspective<?> persp, Card card, int frames, int cardVariantAni) {
		super(container, priority, card, cardVariantAni);
		this.card=card; this.frames=frames; this.persp=persp;
	}
	
	@Override
	public boolean start() {
		initCardMove(persp.pendingPos(), frames);
		persp.setOverlayState(1);
		return true;
	}
	
	@Override
	public void update() {
		if (updateCardMove()) {completed();}
	}
	
	@Override
	public void completed() {
		persp.placeAddCardOnBoard(this.handler, card);
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "MoveToPending {c:"+card+"}";
	}

}

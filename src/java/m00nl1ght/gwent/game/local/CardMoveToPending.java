package m00nl1ght.gwent.game.local;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.LocalPlayer;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.common.CardMoveTask;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class CardMoveToPending extends CardMoveTask {
	
	private int frames = 0;
	private Perspective<LocalPlayer> persp;
	
	public CardMoveToPending(SequenceHandler container, int priority, Perspective<LocalPlayer> persp, Card card, int frames, int cardVariantAni) {
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
		persp.placeAddCardOnBoard(this.handler, card); //TODO test: this.handler was StateGame#state() originally
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "MoveToPending {c:"+card+"}";
	}

}

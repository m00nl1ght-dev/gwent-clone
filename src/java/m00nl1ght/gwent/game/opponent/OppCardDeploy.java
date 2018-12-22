package m00nl1ght.gwent.game.opponent;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardMoveTask;
import m00nl1ght.gwent.game.common.CardRow;
import m00nl1ght.gwent.game.common.CardRow.AddCardAnimation;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Graphics;

public class OppCardDeploy extends CardMoveTask {
	
	protected int frames1 = 24, frames2 = 22, frames3 = 20;
	protected float aspd = 0.1F;
	private int state = 1, rowIdx = 0;
	private float reqX;
	private CardRow row;
	private AddCardAnimation acani;
	public final OppPendingCard pending;
	
	public OppCardDeploy(SequenceHandler container, int priority, CardRow row, Card card, int rowIdx, OppPendingCard nullable) {
		super(container, priority, card);
		this.row=row; this.rowIdx=rowIdx; this.pending=nullable;
	}
	
	@Override
	public void render(Graphics g) {
		card.drawWithRotation(card.x, card.y, card.w, card.h, 1, 2, false);
	}
	
	@Override
	public boolean start() {
		if (row.isPushing()) {return false;}
		acani = row.new AddCardAnimation(null, this.getPriority(), card, aspd);
		acani.setPushIdx(rowIdx);
		reqX = row.requestCardPosition(rowIdx);
		initCardMove(row.getPos().x()+reqX-15, row.getPos().y()-15, 122F, 150F, frames1);
		if (pending!=null) {StateGame.get().removeElement(pending);}
		return true;
	}
	
	@Override
	public void update() {
		switch (state) {
		case 1:
			if (updateCardMove()) {if (pending!=null) {card.getLocation().persp.setOverlayState(2);} state++; step=1;} break;
		case 2:
			float r = card.getRotation();
			if (r>0F) {r-=1F/frames2; if (r<0F) {r=0F;}}
			card.setRotation(r);
			if (step>=frames2) {
				state++; // init state 3
				card.setRotation(0F);
				initCardMove(row.getPos().x()+reqX, row.getPos().y(), 90F, 110F, frames3);
				acani.register(handler);
			} else {step++;} break;
		case 3:
			if (updateCardMove()) {this.completed();} break;
		}
	}
	
	@Override
	public void completed() {
		acani.completed();
		card.deploy(StateGame.get().state());
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "DeployOpponent {s:"+state+" c:"+card+"}";
	}

}

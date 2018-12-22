package m00nl1ght.gwent.game.local;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardMoveTask;
import m00nl1ght.gwent.game.common.CardRow;
import m00nl1ght.gwent.game.common.CardRow.AddCardAnimation;
import m00nl1ght.gwent.game.local.DraggedCard.PhantomPrewiewAnimation;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Graphics;

public class CardDeployToRow extends CardMoveTask {
	
	protected int frames1 = 10, frames2 = 4, frames3 = 16;
	protected float aspd = 0.1F;
	private int state = 1, rowIdx = 0;
	private float reqX;
	private CardRow row;
	private AddCardAnimation acani;
	private PhantomPrewiewAnimation phani;
	private final boolean useACANI;
	
	public CardDeployToRow(SequenceHandler container, int priority, CardRow row, Card card, PhantomPrewiewAnimation phani, int rowIdx, boolean useACANI) {
		super(container, priority, card); this.useACANI=useACANI; this.phani=phani;
		this.row=row; this.rowIdx=rowIdx;
	}
	
	@Override
	public void render(Graphics g) {
		if (phani!=null) {phani.renderD(g);}
		card.draw(g, 1, -1);
	}
	
	@Override
	public boolean start() {
		if (row.isPushing()) {return false;}
		if (useACANI) {
			acani = row.new AddCardAnimation(null, this.getPriority(), card, aspd);
			acani.setPushIdx(rowIdx);
		}
		reqX = row.requestCardPosition(rowIdx);
		initCardMove(row.getPos().x()+reqX-15, row.getPos().y()-15, 122F, 150F, frames1);
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
				initCardMove(row.getPos().x()+reqX, row.getPos().y(), 90F, 110F, frames3);
				if (useACANI) {acani.register(handler);}
			} else {step++;} break;
		case 3:
			if (updateCardMove()) {this.completed();} break;
		}
		if (phani!=null) {phani.update();}
	}
	
	@Override
	public void completed() {
		if (useACANI) {acani.completed();} else {
			row.add(card, rowIdx);
			row.updateSelection(true);
		}
		card.deploy(StateGame.get().state());
		this.unregister();
	}
	
	@Override
	public String toString() {
		return "DeployToRow {s:"+state+" u: "+useACANI+" c:"+card+"}";
	}

}

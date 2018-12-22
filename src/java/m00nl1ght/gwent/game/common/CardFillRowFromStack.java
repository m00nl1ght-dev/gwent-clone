package m00nl1ght.gwent.game.common;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class CardFillRowFromStack extends CardMultiMoveTask {
	
	private CardStack stack;
	private CardRow row;

	public CardFillRowFromStack(SequenceHandler container, int priority, CardStack stack, CardRow row, ArrayList<Card> list, int frames, int fDelta) {
		super(container, priority, frames, fDelta); this.stack=stack; this.row=row; this.list=list;
	}
	
	@Override
	public boolean start() {
		if (row.isPushing()) {return false;}
		this.init(list);
		float[] vec = row.calcVec(list.size(), -1);
		row.initFillOperation();
		for (int i=0; i<list.size(); i++) {
			Card card = list.get(i);
			stack.remove(card);
			initCardMove(i, row.getPos().x()+vec[i], row.getPos().y());
		} return true;
	}
	
	@Override
	public void update() {
		if (updateCardMove()) {this.completed();}
	}
	
	@Override
	public void completed() {
		row.fillWith(list, false);
		this.unregister();
	}
	
	@Override
	public String toString() {
		int s=list==null?0:list.size();
		return "FillRowFromStack {i:"+s+"}";
	}

}

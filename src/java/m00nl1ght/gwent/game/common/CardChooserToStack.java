package m00nl1ght.gwent.game.common;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardRow.CardData;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class CardChooserToStack extends CardMultiMoveTask {
	
	protected ICardChooserUI chooser;
	private CardStack stack;
	protected float a = 0F;
	protected int wait = 0;

	public CardChooserToStack(SequenceHandler container, int priority, CardStack stack, ICardChooserUI chooser, int wait) {
		super(container, priority, 20, 0); this.chooser=chooser; this.stack=stack; this.wait=wait;
	}
	
	@Override
	public void render(Graphics g) {
		if (!chooser.shouldDrawAni()) {return;}
		for (int i=0; i<list.size(); i++) {
			if (step[i]>=0) {
				Color.setBlendFactor(1F-a);
				list.get(i).draw(g, 1, -1);
				Color.setBlendFactor(a);
				list.get(i).drawFull(g, 1, -1);
				Color.resetBlendFactor();
			}
		}
	}
	
	@Override
	public void update() {
		a = 1F - ((float)step[list.size()-1] / frames);
		chooser.setAlpha(a);
		if (updateCardMove()) {this.completed();}
	}
	
	@Override
	public boolean start() {
		if (wait>0) {wait--; return false;}
		StateGame.get().setLocalHighlighted(null, 0, false);
		ArrayList<Card> temp = new ArrayList<Card>();
		for (CardData data : chooser.row().getDataList()) {if (data.data>=0F) {temp.add(data.card);}} // only visible cards from row
		this.init(temp);
		if (list.size()<=0) {throw new IllegalStateException("cannot move cardchooser`s cards to a stack when cardlist is empty!");}
		for (int i=0; i<list.size(); i++) {
			Card card = list.get(i);
			chooser.row().arrangeCard(card); // not absolutely necessary (?), but good for safety (could be bug source though :D )
			initCardMove(i, stack.getPos().x(), stack.getPos().y(), stack.getPos().w(), stack.getPos().h());
		}
		chooser.row().clear(); a=1F;
		return true;
	}

	@Override
	public void completed() {
		chooser.disable();
		for (Card card : list) {stack.addRandom(card);}
		this.unregister();
	}
	
	public ICardChooserUI getChooser() {
		return chooser;
	}
	
	public CardStack getStack() {
		return stack;
	}
	
	public String toString() {
		return "ccToStack {a:"+a+"}";
	}

}

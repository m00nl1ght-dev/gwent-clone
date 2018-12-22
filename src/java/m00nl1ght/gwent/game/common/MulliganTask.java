package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.Main;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardRow.CardData;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class MulliganTask extends CardMoveTask {
	
	protected ICardChooserUI chooser;
	protected Card card0;
	protected CardStack stack;
	protected boolean state = false, last = false;
	protected int idx;
	protected float a = 0F, ox, oy, ow, oh;

	public MulliganTask(SequenceHandler container, int priority, Card card, ICardChooserUI chooser, CardStack stack, int frames, boolean last) {
		super(container, priority, card); this.chooser=chooser; this.frames=frames; this.stack=stack; this.last=last;
	}
	
	@Override
	public void render(Graphics g) {
		if (!chooser.shouldDrawAni()) {return;}
		Color.setBlendFactor(a);
		card.drawFull(g, 1, -1);
		Color.resetBlendFactor();
		try {chooser.row().render(Main.appgc(), Main.instance(), g);} catch (SlickException e) {throw new IllegalStateException("Error drawing chooser row.");}
	}
	
	@Override
	public boolean start() {
		if (!state) {
			idx = chooser.row().idxOf(card); CardData data = chooser.row().getDataList().get(idx);
			if (idx<0) { throw new IllegalStateException("Card to be mulliganed was not found in the given row!");}
			data.data=-1F; chooser.row().setSelected(-1); chooser.row().scroll.lock(true);
			ox=card.x=chooser.row().getPos().x()+chooser.row().scroll.get()+chooser.row().getStableVec()[idx];
			oy=card.y=chooser.row().getPos().y();
			ow=card.w=chooser.row().cardW;
			oh=card.h=chooser.row().cardH;
			initCardMove(stack.getPos().x(), stack.getPos().y(), stack.getPos().w(), stack.getPos().h(), frames);
		} else{
			initCardMove(ox, oy, ow, oh, frames);
		}
		return true;
	}
	
	@Override
	public void update() {
		a = (float)step / frames;
		if (!state) a = 1F - a;
		if (updateCardMove()) {this.completed();}
	}
	
	@Override
	public void completed() {
		if (!state) {
			card0 = card;
			card = stack.get();
			state=true; start();
		} else {
			chooser.row().set(idx, card);
			stack.remove(card);
			stack.addRandom(card0);
			card.updateLocation(chooser.row().getCardLocation());
			chooser.row().updateSelection(true);
			chooser.row().scroll.lock(last);
			this.unregister();
		}
	}
	
	public String toString() {
		return "MulliganTask {s:"+state+" a:"+a+" c:"+card0+"}";
	}


}

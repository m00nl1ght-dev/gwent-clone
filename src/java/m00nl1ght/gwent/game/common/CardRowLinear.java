package m00nl1ght.gwent.game.common;

import java.util.Collection;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.ScrollHelper;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CardRowLinear extends CardRow {
	
	public ScrollHelper scroll = new ScrollHelper(0.9F, 0.05F);
	protected boolean mutable;

	public CardRowLinear(GuiContainer<? extends GuiElement> base, ElementPos position, CardLocation location, boolean mutable) {
		super(base, position, location); this.mutable=mutable;
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (cards.size()<=0) {return;}
		if (scroll.updateOnRender()) this.updateSelection(false);
		for (int i=0; i<cards.size();i++) {
			CardData card = cards.get(i);
			if (card.data<0F) {continue;}
			if (baseVec[i] + scroll.get() - nSpacing> pos.w()) {continue;}
			if (baseVec[i] + scroll.get() + cardW < 0F) {continue;}
			card.card.draw(g, pos.x() + baseVec[i] + scroll.get(), pos.y(), cardW, cardH, 1, card==selected ? 1 : StateGame.get().playerO().getHighlighted()==card.card ? 2 : -1);
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (mutable) scroll.drag(oldx, newx);
	}
	@Override
	public void mouseClicked(int button, int x, int y, int clicks) {
		if (!visible || !mutable) {return;}
		updateSel(x, y);
		if (selected!=null && button==0) {
			base.elementCallback(this, 1, cards.indexOf(selected));
		}
	}
	@Override
	public void mouseWheelMoved(int change) {
		if (mutable) scroll.applyMomentum(change*0.25F);
	}
	@Override
	void updateSel(int mx, int my) {
		mx-=pos.x()+scroll.get();
		if (selected != null) {
			int idx=cards.indexOf(selected);
			if (idx>=0 && mx>baseVec[idx] && mx<baseVec[idx]+cardW) {return;}
		}
		for (int i=cards.size()-1; i>=0; i--) {
			if (mx>baseVec[i] && mx<baseVec[i]+cardW && cards.get(i).data>=0F) {
				setSelected(i);
				StateGame.get().setLocalHighlighted(cards.get(i).card, 1, false);
				return;
			}
		} selected=null; StateGame.get().setLocalHighlighted(null, 0, false); return;
	}
	@Override
	public void initFillOperation() {}
	@Override
	public void fillWith(Collection<Card> list, boolean flag) {
		for (Card card : list) {
			cards.add(new CardData(card).setData(flag?-1F:0F));
		}
		baseVec=calcVec(cards.size(), -1);
	}
	@Override
	public void add(Card card) {
		cards.add(retrieveIdxForCard(card), new CardData(card));
		baseVec=calcVec(cards.size(), -1);
	}
	@Override
	public void add(Card card, int idx) {
		cards.add(idx, new CardData(card));
		baseVec=calcVec(cards.size(), -1);
	}
	@Override
	public boolean remove(CardData card) {
		boolean r = removeIn(card);
		baseVec=calcVec(cards.size(), -1);
		return r;
	}
	@Override
	public void clear() {
		cards.clear();
		selected=null;
		baseVec=calcVec(0, -1);
	}
	@Override
	public void arrangeCard(Card card) {
		int idx = idxOf(card);
		if (idx<0) {throw new IllegalStateException("Cannot arrange a card that is not within this container!");}
		card.x=this.pos.x()+this.getStableVec()[idx]+scroll.get(); card.y=this.pos.y(); card.w=this.cardW; card.h=this.cardH; 
	}
	@Override
	public float[] calcVec(int s, int pushIdx) {
		if (s<=0) {return null;}
		float vec[] = new float[s];
		float p = 0F; // pointer
		for (int i=0; i<s; i++) {
			vec[i]=p;
			p+=cardW+nSpacing;
		}
		return vec;
	}
	@Override
	public float[] getStableVec() {
		return baseVec;
	}
	@Override
	public boolean accepts(Card card) {
		return true;
	}
	@Override
	public boolean isPushing() {return false;}
	
	public int getVisibleCount() {
		int i=0;
		for (CardData card : cards) {if (card.data>=0F) {i++;}}
		return i;
	}

}

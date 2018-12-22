package m00nl1ght.gwent.game.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import m00nl1ght.gwent.Main;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.card.Cards.CardBase;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.local.CardPickUp;
import m00nl1ght.gwent.game.local.DraggedCard;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceTask;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CardRow extends GuiElement implements CardContainer {
	
	protected ArrayList<CardData> cards = new ArrayList<CardData>();
	protected int points;
	protected float pushPrg = 0F;
	protected float nSpacing = 10F;
	public float cardW = 100F;
	public float cardH = 123F;
	protected float selOffset = 0F;
	protected float baseVec[], prgVec[], bckpVec[];
	protected CardData selected;
	protected boolean isPushing = false;
	protected CardLocation location;

	public CardRow(GuiContainer<? extends GuiElement> base, ElementPos position, CardLocation location) {
		super(base, position); this.location=location; location.base=this;
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (!visible) {return;}
		if (cards.size()<=0) {return;}
		for (int i=0; i<cards.size();i++) {
			CardData data = cards.get(i);
			if (data==selected || data.data<0) {continue;}
			data.card.draw(g, pos.x() + baseVec[i] + (pushPrg>0F ? (prgVec[i]-baseVec[i])*pushPrg : 0F), pos.y(), cardW, cardH, 1, StateGame.get().playerO().getHighlighted()==data.card ? 2 : -1);
		}
		if (selected!=null && selected.data>=0) {
			selected.data-=0.1F;
			int i = cards.indexOf(selected);
			if(selected.data<0F) {selected.data=0F;}
			selected.card.draw(g, pos.x() + baseVec[i] + (pushPrg>0F ? (prgVec[i]-baseVec[i])*pushPrg : 0F), pos.y() + selOffset, cardW, cardH, 1, 1);
		}
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clicks) {
		if (!visible) {return;}
		updateSel(x, y);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (!visible) {return;}
		updateSel(newx, newy);
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (!visible || !location.canInteract()) {return;}
		if (selected != null) {
			if (isPushing) {return;}
			Card card = selected.card; //because selection update else could mess it up without buffer
			DraggedCard d = new DraggedCard(StateGame.get(), StateGame.get().state(), this, new ElementPos(base, pos.x()+baseVec[cards.indexOf(selected)], pos.y()-20, 100F, 123F, 5), card, true);
			StateGame.get().addElement(d);
			StateGame.get().state().setShouldEndWhenEmpty(false);
			d.mouseDragged(oldx, oldy, newx, newy);
			base.setSelectedElement(d);
			new CardPickUp(StateGame.get().state(), -2, card, d, 122F, 150F, 10);
			new RemoveCardAnimation(StateGame.get().state(), -2, card, 0.1F);
			selected=null;
		}
	}
	
	void updateSel(int mx, int my) {
		mx-=pos.x();
		if (selected != null) {
			int idx=cards.indexOf(selected);
			if (idx>=0 && mx>baseVec[idx] && mx<baseVec[idx]+cardW) {return;}
		}
		for (int i=cards.size()-1; i>=0; i--) {
			if (mx>baseVec[i] && mx<baseVec[i]+cardW) {
				setSelected(i);
				StateGame.get().setLocalHighlighted(cards.get(i).card, 1, false);
				return;
			}
		} selected=null; StateGame.get().setLocalHighlighted(null, 0, false); return;
	}
	
	public void updateSelection(boolean ignoreCache) { //UNTESTED
		int[] m = Main.getMousePos();
		if (base.getSelectedElement()!=this) {selected=null; return;}
		if (ignoreCache) {this.selected=null;}
		updateSel(m[0], m[1]);
	}
	
	@Override
	public void pointsChanged() {
		points=0;
		for (CardData data : cards) {
			points+=data.card.health;
		}
	}
	
	public int getPoints() {return points;}
	
	// RAW CARDCONTAINER
	
	@Override
	public void add(Card card) {
		cards.add(retrieveIdxForCard(card), new CardData(card));
		card.updateLocation(this.location);
		pointsChanged();
		baseVec=calcVec(cards.size(), -1);
	}
	
	public void add(Card card, int idx) {
		cards.add(idx, new CardData(card));
		card.updateLocation(this.location);
		pointsChanged();
		baseVec=calcVec(cards.size(), -1);
	}
	
	public void set(int idx, Card card) {
		cards.set(idx, new CardData(card));
		card.x=this.getPos().x()+baseVec[idx]; card.y=this.getPos().y(); card.w=this.cardW; card.h=this.cardH;
		pointsChanged();
	}
	
	public void initFillOperation() {isPushing=true;}
	public void fillWith(Collection<Card> list, boolean flag) {
		for (Card card : list) {
			cards.add(new CardData(card).setData(flag?-1F:0F));
			card.updateLocation(this.location);
		}
		pointsChanged();
		baseVec=calcVec(cards.size(), -1); isPushing=false;
	}
	
	public Card get(int idx) {
		return cards.get(idx).card;
	}
	
	@Override
	public boolean remove(Card card) {
		for (CardData data : cards) {
			if (data.card==card) {
				return remove(data);
			}
		} return false;
	}

	protected boolean remove(CardData card) {
		boolean r = removeIn(card);
		baseVec=calcVec(cards.size(), -1);
		pointsChanged();
		return r;
	}
	
	protected boolean removeIn(CardData card) {
		return cards.remove(card);
	}

	@Override
	public void clear() {
		cards.clear();
		selected=null;
		pointsChanged();
		baseVec=calcVec(0, -1);
	}
	
	public void cleanupFlagged() {
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).data<0F) {cards.remove(i); i--;}
		}
	}
	
	public void unflagAll() {
		for (CardData card : cards) {if (card.data<0F) card.data=0F;}
	}
	
	public void sort() {
		Collections.sort(cards);
	}
	
	public CardData find(Card card) {
		for (CardData data : cards) {
			if (data.card==card) {return data;}
		} return null;
	}
	
	public int idxOf(Card card) {
		for (int idx=0; idx<cards.size(); idx++) {
			if (cards.get(idx).card==card) {return idx;}
		} return -1;
	}

	@Override
	public Card find(CardBase base) {
		for (CardData data : cards) {
			if (data.card.base==base) {return data.card;}
		} return null;
	}
	
	@Override
	public Card find(CardFilter... filter) {
		wloop: for (CardData data : cards) {
			for (CardFilter f : filter) {
				if (!f.accepts(data.card)) {continue wloop;}
			}
			return data.card;
		} return null;
	}
	

	@Override
	public void findAll(ArrayList<Card> dest, CardFilter... filters) {
		wloop: for (CardData data : cards) {
			for (CardFilter f : filters) {
				if (!f.accepts(data.card)) {continue wloop;}
			}
			dest.add(data.card);
		}
	}

	@Override
	public ArrayList<Card> getList() {
		ArrayList<Card> list = new ArrayList<Card>();
		for (CardData data : cards) {list.add(data.card);}
		return list;
	}
	
	public ArrayList<CardData> getDataList() {
		return cards;
	}

	@Override
	public int getSize() {
		return cards.size();
	} 
	
	public float getSpacing(int count, float width) {
		return (width-count*cardW)/(count-1);
	}
	
	public int retrieveIdxForCard(Card card) {
		int idx = 0;
		while (idx<cards.size() && card.compareTo(cards.get(idx).card)>0) {idx++;}
		return idx;
	}
	
	public float[] calcVec(int s, int pushIdx) {
		if (s<=0) {return null;}
		int st = s + (pushIdx>=0 ? 1 : 0);
		float vec[] = new float[s];
		float totalW=st*cardW+nSpacing*(st-1);
		float p = 0F, sp = 0F; // pointer
		if (totalW<=pos.w()) {
			p+=pos.w()/2-totalW/2;
			sp=nSpacing;
		} else {
			sp=getSpacing(st, pos.w());
		}
		for (int i=0; i<s; i++) {
			if (i==pushIdx) {p+=cardW+sp;}
			vec[i]=p;
			p+=cardW+sp;
		}
		return vec;
	}
	
	public int calcIdxFromMouseXY(int mx, int my) {
		if (!this.checkSelected(mx, my)) {return -1;}
		float[] myVec = getStableVec();
		int idx = 0; mx-=pos.x();
		while (idx<cards.size()) {
			if (mx<myVec[idx]+cardW/2) {break;}
			idx++;
		}
		return idx;
	}
	
	public float[] getStableVec() {
		return bckpVec!=null ? bckpVec : baseVec;
	}
	
	public void setSelected(int idx) {
		if (idx<0) {selected=null; return;}
		selected=cards.get(idx);
	}
	
	@Override
	public Card getSelected() {
		return selected.card;
	}
	
	@Override
	public void leaveFocus() {
		selected=null;
		StateGame.get().setLocalHighlighted(null, 0, false);
	}
	
	@Override
	public boolean accepts(Card card) {
		return location.acceptsCard(card);
	}
	
	@Override
	public void arrangeCard(Card card) {
		int idx = idxOf(card);
		if (idx<0) {throw new IllegalStateException("Cannot arrange a card that is not within this container!");}
		card.x=this.pos.x()+this.getStableVec()[idx]; card.y=this.pos.y(); card.w=this.cardW; card.h=this.cardH; 
	}
	
	public void setCardData(Card card, float f) {
		find(card).data=f;
	}
	
	public float requestCardPosition(int idx) {
		return calcVec(cards.size()+1, -1)[idx];
	}
	
	public void setProperties(float offset, float spacing, float cardW, float cardH) {
		this.selOffset=offset; this.cardW=cardW; this.cardH=cardH; this.nSpacing=spacing;
	}
	
	@Override
	public CardLocation getCardLocation() {return location;}
	public boolean isPushing() {return isPushing;}
	
	// ANIMATION FACTORY
	
	@Override
	public void catchCard(SequenceHandler seqctx, Card card) {
		new CardMoveToHand(seqctx, -1, this, card, 20, 0.1F);
	}
	
	protected class CardData implements Comparable<CardData> {
		protected CardData(Card card) {this.card=card;};
		public CardData setData(float flag) {data=flag; return this;}
		protected final Card card;
		protected float data;
		public int compareTo(CardData o) {return card.compareTo(o.card);}
	}
	
	public class AddCardAnimation extends SequenceTask {
		
		private Card card;
		private float speed;
		private int pushIdx = -1;

		public AddCardAnimation(SequenceHandler container, int priority, Card card, float speed) {
			super(container, priority); this.speed=speed; this.card=card;
		}
		
		@Override
		public boolean start() {
			if (isPushing()) {return false;}
			getPushIdx();
			pushPrg = 0F; isPushing=true;
			prgVec=calcVec(cards.size(), pushIdx);
			return true;
		}
		
		@Override
		public void update() {
			pushPrg+=speed;
			if (pushPrg>=1.0F) {pushPrg=1F; this.unregister();}
		}
		
		@Override
		public void completed() {
			prgVec=null;
			pushPrg=0F; isPushing=false;
			add(card, getPushIdx());
			updateSelection(false);
			this.unregister();
		}
		
		public float getPushPrg() {return pushPrg;}

		public int getPushIdx() {
			if (card==null) {throw new IllegalStateException("card cannot be null!");}
			if (pushIdx<0) {pushIdx=retrieveIdxForCard(card);}
			return pushIdx;
		}

		public void setPushIdx(int pushIdx) {this.pushIdx = pushIdx;}
		
		@Override
		public String toString() {
			return "RowAddCard {idx:"+pushIdx+" c:"+card+"}";
		}
		
	}
	
	public class RemoveCardAnimation extends SequenceTask {
		
		private Card card;
		private float speed;
		private int pushIdx;

		public RemoveCardAnimation(SequenceHandler container, int priority, Card card, float speed) {
			super(container, priority); this.speed=speed; this.card=card;
			if (card==null) throw new IllegalArgumentException("card is null!");
		}
		
		@Override
		public boolean start() {
			if (isPushing()) {return false;}
			CardData data = find(card);
			pushIdx = cards.indexOf(data);
			remove(data);
			if (selected==data) {selected=null;}
			pushPrg = 1F; isPushing=true;
			prgVec=calcVec(cards.size(), pushIdx);
			return true;
		}
		
		@Override
		public void update() {
			pushPrg-=speed;
			if (pushPrg<=0F) {pushPrg=0F; this.completed();}
		}
		
		@Override
		public void completed() {
			prgVec=null;
			pushPrg=0F; isPushing=false;
			updateSelection(false);
			this.unregister();
		}

		public float getPushPrg() {return pushPrg;}
		
		@Override
		public String toString() {
			return "RowRemoveCard {idx:"+pushIdx+" c:"+card+"}";
		}

	}

	public class PhantomCardAnimation extends SequenceTask {

		protected float speed;
		protected boolean end = false;
		public CardRow s;

		public PhantomCardAnimation(SequenceHandler container, int priority, float speed) {
			super(container, priority); this.speed=speed;
		}

		@Override
		public boolean start() {
			bckpVec=baseVec;
			isPushing=true;
			return true;
		}

		public void push(int idx) {
			if (pushPrg>0F) {
				if (pushPrg<1F) {
					for (int i = 0; i < cards.size(); i++) {baseVec[i] += (prgVec[i]-baseVec[i])*pushPrg;}
				} else {
					baseVec=prgVec;
				} pushPrg = 0F;
			} prgVec=calcVec(cards.size(), idx);
		}

		public void end() {
			if (pushPrg>0F) {
				if (pushPrg<1F) {
					for (int i = 0; i < cards.size(); i++) {baseVec[i] += (prgVec[i]-baseVec[i])*pushPrg;}
				} else {
					baseVec=prgVec;
				} pushPrg = 0F;
			} prgVec=calcVec(cards.size(), -1); end = true;
		}

		public void keep() {
			end=true;
		}

		@Override
		public void update() {
			if (!isPushing()) {return;}
			pushPrg+=speed;
			if (pushPrg>=1.0F) {pushPrg=1F; if (end) {completed();}}
		}

		@Override
		public void completed() {
			baseVec=prgVec;
			prgVec=null; bckpVec=null;
			pushPrg=0F; isPushing=false;
			updateSelection(false);
			this.unregister();
		}

		public float getPushPrg() {return pushPrg;}
		
		@Override
		public String toString() {
			return "RowAddCard {end:"+end+"}";
		}

	}

}

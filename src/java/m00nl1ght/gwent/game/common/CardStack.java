package m00nl1ght.gwent.game.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.card.Cards.CardBase;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardLocation.Location;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CardStack extends GuiElement implements CardContainer {
	
	protected CardLocation location;
	private Card iBase;
	protected float wscale, hscale;
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	public CardStack(GuiContainer<? extends GuiElement> base, ElementPos position, CardLocation location) {
		super(base, position); this.location=location; location.base=this;
		wscale = pos.w() / 518F; hscale = pos.h() / 724F;
	}
	
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (cards.size()>0) {
			getiBase().draw(g, pos.x(), pos.y(), pos.w(), pos.h(), 0, this==base.getSelectedElement() ? 1 : getiBase()==StateGame.get().playerO().getHighlighted() ? 2 : 0);
		}
	}
	
	@Override
	public void gainFocus() {
		if (cards.size()<=0) {return;}
		StateGame.get().setLocalHighlighted(getiBase(), 0, false);
	}
	
	@Override
	public void leaveFocus() {
		StateGame.get().setLocalHighlighted(null, 0, false);
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		base.elementCallback(this, 0, 0);
	}
	
	// RAW CARDCONTAINER
	
	protected void onAdd(Card card) {
		card.updateLocation(this.location);
		card.x=this.getPos().x(); card.y=this.getPos().y(); card.w=this.getPos().w(); card.h=this.getPos().h();
		if (location.loc==Location.GRAVEYARD) {
			card.reset();
		}
	}
	
	public void add(Card card) {
		cards.add(0, card);
		this.onAdd(card);
	}
	
	public void addBottom(Card card) {
		cards.add(card);
		this.onAdd(card);
	}
	
	public void addRandom(Card card) {
		cards.add(cards.size()>0 ? Loader.rand.nextInt(cards.size()) : 0, card);
		this.onAdd(card);
	}
	
	public void add(Collection<Card> cards) {
		this.cards.addAll(cards);
		for (Card card : cards) {this.onAdd(card);}
	}
	
	public boolean remove(Card card) {
		if (!cards.remove(card)) {return false;}
		if (cards.size()<=0) {StateGame.get().setLocalHighlighted(null, 0, false);}
		return true;
	}
	
	public Card get(int idx) {
		return cards.get(idx);
	}
	
	public Card get() {
		if (cards.size()<=0) {return null;}
		return cards.get(0);
	}
	
	@Override
	public Card getSelected() {
		return base.getSelectedElement()==this ? getiBase() : null;
	}
	
	public ArrayList<Card> get(int n, boolean sort) {
		if (n>cards.size()) {throw new IllegalArgumentException("Cannot draw "+n+" cards from stack when stack has only "+cards.size()+"!");}
		ArrayList<Card> drawn = new ArrayList<Card>();
		for (int i=0; i<n; i++) {
			drawn.add(cards.get(i));
		}
		if (sort) Collections.sort(drawn);
		return drawn;
	}
	
	public ArrayList<Card> get(int n, boolean sort, CardFilter... filters) {
		if (n>cards.size()) {throw new IllegalArgumentException("Cannot draw "+n+" cards from stack when stack has only "+cards.size()+"!");}
		ArrayList<Card> drawn = CardFilter.filter(cards, n, filters);
		if (sort) Collections.sort(drawn);
		return drawn;
	}
	
	public void clear() {
		for (Card card : cards) {card.updateLocation(null);}
		this.cards.clear();
	}
	
	public Card find(CardBase base) {
		Iterator<Card> it = cards.iterator();
		while (it.hasNext()) {
			Card c = it.next();
			if (c.base==base) {return c;}
		} return null;
	}
	
	@Override
	public Card find(CardFilter... filter) {
		Iterator<Card> it = cards.iterator();
		wloop:while (it.hasNext()) {
			Card c = it.next();
			for (CardFilter f : filter) {
				if (!f.accepts(c)) {continue wloop;}
			}
			return c;
		} return null;
	}
	
	@Override
	public void findAll(ArrayList<Card> dest, CardFilter... filters) {
		Iterator<Card> it = cards.iterator();
		wloop:while (it.hasNext()) {
			Card c = it.next();
			for (CardFilter f : filters) {
				if (!f.accepts(c)) {continue wloop;}
			}
			dest.add(c);
		}
	}
	
	public ArrayList<Card> getList() {
		return cards;
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public void initCards(int factionID, float rotate) {
		for (Card card : cards) {card.factionID=factionID; card.setRotation(rotate);}
	}
	
	@Override
	public void arrangeCard(Card card) {
		//if (!cards.contains(card)) {throw new IllegalStateException("Cannot arrange a card that is not within this container!");}
		card.x=this.pos.x(); card.y=this.pos.y(); card.w=this.pos.w(); card.h=this.pos.h(); 
	}
	
	// ANIMATION FACTORY
	
	public boolean draw(SequenceHandler seqctx, int priority, int n, Perspective<?> persp, boolean trim, boolean sort) {
		if (!(cards.size()>=n)) {if (trim) {n=cards.size();} else {return false;}}
		if (n<=0) {return false;}
		new CardFillRowFromStack(seqctx, priority, this, persp.hand(), get(n, sort), 35, 1);
		return true;
	}
	
	public boolean draw(SequenceHandler seqctx, int priority, Card card, Perspective<?> persp) {
		if (!cards.contains(card)) {return false;}
		new CardStackToHand(seqctx, priority, this, persp.hand(), card, persp.drawPos());
		return true;
	}
	
	public Card draw(SequenceHandler seqctx, int priority, Perspective<?> persp) {
		if (cards.size()<=0) {return null;}
		CardMoveTask ani;
		ani = new CardStackToHand(seqctx, priority, this, persp.hand(), null, persp.drawPos());
		return ani.getCard();
	}
	
	@Override
	public void catchCard(SequenceHandler seqctx, Card card) {
		// TODO create move animation later
	}

	@Override
	public boolean accepts(Card card) {return false;}

	@Override
	public CardLocation getCardLocation() {return location;}

	public Card getiBase() {
		return iBase;
	}

	public void setiBase(Card iBase) {
		this.iBase = iBase;
	}

}

package m00nl1ght.gwent.game.common;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.card.Cards.CardBase;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardLocation.Location;
import m00nl1ght.gwent.game.local.CardPickUp;
import m00nl1ght.gwent.game.local.DraggedCard;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class LeaderUI extends GuiElement implements CardContainer {
	
	protected Card card;
	protected CardLocation location;
	protected float sOffset = -10F;
	protected boolean used = false;
	protected float shift = 0F;

	public LeaderUI(GuiContainer<? extends GuiElement> base, ElementPos position, Perspective<?> persp, float sOffset) {
		super(base, position); this.sOffset=sOffset;
		location = new CardLocation(Location.LEADER, persp); location.base=this;
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (used) {
			card.drawIn(g, pos.x(), pos.y(), pos.w(), pos.h(), 1, this==base.getSelectedElement() ? 1 : -1, true);
			return;
		}
		if (this==base.getSelectedElement()) {
			if (location.persp.isLocal()) shift+=0.1F;
			if(shift>1F) {shift=1F;}
			card.draw(g, pos.x(), pos.y()+shift*sOffset, pos.w(), pos.h(), 1, 1);
		} else {
			shift-=0.1F;
			if(shift<0F) {shift=0F;}
			card.draw(g, pos.x(), pos.y()+shift*sOffset, pos.w(), pos.h(), 1, -1);
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (used || !location.canInteract()) {return;}
		used=true; shift = 0F;
		DraggedCard d = new DraggedCard(StateGame.get(), StateGame.get().state(), this, new ElementPos(base, pos.x(), pos.y()-20, 100F, 123F, 5), card, true);
		StateGame.get().addElement(d);
		StateGame.get().state().setShouldEndWhenEmpty(false);
		d.mouseDragged(oldx, oldy, newx, newy);
		base.setSelectedElement(d);
		new CardPickUp(StateGame.get().state(), -2, card, d, 122F, 150F, 10);
	}

	@Override
	public void add(Card card) {
		this.card=card; used=false;
		card.updateLocation(this.location);
	}

	@Override
	public void catchCard(SequenceHandler seqctx, Card card) {
		new CardMoveBase(seqctx, -1, pos.x(), pos.y(), pos.w(), pos.h(), this, card, 20);
	}

	@Override
	public boolean accepts(Card card) {return false;}

	@Override
	public boolean remove(Card card) {
		if (card!=this.card) {return false;}
		used=true; return true;
	}
	
	@Override
	public Card getSelected() {
		return base.getSelectedElement()==this ? card : null;
	}

	@Override
	public void clear() {}

	@Override
	public Card find(CardBase base) {
		return used ? null : card.base==base ? card : null;
	}
	
	@Override
	public Card find(CardFilter... filter) {
		if (used) {return null;}
		for (CardFilter f : filter) {
			if (!f.accepts(card)) {return null;}
		}
		return card;
	}
	
	@Override
	public void findAll(ArrayList<Card> dest, CardFilter... filters) {
		if (used) {return;}
		for (CardFilter f : filters) {
			if (!f.accepts(card)) {return;}
		}
		dest.add(card);
	}

	@Override
	public ArrayList<Card> getList() {return null;}

	@Override
	public int getSize() {return used ? 0 : 1;}

	@Override
	public CardLocation getCardLocation() {return location;}
	
	@Override
	public void gainFocus() {
		StateGame.get().setLocalHighlighted(card, used ? 0 : 1, used);
	}
	
	@Override
	public void leaveFocus() {
		StateGame.get().setLocalHighlighted(null, 0, false);
	}
	
	@Override
	public void arrangeCard(Card card) {
		//if (!cards.contains(card)) {throw new IllegalStateException("Cannot arrange a card that is not within this container!");}
		card.x=this.pos.x(); card.y=this.pos.y(); card.w=this.pos.w(); card.h=this.pos.h(); 
	}

	public Card get() {
		return card;
	}
	
	public void setUsed(boolean used) {
		this.used=used;
	}

}

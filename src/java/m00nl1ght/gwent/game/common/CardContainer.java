package m00nl1ght.gwent.game.common;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.card.Cards.CardBase;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public interface CardContainer {
	
	public void add(Card card);
	
	public void catchCard(SequenceHandler seqctx, Card card);
	
	public Card getSelected();
	
	public boolean accepts(Card card);
	
	public boolean remove(Card card);
	
	public void arrangeCard(Card card);
	
	public void clear();
	
	public Card find(CardBase base);
	
	public Card find(CardFilter... filter);
	
	public void findAll(ArrayList<Card> dest, CardFilter... filters);
	
	public ArrayList<Card> getList();
	
	public int getSize();
	
	public default void pointsChanged() {}
	
	public CardLocation getCardLocation();

}

package m00nl1ght.gwent.game;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;

public class Deck {
	
	protected int faction = 0;
	protected Card leader;
	protected ArrayList<Card> cards = new ArrayList<Card>();
	
	public int getFaction() {
		return faction;
	}
	
	public Card getLeader() {
		return leader;
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void setLeader(Card leader) {
		this.leader=leader;
		leader.factionID=faction;
	}
	
	public void setFaction(int faction) {
		this.faction=faction;
	}

}

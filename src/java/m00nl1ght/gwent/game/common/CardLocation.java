package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.Perspective;



public class CardLocation {
	
	public Location loc;
	public Perspective<?> persp;
	public CardContainer base;
	
	public CardLocation(Location location, Perspective<?> perspective) {
		this.loc=location; this.persp=perspective;
	}
	
	public CardLocation(Location location, Perspective<?> perspective, CardContainer base) {
		this(location, perspective); this.base=base;
	}

	public enum Location {
		NULL, DECK, HAND, LEADER, BOARD, GRAVEYARD;
	}

	public boolean canInteract() {
		if (!persp.isLocal()) {return false;}
		if (!persp.canInteract()) {return false;}
		if (loc==Location.HAND) {return true;}
		if (loc==Location.LEADER) {return true;}
		return false;
	}

	public boolean acceptsCard(Card card) {
		if (loc!=Location.BOARD) {return false;}
		switch (card.getLoyality()) {
		case DISLOYAL:
			return (card.getLocation().persp.opp()==persp);
		case LOYAL:
			return (card.getLocation().persp==persp);
		case VERSATILE:
			return true;
		default:
			return false;
		}
	}

}

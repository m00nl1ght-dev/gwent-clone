package m00nl1ght.gwent.card;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Cards.CardBase;
import m00nl1ght.gwent.card.Cards.Tier;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.common.CardLocation.Location;

public class CardFilter {
	
	public static final CardFilter ALL = new CardFilter();
	public static final CardFilter UNITS = new CardFilterUnits();
	public static final CardFilter SPECIAL = new CardFilterSpecial();
	public static final CardFilterBase BASE = new CardFilterBase();
	public static final CardFilter BRONZE = new CardFilterTier(Tier.BRONZE);
	public static final CardFilter SILVER = new CardFilterTier(Tier.SILVER);
	public static final CardFilter GOLD = new CardFilterTier(Tier.GOLD);
	public static final CardFilterLocation ENEMY = new CardFilterLocation(Location.BOARD, true);
	public static final CardFilterLocation ALLY = new CardFilterLocation(Location.BOARD, false);
	
	public static boolean filter(Card card, CardFilter[] filters) {
		if (filters==null) {return true;}
		for (CardFilter filter : filters) {
			if (!filter.accepts(card)) {return false;};
		} return true;
	}
	
	public static ArrayList<Card> filter(ArrayList<Card> in, int n, CardFilter... filters) {
		ArrayList<Card> out = new ArrayList<Card>();
		if (filters==null) {out.addAll(in); return out;}
		l1:for (Card card : in) {
			if (n>0 && out.size()>=n) {return out;}
			for (CardFilter filter : filters) {
				if (!filter.accepts(card)) {continue l1;};
			} out.add(card);
		} return out;
	}
	
	public static ArrayList<Card> filter(ArrayList<Card> in, CardFilter... filters) {
		return filter(in, 0, filters);
	}
	
	public boolean accepts(Card card) {
		return true;
	}
	
	public static class CardFilterUnits extends CardFilter {
		public boolean accepts(Card card) {return card.base.health>0;}
	}
	
	public static class CardFilterSpecial extends CardFilter {
		public boolean accepts(Card card) {return card.base.health==0;}
	}
	
	public static class CardFilterBase extends CardFilter {
		private CardBase base;
		public CardFilter create(CardBase base) {this.base=base; return this;}
		public boolean accepts(Card card) {return card.base==base;}
	}
	
	public static class CardFilterTier extends CardFilter {
		private Tier tier;
		public CardFilterTier(Tier tier) {this.tier=tier;}
		public boolean accepts(Card card) {return card.base.tier==tier;}
	}
	
	public static class CardFilterLocation extends CardFilter {
		private Perspective<?> p; private Location locT; boolean opp;
		public CardFilterLocation(Location lT, boolean opp) {this.locT=lT; this.opp=opp;}
		public CardFilter create(Perspective<?> p) {this.p=opp?p.opp():p; return this;}
		public boolean accepts(Card card) {return card.location.persp==p && card.location.loc==locT;}
	}

}

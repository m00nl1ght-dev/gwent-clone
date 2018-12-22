package m00nl1ght.gwent.card;

import java.util.ArrayList;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.game.common.LightningAttack;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Image;

public class Cards {
	
	public static CardBase _DECK0 = new DummyBase("Your deck", "", -2, 108, 132);
	public static CardBase _GRAVEYARD0 = new DummyBase("Your graveyard", "", -1, 108, 132);
	public static CardBase _DECK1 = new DummyBase("Opponents deck", "", -2, 108, 132);
	public static CardBase _GRAVEYARD1 = new DummyBase("Opponents graveyard", "", -1, 108, 132);
	
	public static CardBase TUIRSEACH_HUNTER = new CardTuirseachHunter("Tuirseach Hunter", 3); //damage
	public static CardBase UDALRYK = new CardUdalryk("Udalryk", 2); //spy
	public static CardBase CRACH_AN_CRAITE = new CardCrachAnCraite("Crach an Craite", 0); //leader, draw card
	public static CardBase SIGRDRIFA = new CardSigrdrifa("Sigrdrifa", 1); //healer
	
	public static enum Tag {LEADER, VIKING;}
	public static enum Loyality {LOYAL, DISLOYAL, VERSATILE;}
	public static enum Tier {
		BRONZE(0, 0), SILVER(1, 0), GOLD(2, 1), CHAMPION(3, 1);
		int ID, bannerID;
		Tier(int ID, int bannerID) {this.ID=ID; this.bannerID=bannerID;}
		public int getID() {return ID;}
		public int banner() {return bannerID;}
	}
	
	public static abstract class CardBase {
		
		protected String name;
		protected int factionID;
		protected int health; // 0 if special card
		protected Loyality loyality;
		protected Tier tier; // null if dummy card
		protected int row;
		protected ArrayList<Tag> tag = new ArrayList<Tag>();
		protected int textureID;
		
		protected float sdW = 100F, sdH = 123F; // size of small card image at pos 518|0 in card texture
		
		public CardBase(String name, int textureID, int health, Tier tier, Loyality loyality, int faction, int row) {
			this.name=name;
			this.textureID=textureID;
			this.tier=tier;
			this.health=health;
			this.loyality=loyality;
			this.factionID=faction;
			this.row=row;
		}
		
		public abstract void deploy(SequenceHandler seqctx, Card card);
		
		public String name() {return name;}
		
		public Image texture() {
			if (textureID==-1) return Loader.iGraveyard;
			if (textureID<-1) return null;
			return Loader.iCard[textureID];
		}
		
		public String getTags() {
			String s="";
			if (tag.isEmpty()) {return "";}
			for (Tag tag : tag) {
				s+=tag.name()+",";
			}
			return s.substring(0, s.length()-1);
		}
		
		public String getAbbility() {
			return "No ability.";
		}
		
	}
	
	public static class DummyBase extends CardBase {
		
		protected String descr;

		public DummyBase(String name, String descr, int textureID, float sdW, float sdH) {
			super(name , textureID, 0, null, null, 0, 0);
			this.sdW=sdW; this.sdH=sdH; this.descr=descr;
		}
		
		public String getTags() {
			return descr;
		}
		
		public String getAbbility() {
			return null;
		}
		
		public void deploy(SequenceHandler seqctx, Card card) {}
		
		public void setInfo(String name, String descr) {this.name=name; this.descr=descr;}
		
	}
	
	public static class CardTuirseachHunter extends CardBase {

		public CardTuirseachHunter(String name, int textureID) {
			super(name, textureID, 6, Tier.BRONZE, Loyality.LOYAL, 0, 1);
			this.tag.add(Tag.VIKING);
		}
		
		@Override
		public void deploy(SequenceHandler seqctx, Card card) {
			CardFilter[] filter = new CardFilter[] {CardFilter.UNITS, CardFilter.ENEMY.create(card.location.persp)};
			if (!card.location.persp.opp().hasTargetableCard(filter)) {return;}
			card.location.persp.targetAttack(seqctx, card, new LightningAttack(seqctx, -1, card, 5, false, 2), filter);
		}
		
		public String getAbbility() {
			return "Deal 5 damage.";
		}
		
	}
	
	public static class CardUdalryk extends CardBase {

		public CardUdalryk(String name, int textureID) {
			super(name, textureID, 13, Tier.SILVER, Loyality.DISLOYAL, 0, 3);
			this.tag.add(Tag.VIKING);
		}
		
		@Override
		public void deploy(SequenceHandler seqctx, Card card) {
			if (card.location.persp.opp().deck().getSize()<=0) {return;}
			card.location.persp.opp().drawCards(seqctx, card.location.persp.opp().deck().get());
		}
		
		public String getAbbility() {
			return "Draw a card.";
		}
		
	}
	
	public static class CardCrachAnCraite extends CardBase {

		public CardCrachAnCraite(String name, int textureID) {
			super(name, textureID, 5, Tier.GOLD, Loyality.LOYAL, 0, 5);
			this.tag.add(Tag.LEADER);
			this.tag.add(Tag.VIKING);
		}
		
		@Override
		public void deploy(SequenceHandler seqctx, Card card) {
			int c = card.location.persp.deck().getSize();
			if (c<=0) {return;}
			card.location.persp.chooseOne(seqctx, card.location.persp.deck().get(c>1?2:1, true));
		}
		
		public String getAbbility() {
			return "Draw 2 cards,\nkeep one of them.";
		}
		
	}
	
	public static class CardSigrdrifa extends CardBase {

		public CardSigrdrifa(String name, int textureID) {
			super(name, textureID, 3, Tier.SILVER, Loyality.LOYAL, 0, 5);
			this.tag.add(Tag.VIKING);
		}
		
		@Override
		public void deploy(SequenceHandler seqctx, Card card) {
			if(card.location.persp.graveyard().getSize()<=0) {return;}
			card.location.persp.resurrectOne(seqctx, card.location.persp.graveyard().get(0, true, CardFilter.BRONZE));
		}
		
		public String getAbbility() {
			return "Resurrect a Bronze unit.";
		}
		
	}

}

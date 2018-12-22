package m00nl1ght.gwent.game;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.card.Cards;
import m00nl1ght.gwent.game.common.CardContainer;
import m00nl1ght.gwent.game.common.CardLocation;
import m00nl1ght.gwent.game.common.CardRow;
import m00nl1ght.gwent.game.common.CardStack;
import m00nl1ght.gwent.game.common.GuiPerspectiveOverlay;
import m00nl1ght.gwent.game.common.ICardChooserUI;
import m00nl1ght.gwent.game.common.LeaderUI;
import m00nl1ght.gwent.game.common.CardLocation.Location;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.sequence.SequenceHandler;


public abstract class Perspective<P extends Player> {
	
	public final StateGame game;
	protected ICardChooserUI chooser;
	protected P player;
	protected CardStack graveyard, deck;
	protected CardRow hand;
	protected CardRow board[] = new CardRow[3];
	protected LeaderUI leader;
	protected GuiPerspectiveOverlay overlay;
	protected Perspective<?> opp;
	protected Card highlighted;
	protected ElementPos drawPos, pendingPos;
	protected int crown = 0;
	protected int[] roundPts;
	private boolean passed = false;
	
	public Perspective(StateGame game) {
		this.game=game;
	}
	
	public void setProperties(Perspective<?> opp, ElementPos drawPos, ElementPos pendingPos) {
		this.drawPos=drawPos; this.pendingPos=pendingPos; this.opp=opp;
		deck.setiBase(new Card(isLocal()?Cards._DECK0:Cards._DECK1)); deck.getiBase().setRotation(1F);
		deck.getiBase().updateLocation(new CardLocation(Location.DECK, this, deck));
		graveyard.setiBase(new Card(isLocal()?Cards._GRAVEYARD0:Cards._GRAVEYARD1));
		graveyard.getiBase().updateLocation(new CardLocation(Location.GRAVEYARD, this, graveyard));
	}
	
	public final void initGame(SequenceHandler seqctx, P player) {
		this.player=player;
		this.deck.getiBase().factionID=player.getDeck().getFaction();
		this.leader.add(player.getDeck().getLeader());
		this.deck.clear(); this.graveyard.clear(); this.hand.clear();
		this.board[0].clear(); this.board[1].clear(); this.board[2].clear();
		this.deck.add(player.getDeck().getCards()); 
		this.deck.shuffle(); this.deck.initCards(player.getDeck().getFaction(), isLocal() ? 0F : 1F);
		this.deck.draw(seqctx, -2, 5, this, true, true); //debug (5) org (10)
		this.startGame();
	}
	
	public void roundEnd(SequenceHandler seqctx, int round, boolean getCrown) {
		this.passed=false;
		if (getCrown) this.crown++;
		this.roundPts[round]=this.getPoints();
		for (CardRow row : board) {
			for (Card card : row.getList()) {
				card.roundEnd(seqctx);
			}
		}
	}
	
	public void startGame() {
		this.roundPts=new int[3];
		this.crown=0;
		this.passed=false;
		this.highlighted=null;
	}
	
	public void endGame() {
		
	}
	
	public abstract void placeAddCardOnBoard(SequenceHandler seqctx, Card card);
	public abstract void mulligan(SequenceHandler seqctx, int count);
	public abstract void targetAttack(SequenceHandler seqctx, Card card, CardCallback attack, CardFilter... filter);
	public abstract void drawCards(SequenceHandler seqctx, Card... cards);
	public abstract void resurrectOne(SequenceHandler seqctx, ArrayList<Card> cards);
	public abstract void chooseOne(SequenceHandler seqctx, ArrayList<Card> cards);
	public abstract void viewContainer(SequenceHandler seqctx, CardContainer container);
	
	public P player() {return player;}
	public abstract boolean isLocal();
	public final boolean onTurn() {return game.onTurn==this;}
	public Card getHighlighted() {return highlighted;}
	public void setHighlighted(Card card) {this.highlighted=card;}
	public int getCrown() {return crown;}
	public int getPoints() {return board[0].getPoints() + board[1].getPoints() + board[2].getPoints();}
	
	public CardRow hand() {return hand;}
	public CardStack graveyard() {return graveyard;}
	public CardStack deck() {return deck;}
	public LeaderUI leader() {return leader;}
	public Perspective<?> opp() {return opp;}
	public ElementPos drawPos() {return drawPos;}
	public ElementPos pendingPos() {return pendingPos;}
	public boolean passed() {return passed;}

	public boolean canInteract() {
		return game!=null && game.state()!=null && game.state().canInteract(this);
	}

	public void setOverlayState(int i) {
		overlay.alphaMode(i);
	}

	public boolean hasTargetableCard(CardFilter... filter) {
		return (board[0].find(filter)!=null || board[1].find(filter)!=null || board[2].find(filter)!=null);
	}
	
	public ArrayList<Card> getTargetableCards(CardFilter... filters) {
		ArrayList<Card> list = new ArrayList<Card>();
		board[0].findAll(list, filters); board[1].findAll(list, filters); board[2].findAll(list, filters);
		return list;
	}
	
	public void pass() {
		if (passed) {throw new IllegalStateException("Already passed!");}
		this.passed=true;
		this.overlay.setText("passed");
		this.setOverlayState(1);
		game.state().setShouldEndWhenEmpty(true);
	}

	public abstract void onTurnStart();
	public abstract void onTurnEnd();
	public abstract void tick();
	
	public ICardChooserUI chooser() {return chooser;}

	public void setOverlayAlpha(float a1) {
		overlay.setAlpha(a1);
		if (a1<=0F) {overlay.setText("");}
	}
	
}

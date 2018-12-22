package m00nl1ght.gwent.game;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.game.common.CardChooserFromHand;
import m00nl1ght.gwent.game.common.CardChooserFromStack;
import m00nl1ght.gwent.game.common.CardContainer;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.gwent.game.opponent.OppChooserOverlay;
import m00nl1ght.gwent.game.opponent.OppPendingCard;
import m00nl1ght.gwent.game.opponent.OppTargetAttack;
import m00nl1ght.gwent.game.opponent.OppChooserOverlay.OppDraw1ofXCallback;
import m00nl1ght.gwent.game.opponent.OppChooserOverlay.OppMulliganCallback;
import m00nl1ght.gwent.game.opponent.OppChooserOverlay.OppResurrectCallback;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class PerspectiveAI extends Perspective<AIPlayer> {

	public PerspectiveAI(StateGame game) {
		super(game);
		chooser = new OppChooserOverlay(game, game.oppChooser(), this);
	}
	
	@Override
	public void startGame() {
		this.player.newGame(this);
		super.startGame();
	}

	@Override
	public void endGame() {
		this.player.gameFinished();
		super.endGame();
	}
	
	@Override
	public void onTurnStart() {
		if (passed()) {game.state().setShouldEndWhenEmpty(true); return;}
		this.player.onTurnStart();
	}

	@Override
	public void onTurnEnd() {
		this.player.onTurnEnd();
	}

	@Override
	public void tick() {
		this.player.gameTick(onTurn() && !passed());
	}
	
	@Override
	public void placeAddCardOnBoard(SequenceHandler seqctx, Card card) {
		OppPendingCard p = new OppPendingCard(StateGame.get(), pendingPos().copy(), seqctx, card);
		player.planned.apply(p);
		StateGame.get().addElement(p);
	}
	
	@Override
	public void mulligan(SequenceHandler seqctx, int count) {
		OppMulliganCallback cb = new OppMulliganCallback(seqctx, this, count).init();
		chooser.setCallback(cb);
		new CardChooserFromHand(seqctx, -1, this, chooser, (CardFilter[])null);
		player.mulligan(cb);
	}
	
	@Override
	public void targetAttack(SequenceHandler seqctx, Card card, CardCallback attack, CardFilter... filter) {
		player.planned.apply(new OppTargetAttack(seqctx, -2, card, attack, this, filter));
	}
	
	@Override
	public void drawCards(SequenceHandler seqctx, Card... cards) {
		for (Card card : cards) {
			deck.draw(seqctx, -1, card, this);
		}
	}
	
	@Override
	public void resurrectOne(SequenceHandler seqctx, ArrayList<Card> cards) {
		CardCallback cb = new OppResurrectCallback(seqctx, this).init();
		chooser.setCallback(cb);
		new CardChooserFromStack(seqctx, -1, graveyard, cards, chooser);
		player.planned.apply(cb);
	}
	
	@Override
	public void chooseOne(SequenceHandler seqctx, ArrayList<Card> cards) {
		CardCallback cb = new OppDraw1ofXCallback(seqctx, this, cards).init();
		chooser.setCallback(cb);
		new CardChooserFromStack(seqctx, -1, deck, cards, chooser);
		player.planned.apply(cb);
	}
	
	@Override
	public void viewContainer(SequenceHandler seqctx, CardContainer container) {} //TODO add?: opp searches own/opp graveyards

	@Override
	public boolean isLocal() {return false;}

}

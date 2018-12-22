package m00nl1ght.gwent.game;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.game.common.CardChooserFromHand;
import m00nl1ght.gwent.game.common.CardChooserFromStack;
import m00nl1ght.gwent.game.common.CardContainer;
import m00nl1ght.gwent.game.common.CardStack;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.gwent.game.local.LocalCardChooser;
import m00nl1ght.gwent.game.local.PendingCard;
import m00nl1ght.gwent.game.local.TargetAttack;
import m00nl1ght.gwent.game.local.LocalCardChooser.Draw1ofXCallback;
import m00nl1ght.gwent.game.local.LocalCardChooser.MulliganCallback;
import m00nl1ght.gwent.game.local.LocalCardChooser.ResurrectCallback;
import m00nl1ght.gwent.game.local.LocalCardChooser.ViewCallback;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.sequence.SequenceHandler;



public class PerspectiveLocal extends Perspective<LocalPlayer> {
	
	public PerspectiveLocal(StateGame game) {
		super(game);
	}
	
	@Override
	public void setProperties(Perspective<?> opp, ElementPos drawPos, ElementPos pendingPos) {
		chooser = new LocalCardChooser(game, this);
		super.setProperties(opp, drawPos, pendingPos);
	}

	@Override
	public void startGame() {
		player.newGame(this);
		super.startGame();
	}

	@Override
	public void endGame() {
		player.gameFinished();
		super.endGame();
	}

	@Override
	public void onTurnStart() {
		if (passed()) {game.state().setShouldEndWhenEmpty(true); return;}
		player.onTurnStart();
	}

	@Override
	public void onTurnEnd() {
		
	}

	@Override
	public void tick() {
		
	}
	

	@Override
	public void placeAddCardOnBoard(SequenceHandler seqctx, Card card) {
		PendingCard p = new PendingCard(StateGame.get(), seqctx, pendingPos().copy(), card);
		seqctx.setShouldEndWhenEmpty(false);
		StateGame.get().addElement(p);
	}
	
	@Override
	public void mulligan(SequenceHandler seqctx, int count) {
		chooser.setCallback(new MulliganCallback(seqctx, this, count).init());
		new CardChooserFromHand(seqctx, -1, this, chooser, (CardFilter[])null);
	}
	
	@Override
	public void targetAttack(SequenceHandler seqctx, Card card, CardCallback attack, CardFilter... filter) {
		new TargetAttack(seqctx, -2, card, attack, this, filter);
	}
	
	@Override
	public void drawCards(SequenceHandler seqctx, Card... cards) {
		for (Card card : cards) {
			deck.draw(seqctx, -1, card, this);
		}
	}
	
	@Override
	public void resurrectOne(SequenceHandler seqctx, ArrayList<Card> cards) {
		chooser.setCallback(new ResurrectCallback(seqctx, this).init());
		new CardChooserFromStack(seqctx, -1, graveyard, cards, chooser);
	}
	
	@Override
	public void chooseOne(SequenceHandler seqctx, ArrayList<Card> cards) {
		chooser.setCallback(new Draw1ofXCallback(seqctx, this).init());
		new CardChooserFromStack(seqctx, -1, deck, cards, chooser);
	}
	
	@Override
	public void viewContainer(SequenceHandler seqctx, CardContainer container) {
		if (container instanceof CardStack) {
			chooser.setCallback(new ViewCallback(seqctx, this, (CardStack)container).init());
			new CardChooserFromStack(seqctx, -1, (CardStack)container, null, chooser);
		}
	}

	@Override
	public boolean isLocal() {return true;}

}

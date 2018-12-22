package m00nl1ght.gwent.game.campaign;

import java.util.ArrayList;
import java.util.Collections;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.card.Cards;
import m00nl1ght.gwent.game.AIPlayer;
import m00nl1ght.gwent.game.Deck;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.gwent.game.opponent.OppPendingCard;
import m00nl1ght.gwent.game.opponent.OppTargetAttack;
import m00nl1ght.gwent.game.opponent.OppChooserOverlay.OppDraw1ofXCallback;
import m00nl1ght.gwent.game.opponent.OppChooserOverlay.OppResurrectCallback;
import m00nl1ght.voidUI.sequence.Interactable;

public class TestAI extends AIPlayer {
	
	@Override
	public void init(Deck deck) {
		super.init(deck);
		new ActionPass(25, 25);
		new ActionTuirseachHunter();
		new ActionUdalryk();
		new ActionSigrdrifa();
		new ActionLeaderCrachAnCraite();
	}
	
	protected class ActionTuirseachHunter extends ActionBaseCard {

		ActionTuirseachHunter() {super(Cards.TUIRSEACH_HUNTER);}
		private Card chachedTarget;
		private ArrayList<Card> possible;

		protected void apply(Interactable task) {
			if (task instanceof OppTargetAttack) {
				new InteractionTargetAttack((OppTargetAttack) task, chachedTarget, possible);
				chachedTarget=null;
			} else {
				throw new AIException("This action instance cannot provide an interaction for task: "+task);
			}
		}

		protected float viability() {
			possible = persp.opp().getTargetableCards(CardFilter.UNITS, CardFilter.ENEMY.create(persp));
			if (!possible.isEmpty()) {
				Collections.shuffle(possible); //sort by health instead... ?
				chachedTarget=possible.get(0);
				return 0.6F;
			} else {return 0.1F;}
		}

	}
	
	protected class ActionUdalryk extends ActionBaseCard {

		ActionUdalryk() {super(Cards.UDALRYK);}

		protected void apply(Interactable task) {
			throw new AIException("This action instance cannot provide an interaction for task: "+task);
		}

		protected float viability() {
			// TODO if player has fewer points -> spy ...
			if (persp.deck().getSize()>0) {return 0.5F;} else {return 0.25F;}
		}

	}
	
	protected class ActionSigrdrifa extends ActionBaseCard {

		ActionSigrdrifa() {super(Cards.SIGRDRIFA);}
		private Card chachedTarget;
		private ArrayList<Card> possible = new ArrayList<Card>();
		private Action actionForTargetCard;
		
		protected void apply(Interactable task) {
			if (task instanceof OppResurrectCallback) {
				new InteractionChooseCard((CardCallback) task, chachedTarget, possible);
				chachedTarget=null;
			} else if (task instanceof OppPendingCard) {
				actionForPendingCard((OppPendingCard) task, actionForTargetCard);
			} else {
				throw new AIException("This action instance cannot provide an interaction for task: "+task);
			}
		}

		protected float viability() {
			// TODO more inteligent logic (viable cards in graveyard? ...)
			possible.clear();
			persp.graveyard().findAll(possible, CardFilter.BRONZE);
			if (!possible.isEmpty()) {
				Collections.shuffle(possible); //sort by viability instead... ?
				chachedTarget=possible.get(0);
				//actionForTargetCard=... TODO pre-generate
				return 0.7F;
			} else {return 0.05F;}
		}

	}
	
	protected class ActionLeaderCrachAnCraite extends ActionLeader {

		ActionLeaderCrachAnCraite() {super(Cards.CRACH_AN_CRAITE);}

		protected void apply(Interactable task) {
			if (task instanceof OppDraw1ofXCallback) {
				OppDraw1ofXCallback cb = (OppDraw1ofXCallback) task;
				Card target=cb.getCards().get(0); //TODO logic, just for testing
				new InteractionChooseCard(cb, target, cb.getCards());
			} else {
				throw new AIException("This action instance cannot provide an interaction for task: "+task);
			}
		}

		protected float viability() {
			if (persp.deck().getSize()>0) {return 0.5F;} else {return 0.1F;}
		}

	}

}

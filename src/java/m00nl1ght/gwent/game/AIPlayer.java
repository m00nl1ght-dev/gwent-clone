package m00nl1ght.gwent.game;

import java.util.ArrayList;
import java.util.Collections;

import m00nl1ght.gwent.Main;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.card.Cards.CardBase;
import m00nl1ght.gwent.game.common.CardRow;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.gwent.game.opponent.OppCardDeploy;
import m00nl1ght.gwent.game.opponent.OppPendingCard;
import m00nl1ght.gwent.game.opponent.OppTargetAttack;
import m00nl1ght.gwent.game.opponent.OppChooserOverlay.OppMulliganCallback;
import m00nl1ght.voidUI.sequence.Interactable;
import m00nl1ght.voidUI.sequence.InteractionTask;


public abstract class AIPlayer extends Player {
	
	protected PerspectiveAI persp; // current game perspective
	protected Action planned;
	protected ArrayList<Action> actions = new ArrayList<Action>();
	protected int turnTicks=0, randTicks=0;

	public void init(Deck deck) {
		this.deckBase=deck; actions.clear(); planned=null;
		// create actions here in subclasses
	}
	
	public void newGame(PerspectiveAI gamePersp) {
		if (this.persp!=null) {throw new IllegalStateException("This player is already in a game!");}
		this.persp=gamePersp;
	}
	
	public void gameFinished() {
		validate();
		this.persp=null;
	}
	
	public void gameTick(boolean onTurn) {
		validate();
		if (onTurn) {
			if (turnTicks>=0) {turnTicks++;} else {turnTicks--;}
			planned.turnTick(turnTicks);
		} else {
			if (!persp.passed()) {
				if (randTicks<=0) {
					persp.setHighlighted(this.highlightRandomOffTurn());
					randTicks=300+Main.rand.nextInt(400);
				} else {randTicks--;}
			}
		}
	}
	
	protected void playCard(Card card, OppPendingCard fromPending) {
		CardRow row = null; int idx = 0;
		switch (card.getLoyality()) {
		case DISLOYAL:
			row = persp.opp.board[Main.rand.nextInt(3)];
			idx = row.getSize()==0?0:Main.rand.nextInt(row.getSize()+1);
			break;
		case LOYAL:
			row = persp.board[Main.rand.nextInt(3)];
			idx = row.getSize()==0?0:Main.rand.nextInt(row.getSize()+1);
			break;
		case VERSATILE:
			throw new AIException("AI does not support cards with versatile loyality yet.");
		}
		persp.setHighlighted(card);
		if (card==persp.leader.get()) {
			new OppCardDeploy(persp.game.state(), -1, row, card, idx, null).tryStart();
			persp.leader.setUsed(true);
		} else {
			new OppCardDeploy(persp.game.state(), -1, row, card, idx, fromPending);
			if (fromPending==null) {persp.hand.new RemoveCardAnimation(persp.game.state(), -2, card, 0.1F);}
		}
		turnTicks=-1;
		persp.game.state().setShouldEndWhenEmpty(true);
	}
	
	protected void actionForPendingCard(OppPendingCard task, Action action) {
		if (action==null) { // if not given yet, find it
			for (Action act : actions) {
				act.calcViabilityOfCard(task.card);
			}
			Collections.sort(actions);
			action = actions.get(0);
			if (action.viability<=0F) {throw new AIException("No action (pend.) with viability > 0 found!");}
		}
		this.planned=action;
		new InteractionPendingCard(task);
	}
	
	protected Card highlightRandom(boolean allowLeader) {
		if (allowLeader && persp.leader.getSize()>0 && Main.rand.nextInt(6)==0) {return deckBase.leader;}
		if (persp.hand.getSize()==0) {return null;}
		return persp.hand.get(Main.rand.nextInt(persp.hand.getSize()));
	}
	
	protected Card highlightRandomOffTurn() {
		if (Main.rand.nextInt(5)==0) {return persp.graveyard.getiBase();}
		if (Main.rand.nextInt(4)==0) {return persp.deck.getiBase();}
		if (persp.leader.getSize()>0 && Main.rand.nextInt(5)==0) {return persp.leader.get();}
		if (persp.hand.getSize()<=0 || Main.rand.nextInt(4)==0) {return null;}
		return persp.hand.get(Main.rand.nextInt(persp.hand.getSize()));
	}
	
	protected Card bestMulligan() {
		Card ret = persp.chooser.row().find(CardFilter.BRONZE);
		if (ret==null) {ret=persp.chooser.row().find(CardFilter.SILVER);}
		if (ret==null) {ret=persp.chooser.row().find();}
		return ret;
	}
	
	protected void pass() {
		persp.pass();
	}
	
	public void onTurnStart() {
		validate();
		turnTicks=0;
		for (Action act : actions) {
			act.calcViabilityOfHand();
		}
		Collections.sort(actions);
		planned = actions.get(0);
		if (planned.viability<=0F) {throw new AIException("No action with viability > 0 found!");}
		//if (actions.get(1).viability>0F && Main.rand.nextFloat()>best.viability && Main.rand.nextFloat()<actions.get(1).viability) {best = actions.get(1);}
		System.out.println("[AI] Planned action "+planned+" with viability "+planned.viability+".");
	}
	
	public void onTurnEnd() {
		validate();
		planned=null;
	}
	
	public void mulligan(OppMulliganCallback cb) {
		new InteractionMulligan(cb);
	}
	
	private void validate() {
		if (this.persp==null) {throw new IllegalStateException("This player is not in a game!");}
	}
	
	protected class InteractionMulligan extends InteractionTask<OppMulliganCallback> {

		protected int ticks = 0;
		
		public InteractionMulligan(OppMulliganCallback task) {
			super(task, "OppMulligan");
		}

		public void update() {
			if (ticks>=100) {
				Card tar = bestMulligan();
				if (tar==null) {target.cancel(); this.completed();}
				else {
					persp.setHighlighted(tar);
					target.result(tar);
					if (target.count()>0) {
						ticks=0;
					} else {
						this.completed();
					}
				}
			} else if (ticks>=80) {
				if (ticks==80) persp.setHighlighted(bestMulligan());
			} else if (ticks>0 && ticks % 50 == 0 && Main.rand.nextBoolean()) {
				persp.setHighlighted(persp.chooser.row().get(Main.rand.nextInt(persp.chooser.row().getSize())));
			}
			ticks++;
		}
		
	}
	
	protected class InteractionTargetAttack extends InteractionTask<OppTargetAttack> {

		protected final Card tarc;
		protected final ArrayList<Card> possible;
		protected int ticks = 0;
		
		public InteractionTargetAttack(OppTargetAttack task, Card tarc, ArrayList<Card> possible) {
			super(task, "OppTargetAttack"); this.tarc=tarc; this.possible=possible;
		}

		@Override
		public void update() {
			if (ticks>=100) {
				persp.setHighlighted(tarc);
				if (!target.proc()) {throw new AIException("Failed to proc OppTargetAttack on "+tarc);}
				this.completed();
			} else if (ticks>=80) {
				if (ticks==80) persp.setHighlighted(tarc);
			} else if (ticks>10 && ticks % 15 == 0 && Main.rand.nextBoolean()) {
				if (possible.size()<2) {persp.setHighlighted(tarc);} else
				persp.setHighlighted(possible.get(Main.rand.nextInt(possible.size())));
			}
			ticks++;
		}
		
	}
	
	protected class InteractionChooseCard extends InteractionTask<CardCallback> {

		protected final Card tarc;
		protected final ArrayList<Card> possible;
		protected int ticks = 0;
		
		public InteractionChooseCard(CardCallback task, Card tarc, ArrayList<Card> possible) {
			super(task, "OppChooseAttack"); this.tarc=tarc; this.possible=possible;
		}

		public void update() {
			if (ticks>=250) {
				persp.setHighlighted(tarc);
				target.result(tarc);
				this.completed();
			} else if (ticks>=200) {
				if (ticks==200) persp.setHighlighted(tarc);
			} else if (ticks>25 && ticks % 50 == 0 && Main.rand.nextBoolean()) {
				if (possible.size()<2) {persp.setHighlighted(tarc);} else
				persp.setHighlighted(possible.get(Main.rand.nextInt(possible.size())));
			}
			ticks++;
		}
		
	}
	
	protected class InteractionPendingCard extends InteractionTask<OppPendingCard> {

		protected int ticks = 0;
		
		public InteractionPendingCard(OppPendingCard task) {
			super(task, "OppPendingCard");
		}

		public void update() {
			if (ticks>100) {
				playCard(target.card, target);
				this.completed();
			} else if (ticks==50) {
				persp.setHighlighted(target.card);
			}
			ticks++;
		}
		
	}
	
	protected abstract class Action implements Comparable<Action> {
		
		float viability = 0F;
		
		protected Action() {
			actions.add(this);
		}
		
		protected abstract void turnTick(int turnTicks);
		protected abstract void calcViabilityOfHand();
		protected abstract void calcViabilityOfCard(Card card);
		protected abstract void apply(Interactable task);
		
		public int compareTo(Action arg) {
			return -Float.compare(viability, arg.viability);
		}
		
	}
	
	protected abstract class ActionBaseCard extends Action {

		protected final CardBase base;
		protected Card instance;
		protected ActionBaseCard(CardBase base) {super(); this.base=base;}
		
		protected void turnTick(int ticks) {
			if (instance!=null && ticks>=0) {
				if (turnTicks>300 && persp.canInteract()) {
					playCard(instance, null); instance=null;
				} else if (turnTicks>=200) {
					if (turnTicks==200) persp.setHighlighted(instance);
				} else if (turnTicks % 50 == 0 && Main.rand.nextBoolean()) {
					persp.setHighlighted(highlightRandom(true));
				}
			}
		}

		protected void calcViabilityOfHand() {
			instance = persp.hand.find(base);
			if (instance!=null) {viability=viability();} else {viability=-1F;}
		}
		
		protected void calcViabilityOfCard(Card card) {
			if (card==null || card.base==base) {viability=viability();} else {viability=-1F;}
		}
		
		protected abstract float viability();
		
	}
	
	protected abstract class ActionLeader extends Action {

		protected final CardBase base;
		protected ActionLeader(CardBase base) {super(); this.base=base;}
		
		protected void turnTick(int ticks) {
			if (ticks>=0) {
				if (turnTicks>300 && persp.canInteract()) {
					playCard(deckBase.leader, null);
				} else if (turnTicks>=200) {
					if (turnTicks==200) persp.setHighlighted(persp.leader.get());
				} else if (turnTicks % 50 == 0 && Main.rand.nextBoolean()) {
					persp.setHighlighted(highlightRandom(true));
				}
			}
		}

		protected void calcViabilityOfHand() {
			if (persp.leader.find(base)!=null) {viability=viability();} else {viability=-1F;}
		}
		
		protected void calcViabilityOfCard(Card card) {
			if (card==null || card.base==base) {viability=viability();} else {viability=-1F;}
		}
		
		protected abstract float viability();
		
	}
	
	protected class ActionPass extends Action {
		
		protected int pointDiffWin, pointDiffDefeat;

		public ActionPass(int pointDiffWin, int pointDiffDefeat) {
			super();
			this.pointDiffWin=pointDiffWin; this.pointDiffDefeat=pointDiffDefeat;
		}
		
		protected void turnTick(int ticks) {
			if (ticks>=0) {
				if (turnTicks>150 && persp.canInteract()) {
					pass();
				} else if (turnTicks % 40 == 0 && Main.rand.nextBoolean()) {
					persp.setHighlighted(highlightRandom(true));
				}
			}
		}

		protected void calcViabilityOfHand() {
			if (persp.hand.getSize()+persp.leader.getSize()<=0) {viability=2F; return;}
			if (persp.getPoints()>persp.opp.getPoints()) {
				if (persp.opp.passed()) {viability=2F; return;}
				if (persp.opp.getCrown()==0 && pointDiffWin>0 && persp.getPoints()-persp.opp.getPoints()>=pointDiffWin) {
					viability=2F; return;
				}
			} else {
				if (persp.opp.getCrown()==0 && pointDiffDefeat>0 && persp.opp.getPoints()-persp.getPoints()>=pointDiffDefeat) {
					viability=2F; return;
				}
			}
			viability=0F; return;
		}
		
		protected void calcViabilityOfCard(Card card) {
			viability=-1F;
		}

		protected void apply(Interactable task) {
			throw new AIException("This action instance cannot provide an interaction for task: "+task);
		}
		
	}
	
	protected class AIException extends RuntimeException {
		public AIException(String description) {super(description);}
		private static final long serialVersionUID = -5169439113299251722L;
		
	}

}

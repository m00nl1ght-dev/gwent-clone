package m00nl1ght.gwent.game;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.Main;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.Cards;
import m00nl1ght.gwent.game.common.CardLocation;
import m00nl1ght.gwent.game.common.CardRow;
import m00nl1ght.gwent.game.common.CardRowLayered;
import m00nl1ght.gwent.game.common.CardStack;
import m00nl1ght.gwent.game.common.GameIntro;
import m00nl1ght.gwent.game.common.GuiPerspectiveOverlay;
import m00nl1ght.gwent.game.common.LeaderUI;
import m00nl1ght.gwent.game.common.RoundEndTask;
import m00nl1ght.gwent.game.common.CardLocation.Location;
import m00nl1ght.gwent.game.local.CardPrewiewTask;
import m00nl1ght.gwent.gui.GuiButtonAnimated;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiScreen;
import m00nl1ght.voidUI.sequence.AdvancedSeqHandler;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceQueue;
import m00nl1ght.voidUI.sequence.SequenceState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class StateGame extends GuiScreen {
	
	protected static StateGame instance;
	protected SequenceQueue seqbase = new SequenceQueue();
	public Card debugCard = new Card(Cards.TUIRSEACH_HUNTER);
	
	protected int round;
	
	float tx = 500, ty = 200, tw = 80, th = 80; // <- DEBUG
	protected ElementPos boardRect = new ElementPos(pos, 505, 157, 912, 766, 0);
	protected ElementPos oppChooser = new ElementPos(pos, 0, 0, 1920, 450, 3);
	protected Perspective<LocalPlayer> playerL = new PerspectiveLocal(this);
	protected Perspective<AIPlayer> playerO = new PerspectiveAI(this);
	protected Perspective<?> onTurn = null;
	private CardPrewiewTask currentPrewiew;
	protected GuiButtonAnimated passBtn;
	public boolean cardPrewiew = true, update=true;
	public static String debug = "m00nl1ght.gwent - gwent beta clone test";

	public StateGame(int id, GameContainer gc) {
		super(id, gc);
		if (instance!=null) {throw new IllegalStateException("GuiState 'StateGame' is already constructed!");} instance=this;
	}
	
	@Override
	public void init(GameContainer arg0, GuiBasedGame arg1) throws SlickException {
		super.init(arg0, arg1);
		addElement(playerL.leader = new LeaderUI(this, new ElementPos(this, 529, 945, 100, 123, 1), playerL, -10F));
		addElement(playerO.leader = new LeaderUI(this, new ElementPos(this, 529, 12, 100, 123, 1), playerO, 10F));
		addElement(playerL.graveyard = new CardStack(this, new ElementPos(this, 1583, 948, 100, 123, 1), new CardLocation(Location.GRAVEYARD, playerL)));
		addElement(playerO.graveyard = new CardStack(this, new ElementPos(this, 1586, 9, 92, 113, 1), new CardLocation(Location.GRAVEYARD, playerO)));
		addElement(playerL.deck = new CardStack(this, new ElementPos(this, 1711, 948, 100, 123, 1), new CardLocation(Location.DECK, playerL)));
		addElement(playerO.deck = new CardStack(this, new ElementPos(this, 1713, 9, 92, 113, 1), new CardLocation(Location.DECK, playerO)));
		addElement(playerL.board[0] = new CardRow(this, new ElementPos(this, 540, 560, 842, 110, 1), new CardLocation(Location.BOARD, playerL)));
		addElement(playerL.board[1] = new CardRow(this, new ElementPos(this, 540, 677, 842, 110, 1), new CardLocation(Location.BOARD, playerL)));
		addElement(playerL.board[2] = new CardRow(this, new ElementPos(this, 540, 794, 842, 110, 1), new CardLocation(Location.BOARD, playerL)));
		playerL.board[0].setProperties(0F, 5F, 90, 110); playerL.board[1].setProperties(0F, 5F, 90, 110); playerL.board[2].setProperties(0F, 5F, 90, 110);
		addElement(playerO.board[0] = new CardRow(this, new ElementPos(this, 540, 411, 842, 110, 1), new CardLocation(Location.BOARD, playerO)));
		addElement(playerO.board[1] = new CardRow(this, new ElementPos(this, 540, 294, 842, 110, 1), new CardLocation(Location.BOARD, playerO)));
		addElement(playerO.board[2] = new CardRow(this, new ElementPos(this, 540, 177, 842, 110, 1), new CardLocation(Location.BOARD, playerO)));
		playerO.board[0].setProperties(0F, 5F, 90, 110); playerO.board[1].setProperties(0F, 5F, 90, 110); playerO.board[2].setProperties(0F, 5F, 90, 110);
		addElement(playerL.hand = new CardRowLayered(this, new ElementPos(this, 650, 945, 785, 123, 1), new CardLocation(Location.HAND, playerL)));
		addElement(playerO.hand = new CardRow(this, new ElementPos(this, 650, 12, 785, 123, 1), new CardLocation(Location.HAND, playerO)));
		playerL.hand.setProperties(-10F, 10F, 100, 123); playerO.hand.setProperties(0F, 10F, 100, 123);
		addElement(playerL.overlay = new GuiPerspectiveOverlay(this, new ElementPos(this, 0, Loader.screenH-180, 1920, 180, 3), false));
		addElement(playerO.overlay = new GuiPerspectiveOverlay(this, new ElementPos(this, 0, 0, 1920, 180, 3), true));
		playerL.setProperties(playerO, new ElementPos(this, Loader.screenW/2-61, Loader.screenH-360, 122, 150, 2), new ElementPos(this, Loader.screenW/2-50, 945, 100, 123, 4)); 
		playerO.setProperties(playerL, new ElementPos(this, Loader.screenW/2-61, 217, 122, 150, 2), new ElementPos(this, Loader.screenW/2-50, 12, 100, 123, 4));
	
		addElement(passBtn = new GuiButtonAnimated(this, new ElementPos(this, -5, 850, 397, 88, 2), "pass"));
	}
	
	@Override
	public void update(GameContainer arg0, GuiBasedGame arg1, int arg2) throws SlickException {
		super.update(arg0, arg1, arg2);
		if (update) {
			playerL.tick(); playerO.tick();
			state().update();
		}
	}

	@Override
	public void render(GameContainer arg0, GuiBasedGame arg1, Graphics g) throws SlickException {
		pos.update(); this.resetClipping(g);
		Loader.iGameBG.draw(-89, -65, 1920+177, 1080+138);
		
		Loader.iChar[playerO.player().getCharID()].draw(42+5, 31+10, 60, 60);
		Loader.iCframe[playerO.player().getCframeID()].draw(-3+5, -15+10, 150, 150);
		Loader.iChar[playerL.player().getCharID()].draw(42+5, Loader.screenH-40-60, 60, 60);
		Loader.iCframe[playerL.player().getCframeID()].draw(-3+5, Loader.screenH-86-60, 150, 150);
		
		Loader.iRibbonR.draw(21, 260, 384, 180);
		Loader.iRibbonB.draw(21, 642, 384, 180);
		Loader.iIconDeck.draw(1842, 963, 64, 64);
		Loader.iIconGrave.draw(1490, 963+3, 64, 64);
		Loader.iIconDeck.draw(1842, 48, 64, 64);
		Loader.iIconGrave.draw(1490, 50-3, 64, 64);
		Loader.iDivider1.draw(1425, 974, 44, 97);
		Loader.iDivider1.draw(496, 970, 44, 97);
		Loader.iDivider1.draw(1425, 8, 44, 97);
		Loader.iDivider1.draw(496, 12, 44, 97);
		Loader.iDivider2.draw(617, 12, 44, 97);
		Loader.iDivider2.draw(617, 970, 44, 97);
		
		Loader.fontNum1.renderBuildCentered(g, String.valueOf(playerO.board[0].getPoints()), 456, 467, 0.3F, -8, 42);
		Loader.fontNum1.renderBuildCentered(g, String.valueOf(playerO.board[1].getPoints()), 456, 350, 0.3F, -8, 42);
		Loader.fontNum1.renderBuildCentered(g, String.valueOf(playerO.board[2].getPoints()), 456, 233, 0.3F, -8, 42);
		Loader.fontNum1.renderBuildCentered(g, String.valueOf(playerL.board[0].getPoints()), 456, 613, 0.3F, -8, 42);
		Loader.fontNum1.renderBuildCentered(g, String.valueOf(playerL.board[1].getPoints()), 456, 730, 0.3F, -8, 42);
		Loader.fontNum1.renderBuildCentered(g, String.valueOf(playerL.board[2].getPoints()), 456, 847, 0.3F, -8, 42);
		Loader.fontNum1.renderBuildCenteredX(g, String.valueOf(playerO.getPoints()), 300, 316, 0.4F, -10);
		Loader.fontNum1.renderBuildCenteredX(g, String.valueOf(playerL.getPoints()), 300, 696, 0.4F, -10);
		
		Toolkit.drawBaseCentered(String.valueOf(playerL.deck.getSize()), 1876, 1030, Loader.fontOBL);
		Toolkit.drawBaseCentered(String.valueOf(playerL.graveyard.getSize()), 1522, 1030, Loader.fontOBL);
		Toolkit.drawBaseCentered(String.valueOf(playerO.deck.getSize()), 1876, 46, Loader.fontORD);
		Toolkit.drawBaseCentered(String.valueOf(playerO.graveyard.getSize()), 1522, 46, Loader.fontORD);
		Loader.fontRD.drawString(150, 30, playerO.player().getName().toLowerCase());
		Loader.fontN.drawString(150, 65, playerO.player().getTitle().toLowerCase());
		Loader.fontBL.drawString(150, Loader.screenH-110, playerL.player().getName().toLowerCase());
		Loader.fontN.drawString(150, Loader.screenH-75, playerL.player().getTitle().toLowerCase());
		if (playerL.getCrown()>0) Loader.iCrown[0].draw(122, 672, 115, 115);
		if (playerL.getCrown()>1) Loader.iCrown[1].draw(122, 672, 115, 115);
		if (playerO.getCrown()>0) Loader.iCrown[2].draw(122, 292, 115, 115);
		if (playerO.getCrown()>1) Loader.iCrown[3].draw(122, 292, 115, 115);
		
		for (GuiElement e : elements) {
			e.getPos().update();
			if (e.isVisible()) {e.render(arg0, arg1, g);}
		} 
		
		sequenceContext.render(g);
		if (state()!=null) state().render(g);
		g.setColor(Color.black);
		g.drawString("pos: x "+tx+" y "+ty+" w "+tw+" y "+th+(playerL.canInteract() ? " T" : ""), 150, 10);
		g.drawString(debug, 150, 30);
		g.setColor(Color.white);
		g.drawString(seqbase.debug() + "\n" + sequenceContext.debug(), 30, 420);
	}
	
	@Override
	public void elementCallback(GuiElement element, int eventID, int data) {
		if (element==passBtn && eventID==0 && data==0) {
			if (playerL.canInteract() && !playerL.passed()) {playerL.pass(); passBtn.hide();}
			return;
		}
		System.out.println(element);
		if (element==playerL.graveyard && eventID==0 && data==0 && playerL.canInteract() && playerL.graveyard.getSize()>0) {
			playerL.viewContainer(state(), playerL.graveyard);
			return;
		}
		if (element==playerO.graveyard && eventID==0 && data==0 && playerL.canInteract() && !playerO.passed() && playerO.graveyard.getSize()>0) {
			playerL.viewContainer(state(), playerO.graveyard);
			return;
		}
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int count) {
		super.mouseClicked(button, x, y, count); tx=x; ty=y; //DEBUG
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (c=='y') {update=!update;} // pause/unpause sequence updates
		if (c=='x') {state().update();} // trigger one single sequence update
		if (c=='0') {debug="";} // clear debug string
		if (c=='1') {playerL.deck.draw(state(), -1, playerL);} // you draw a card
		if (c=='2') {playerO.deck.draw(state(), -1, playerO);} // opponent draws a card
		if (c=='p') { // for debugging
			System.out.println("VM breakpoint requested");
		}
		if (c=='9') {playerL.mulligan(state(), 3);} // trigger a mulligan
		super.keyPressed(key, c);
 	}
	
	public void initGame(LocalPlayer playerLoc, AIPlayer playerOpp) {
		this.sequenceContext.reset(); round=0;
		this.queueState(new SequenceState(seqbase, true));
		new GameIntro(state(), 0, playerOpp, playerLoc);
		SequenceHandler initO = new AdvancedSeqHandler(state(), 1).setShouldEndWhenEmpty(true);
		SequenceHandler initL = new AdvancedSeqHandler(state(), 1).setShouldEndWhenEmpty(true);
		this.playerL.initGame(initL, playerLoc);
		this.playerO.initGame(initO, playerOpp);
		this.playerO.mulligan(initO, 3);
		this.playerL.mulligan(initL, 3);
		queueState(new StateOnTurn(seqbase, playerL)); // coinflip later?
		passBtn.show();
	}
	
	public void setLocalHighlighted(Card card, int highlight, boolean gray) {
		//if (card==null && state instanceof StateDeploying) {return;} // not working/needed
		if (card==playerL.highlighted) {return;}
		playerL.setHighlighted(card);
		if (currentPrewiew!=null) {currentPrewiew.dismiss(); currentPrewiew=null;}
		if (card==null || !cardPrewiew) {return;}
		currentPrewiew=new CardPrewiewTask(this.sequenceContext, 0, card, 0, gray);
	}
	
	public void queueState(SequenceState state) {
		this.seqbase.addToQueue(state);
	}
	
	public SequenceState state() {
		return seqbase.get();
	}
	
	public void roundEnd(Perspective<?> winner, SequenceState transition) {
		if (playerL.getCrown()<2 && playerO.getCrown()<2) {
			round++;
			this.passBtn.show();
			this.queueState(new StateOnTurn(seqbase, winner!=null?winner.opp:(Main.rand.nextBoolean()?playerL:playerO)));
		}
	}
	
	public void gameEnd() {
		playerL.endGame();
		playerO.endGame();
		Main.instance().enterState(3);
	}
	
	public static StateGame get() {return instance;}
	public Perspective<LocalPlayer> playerL() {return playerL;}
	public Perspective<AIPlayer> playerO() {return playerO;}
	public ElementPos boardRect() {return boardRect;}
	public ElementPos oppChooser() {return oppChooser;}

	public class StateOnTurn extends SequenceState {
		
		private final Perspective<?> player;
		public String toString() {return "StateOnTurn";}

		public StateOnTurn(SequenceQueue base, Perspective<?> player) {
			super(base, false); this.player=player;
		}
		
		@Override
		public void enter() {
			onTurn=player;
			onTurn.onTurnStart();
		}
		
		@Override
		public void leave() {
			onTurn.onTurnEnd();
			if (onTurn.passed() && onTurn.opp.passed()) {
				onTurn=null;
				Perspective<?> winner = null;
				if (playerL.getPoints()!=playerO.getPoints()) { //tie?
					winner = playerL.getPoints()>playerO.getPoints()?playerL:playerO;	
				}
				queueState(new RoundEndTask(seqbase, winner, round, playerL.getPoints(), playerO.getPoints(), playerL.crown, playerO.crown));
			} else {
				queueState(new StateOnTurn(seqbase, onTurn.opp));
				onTurn=null;
			}
		}
		
		@Override
		public boolean canInteract(Perspective<?> persp) {
			return player==persp && !player.passed() && this.isIdle();
		}
		
	}

}

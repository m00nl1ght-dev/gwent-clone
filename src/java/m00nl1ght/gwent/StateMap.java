package m00nl1ght.gwent;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.Cards;
import m00nl1ght.gwent.game.AIPlayer;
import m00nl1ght.gwent.game.Deck;
import m00nl1ght.gwent.game.campaign.TestAI;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiButton;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiScreen;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class StateMap extends GuiScreen {

	private GuiButton btnTest;
	private AIPlayer testPlayer = new TestAI();
	public Deck test1, test2;

	protected StateMap(int id, GameContainer gc) {
		super(id, gc);
	}
	
	@Override
	public void init(GameContainer arg0, GuiBasedGame arg1) throws SlickException {
		super.init(arg0, arg1);
		
		addElement(btnTest=new GuiButton(this, new ElementPos(this, Loader.screenW/2-220, Loader.screenH/2+350, 441, 98, 1), "test game"));
		
		test1=new Deck();
		test1.setFaction(5);
		test1.setLeader(new Card(Cards.CRACH_AN_CRAITE));
		for (int i=0;i<13;i++) test1.getCards().add(new Card(Cards.TUIRSEACH_HUNTER));
		for (int i=0;i<3;i++) test1.getCards().add(new Card(Cards.UDALRYK));
		for (int i=0;i<4;i++) test1.getCards().add(new Card(Cards.SIGRDRIFA));
		
		test2=new Deck();
		test2.setFaction(4);
		test2.setLeader(new Card(Cards.CRACH_AN_CRAITE));
		for (int i=0;i<13;i++) test2.getCards().add(new Card(Cards.TUIRSEACH_HUNTER));
		for (int i=0;i<3;i++) test2.getCards().add(new Card(Cards.UDALRYK));
		for (int i=0;i<4;i++) test2.getCards().add(new Card(Cards.SIGRDRIFA));
		
	}
	
	@Override
	public void render(GameContainer arg0, GuiBasedGame arg1, Graphics g) throws SlickException {
		Loader.iBackground.draw(0, 0, Loader.screenW, Loader.screenH);
		super.render(arg0, arg1, g);
	}
	
	@Override
	public void elementCallback(GuiElement element, int eventID, int data) {
		if (element==btnTest) {
			Profile.user.setDeck(test1);
			testPlayer.init(test2);
			Main.instance.stateGame.initGame(Profile.user.derivePlayer(), testPlayer);
			Main.instance.enterState(Main.instance.stateGame.getID());
		}
	}

}

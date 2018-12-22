package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CardRowLinearFullInfo extends CardRowLinear {

	public CardRowLinearFullInfo(GuiContainer<? extends GuiElement> base, ElementPos position, CardLocation location, boolean mutable) {
		super(base, position, location, mutable);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (cards.size()<=0) {return;}
		if (scroll.updateOnRender()) this.updateSelection(false);
		for (int i=0; i<cards.size();i++) {
			CardData card = cards.get(i);
			if (card.data<0F) {continue;}
			if (baseVec[i] + scroll.get() - nSpacing> pos.w()) {continue;}
			if (baseVec[i] + scroll.get() + cardW < 0F) {continue;}
			card.card.drawFull(g, pos.x() + baseVec[i] + scroll.get(), pos.y(), cardW, cardH, 1, card==selected ? 1 : StateGame.get().playerO().getHighlighted()==card.card ? 2 : -1);
			card.card.drawInfo(g, pos.x() + baseVec[i] + scroll.get(), pos.y()+450F, 300F, Card.alpha100);
		}
	}

}

package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.local.CardPickUp;
import m00nl1ght.gwent.game.local.DraggedCard;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CardRowLayered extends CardRow {

	public CardRowLayered(GuiContainer<? extends GuiElement> base, ElementPos position, CardLocation location) {
		super(base, position, location);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (cards.size()<=0) {return;}
		if (pushPrg>0F && prgVec==null) {throw new IllegalStateException("pushPrg > 0 but no pushVec provided!");}
		for (int i=0; i<cards.size();i++) {
			i=renderRecursive(g, i);
		}
	}
	
	private int renderRecursive(Graphics g, int i) {
		CardData card = cards.get(i); int n = i;
		if (cards.size()>i+1 && card.data>cards.get(i+1).data) {n = renderRecursive(g, i+1);}
		if (card.data<0F) {return n;}
		if (card==selected) {
			card.data+=0.1F;
			if(card.data>1F) {card.data=1F;}
			card.card.draw(g, pos.x() + baseVec[i] + (pushPrg>0F ? (prgVec[i]-baseVec[i])*pushPrg : 0F), pos.y() + card.data*selOffset, cardW, cardH, 1, 1);
		} else {
			card.data-=0.1F;
			if(card.data<0F) {card.data=0F;}
			card.card.draw(g, pos.x() + baseVec[i] + (pushPrg>0F ? (prgVec[i]-baseVec[i])*pushPrg : 0F), pos.y() + card.data*selOffset, cardW, cardH, 1, StateGame.get().playerO().getHighlighted()==card.card ? 2 : -1);
		}
		return n;
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (!visible || !location.canInteract()) {return;}
		if (selected != null) {
			CardData card = selected; //selection update could mess it up without buffer
			if (isPushing) {return;}
			card.data=0F;
			DraggedCard d = new DraggedCard(StateGame.get(), StateGame.get().state(), this, new ElementPos(base, pos.x()+baseVec[cards.indexOf(card)], pos.y()-20, 100F, 123F, 5), card.card, true);
			StateGame.get().addElement(d);
			StateGame.get().state().setShouldEndWhenEmpty(false);
			d.mouseDragged(oldx, oldy, newx, newy);
			base.setSelectedElement(d);
			new CardPickUp(StateGame.get().state(), -2, card.card, d, 122F, 150F, 10);
			new RemoveCardAnimation(StateGame.get().state(), -2, card.card, 0.1F);
			selected = null;
		}
	}
	
}
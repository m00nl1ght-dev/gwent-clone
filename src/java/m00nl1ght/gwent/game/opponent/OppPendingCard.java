package m00nl1ght.gwent.game.opponent;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.sequence.Interactable;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class OppPendingCard extends GuiElement implements Interactable {
	
	public final Card card;
	public final SequenceHandler seqctx;
	
	public OppPendingCard(GuiContainer<? extends GuiElement> base, ElementPos position, SequenceHandler seqctx, Card card) {
		super(base, position); this.card=card; this.seqctx=seqctx;
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		card.draw(g, pos.x(), pos.y(), pos.w(), pos.h(), 1, StateGame.get().playerL().getHighlighted()==card? 1 : StateGame.get().playerO().getHighlighted()==card ? 2 : -1, false);
	}
	
	@Override
	public void gainFocus() {
		StateGame.get().setLocalHighlighted(card, 1, false);
	}
	
	@Override
	public void leaveFocus() {
		StateGame.get().setLocalHighlighted(null, 0, false);
	}

	@Override
	public SequenceHandler getHandler() {
		return seqctx;
	}

	@Override
	public int getPriority() {
		return -1;
	}

}

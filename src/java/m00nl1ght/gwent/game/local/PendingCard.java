package m00nl1ght.gwent.game.local;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class PendingCard extends DraggedCard {

	public PendingCard(GuiContainer<? extends GuiElement> base, SequenceHandler seqctx, ElementPos position, Card card) {
		super(base, seqctx, null, position, card, false);
	}
	
	@Override
	public void mouseReleased(int button, int x2, int y2) {
		if (button==0 && !isCatched) {
			updateAni(x2, y2, x2, y2, false);
			if (lastRow != null && lastRow.accepts(card)) {
				if (lastP!=null) {lastP.keep();}
				phantom.unregister();
				new CardDeployToRow(seqctx, -2, lastRow, card, phantom, lastIdx, false);
				StateGame.get().playerL().setOverlayState(2);
				StateGame.get().removeElement(this);
				seqctx.setShouldEndWhenEmpty(true);
				lastRow.mouseMoved(x2, y2, x2, y2);
				isCatched=true;
			} else {
				new CardMoveToPending(seqctx, -2, StateGame.get().playerL(), card, 20, 0);
				StateGame.get().removeElement(this);
				if (lastRow != null) {lastRow.mouseMoved(x2, y2, x2, y2);}
				isCatched=true;
				phantom.completed();
				StateGame.get().setLocalHighlighted(null, 0, false);
			}
		}
	}

}

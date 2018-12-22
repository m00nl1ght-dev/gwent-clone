package m00nl1ght.gwent.game.local;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardContainer;
import m00nl1ght.gwent.game.common.CardRow;
import m00nl1ght.gwent.game.common.PassiveCardMoveTask;
import m00nl1ght.gwent.game.common.CardRow.PhantomCardAnimation;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class DraggedCard extends GuiElement {
	
	protected final Card card;
	protected final CardContainer source;
	protected final SequenceHandler seqctx;
	public final boolean fromHand;
	protected boolean isCatched = false;
	protected PhantomCardAnimation lastP;
	protected int lastIdx = -2;
	protected CardRow lastRow = null;
	public  PhantomPrewiewAnimation phantom;

	public DraggedCard(GuiContainer<? extends GuiElement> base, SequenceHandler seqctx, CardContainer source, ElementPos position, Card card, boolean fromHand) {
		super(base, position); this.card=card; this.source=source; this.seqctx=seqctx; this.fromHand=fromHand;
		phantom = new PhantomPrewiewAnimation(seqctx, -1, card);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		phantom.renderD(g); // see renderD() for reference
		card.draw(g, pos.x(), pos.y(), pos.w(), pos.h(), 1, StateGame.get().playerL().getHighlighted()==card? 1 : -1, false);
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		pos.add(newx-oldx, newy-oldy);
		updateAni(oldx, oldy, newx, newy, true);
	}
	
	public void updateAni(int oldx, int oldy, int newx, int newy, boolean checkPos) {
		GuiElement below = base.getBelowSelected();
		int rowIdx = -2; CardRow belowRow = null;
		if (below instanceof CardRow && ((CardRow)below).accepts(card)) {
			belowRow = (CardRow)below;
			rowIdx = belowRow.calcIdxFromMouseXY(newx, newy);
		} else {
			if (checkPos && StateGame.get().boardRect().within(newx, newy)) {return;}
			belowRow=null;
			if (!phantom.moving) {phantom.draw = false;}
			if (lastRow!=null) {
				if (lastP!=null) {lastP.end();} lastP=null; lastRow=null; lastIdx=-2;
			} return;
		}
		
		if (belowRow==lastRow) {
			if (rowIdx==lastIdx) {return;}
			phantom.move(belowRow, rowIdx);
			lastIdx = rowIdx;
			lastP.push(rowIdx);
		} else {
			if (lastRow!=null && lastP != null) {lastP.end(); lastP=null;}
			if (belowRow.isPushing()) {lastRow=null; lastIdx=-2; return;}
			lastRow = belowRow; lastIdx = rowIdx;
			phantom.move(belowRow, rowIdx);
			if (belowRow.getSize()<=0) {lastP=null; return;}
			lastP = belowRow.new PhantomCardAnimation(seqctx, -2, 0.1F);
			lastP.push(rowIdx);
		}
	}
	
	@Override
	public void mouseReleased(int button, int x2, int y2) {
		if (button==0 && !isCatched) {
			updateAni(x2, y2, x2, y2, false);
			if (lastRow != null && lastRow.accepts(card)) {
				if (lastP!=null) {lastP.keep();}
				phantom.unregister();
				new CardDeployToRow(seqctx, -2, lastRow, card, phantom, lastIdx, false);
				StateGame.get().removeElement(this);
				seqctx.setShouldEndWhenEmpty(true);
				lastRow.mouseMoved(x2, y2, x2, y2);
				isCatched=true;
			} else {
				source.catchCard(seqctx, card);
				StateGame.get().removeElement(this);
				if (lastRow != null) {lastRow.mouseMoved(x2, y2, x2, y2);}
				isCatched=true;
				phantom.completed();
				StateGame.get().setLocalHighlighted(null, 0, false);
			}
		}
	}
	
	@Override
	public void gainFocus() {
		StateGame.get().setLocalHighlighted(card, 1, false);
	}
	
	@Override
	public void leaveFocus() {
		StateGame.get().setLocalHighlighted(null, 0, false);
	}
	
	public class PhantomPrewiewAnimation extends PassiveCardMoveTask {
		
		protected boolean moving = false, draw = false;

		public PhantomPrewiewAnimation(SequenceHandler container, int priority, Card card) {
			super(container, priority, card);
		}
		
		@Override
		public void render(Graphics g) {} // draw directly using renderD() from DraggedCard.render() instead (solves overlapping)
		
		public void renderD(Graphics g) { // ^^
			if (!draw) {return;}
			card.drawPhantom(g, x, y, w, h);
		}
		
		@Override
		public void update() {
			if (!moving) {return;}
			if (updateCardMove()) {moving=false;}
		}
		
		public void move(float tx, float ty, float tw, float th) {
			if (draw==false) {
				initPos(tx, ty, tw, th);
				draw=true; return;
			}
			initCardMove(tx, ty, tw, th, 10);
			moving=true;
		}
		
		public void move(CardRow row, int idx) {
			move(row.getPos().x() + row.calcVec(row.getSize()+1, -1)[idx], row.getPos().y(), row.cardW, row.cardH);
		}
		
		@Override
		public void completed() {
			this.unregister();
		}
		
		@Override
		public String toString() {
			return "PhantomPreview {m:"+moving+" d:"+draw+" c:"+card+"}";
		}
		
	}

}

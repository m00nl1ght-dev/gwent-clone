package m00nl1ght.gwent.game.opponent;

import java.util.ArrayList;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardChooserCloseTask;
import m00nl1ght.gwent.game.common.CardChooserToHand;
import m00nl1ght.gwent.game.common.CardChooserToStack;
import m00nl1ght.gwent.game.common.CardDrawToHand;
import m00nl1ght.gwent.game.common.CardLocation;
import m00nl1ght.gwent.game.common.CardRowLinear;
import m00nl1ght.gwent.game.common.CardRowLinearFull;
import m00nl1ght.gwent.game.common.ICardChooserUI;
import m00nl1ght.gwent.game.common.MulliganTask;
import m00nl1ght.gwent.game.common.CardLocation.Location;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallbackBase;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiPopup;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class OppChooserOverlay extends GuiPopup implements ICardChooserUI {
	
	protected CardRowLinear row;
	protected CardCallback callback;
	protected String text;

	public OppChooserOverlay(GuiContainer<? extends GuiElement> base, ElementPos position, Perspective<?> player) {
		super(base, position);
		row = new CardRowLinearFull(this, new ElementPos(this, 0F, 95F, Loader.screenW, 280F, 0), new CardLocation(Location.NULL, player), false);
		row.setProperties(0F, 50F, 200F, 280F);
		this.addElement(row);
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		this.resetClipping(g);
		if (alpha<=0F) {return;}
		if (alpha<1F) Color.setBlendFactor(alpha);
		Loader.iOverlay.setRotation(180);
		Loader.iOverlay.draw(pos.x(), pos.y(), pos.x()+pos.w(), pos.y()+pos.h(), 65, 10, 65+pos.w(), 10+pos.h());
		Loader.iOverlay.setRotation(0);
		Toolkit.drawBaseCentered(text, Loader.screenW/2, 40F, Loader.fontODC);
		super.render(container, game, g);
		Color.resetBlendFactor();
	}

	@Override
	public void setCallback(CardCallback callback) {
		this.callback=callback;
	}
	
	@Override
	public boolean shouldDrawAni() {
		return !StateGame.get().playerL().chooser().shouldDrawAni();
	}
	
	@Override
	public boolean checkSelected(int mx, int my) {
		if (alpha>0 && pos.within(mx, my)) {return true;}
		return false;
	}

	@Override
	public void setText(String text) {
		this.text=text;
	}

	@Override
	public CardRowLinear row() {
		return row;
	}

	@Override
	public void enable() {
		StateGame.get().addElement(this);
		this.setVisible(true);
	}

	@Override
	public void disable() {
		this.row.clear();
		StateGame.get().removeElement(this);
		this.setVisible(false);
	}
	
	@Override
	public boolean active() {
		return alpha>0F;
	}
	
	public String toString() {
		return "OppChooserOverlay";
	}
	
	public static class OppMulliganCallback extends CardCallbackBase {

		private int count;
		private Perspective<?> persp;

		public OppMulliganCallback(SequenceHandler seqctx, Perspective<?> persp, int count) {
			super(seqctx); this.count=count; this.persp=persp;
		}

		public OppMulliganCallback init() {
			persp.chooser().setText("Your opponent is redrawing cards.");
			seqctx.makeKeepAlive(true);
			return this;
		}

		@Override
		public void result(Card card) {
			count--;
			new MulliganTask(seqctx, -2, card, persp.chooser(), persp.deck(), 16, count<=0).tryStart();
			if (count<=0) {end(false);}
		}

		@Override
		public void cancel() {
			end(true);
		}

		@Override
		public void exception(int i) {

		}

		private void end(boolean immedialtely) {
			new CardChooserToHand(seqctx, -1, persp, persp.chooser(), immedialtely ? 0 : 20).tryStart();
			seqctx.makeKeepAlive(false);
		}

		public int count() {
			return count;
		}
		
		public String toString() {
			return "OppMulliganCallback";
		}

	}

	public static class OppDraw1ofXCallback extends CardCallbackBase {

		private Perspective<?> persp;
		protected ArrayList<Card> cards;

		public OppDraw1ofXCallback(SequenceHandler seqctx, Perspective<?> persp, ArrayList<Card> cards) {
			super(seqctx); this.persp=persp; this.cards=cards;
		}

		public OppDraw1ofXCallback init() {
			persp.chooser().setText("Your opponent is choosing a card.");
			seqctx.makeKeepAlive(true);
			return this;
		}

		@Override
		public void result(Card card) {
			persp.chooser().row().setCardData(card, -1F); persp.chooser().row().setSelected(-1);
			end(true);
			new CardDrawToHand(seqctx, -2, persp.hand(), card, persp.drawPos(), 2).tryStart();
		}

		@Override
		public void cancel() {
			end(true);
		}

		@Override
		public void exception(int i) {

		}
		
		public ArrayList<Card> getCards() {return cards;}

		private void end(boolean immedialtely) {
			if (persp.chooser().row().getVisibleCount()<=0) {
				new CardChooserCloseTask(seqctx, -2, persp.chooser(), 0.05F);
			} else {
				new CardChooserToStack(seqctx, -2, persp.deck(), persp.chooser(), immedialtely ? 0 : 20).tryStart();
			}
			seqctx.makeKeepAlive(false);
		}
		
		public String toString() {
			return "OppDraw1ofXCallback";
		}

	}

	public static class OppResurrectCallback extends CardCallbackBase {

		private Perspective<?> persp;

		public OppResurrectCallback(SequenceHandler seqctx, Perspective<?> persp) {
			super(seqctx); this.persp=persp;
		}

		public OppResurrectCallback init() {
			persp.chooser().setText("Your opponent is choosing a card to resurrect");
			seqctx.makeKeepAlive(true);
			return this;
		}

		@Override
		public void result(Card card) {
			persp.chooser().row().setCardData(card, -1F); persp.chooser().row().setSelected(-1);
			end(true);
			persp.setOverlayState(3);
			new OppCardMoveToPending(seqctx, -2, persp, card, 20, 2).tryStart();
		}

		@Override
		public void cancel() {
			end(true);
		}

		@Override
		public void exception(int i) {

		}

		private void end(boolean immedialtely) {
			if (persp.chooser().row().getVisibleCount()<=0) {
				new CardChooserCloseTask(seqctx, -2, persp.chooser(), 0.05F);
			} else {
				new CardChooserToStack(seqctx, -2, persp.graveyard(), persp.chooser(), immedialtely ? 0 : 20).tryStart();
			}
			seqctx.makeKeepAlive(false);
		}
		
		public String toString() {
			return "OppResurrectCallback";
		}

	}

}

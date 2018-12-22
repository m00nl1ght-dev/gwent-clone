package m00nl1ght.gwent.game.local;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.LocalPlayer;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.PerspectiveLocal;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardChooserCloseTask;
import m00nl1ght.gwent.game.common.CardChooserToHand;
import m00nl1ght.gwent.game.common.CardChooserToStack;
import m00nl1ght.gwent.game.common.CardLocation;
import m00nl1ght.gwent.game.common.CardMoveToHand;
import m00nl1ght.gwent.game.common.CardRowLinear;
import m00nl1ght.gwent.game.common.CardRowLinearFullInfo;
import m00nl1ght.gwent.game.common.CardStack;
import m00nl1ght.gwent.game.common.ICardChooserUI;
import m00nl1ght.gwent.game.common.MulliganTask;
import m00nl1ght.gwent.game.common.CardLocation.Location;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallbackBase;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.ElementPos.AlignType;
import m00nl1ght.voidUI.gui.GuiButton;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiPopup;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class LocalCardChooser extends GuiPopup implements ICardChooserUI {
	
	public static final float DEPTH = 10;
	protected CardRowLinearFullInfo row;
	protected CardCallback callback;
	protected GuiButton btn;
	protected String text;

	public LocalCardChooser(GuiContainer<? extends GuiElement> base, Perspective<LocalPlayer> player) {
		super(base, base.getPos().sub(DEPTH));
		row = new CardRowLinearFullInfo(this, new ElementPos(this, 0F, Loader.screenH/2-300F, Loader.screenW, 420F, 0), new CardLocation(Location.NULL, player), true);
		row.setProperties(0F, 90F, 300F, 420F);
		this.addElement(row);
		this.addElement(btn = new GuiButton(this, new ElementPos(this, 0F, Loader.screenH-105F, 441, 98, 1, AlignType.CENTEREDX), "cancel"));
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		this.resetClipping(g);
		if (alpha<=0F) {return;}
		if (alpha<1F) Color.setBlendFactor(alpha);
		Loader.iGenBG.draw(0F, 0F, Loader.screenW, Loader.screenH);
		Toolkit.drawBaseCentered(text, Loader.screenW/2, 60F, Loader.fontODC);
		super.render(container, game, g);
		Color.resetBlendFactor();
	}

	public void setCallback(CardCallback callback) {
		this.callback=callback;
	}
	
	@Override
	public boolean shouldDrawAni() {
		return visible;
	}
	
	@Override
	public void elementCallback(GuiElement e, int event, int data) {
		if (row.scroll.isLocked()) {return;}
		if (e==row) {
			if (event==1) {callback.result(row.get(data));}
			return;
		}
		if (e==btn) {
			if (event==0) {callback.cancel();}
			return;
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (key==Input.KEY_ENTER) {
			callback.cancel();
			return;
		}
		if (selected!=null) {
			selected.keyPressed(key, c);
		}
	}
	
	public void setText(String t) {
		text=t;
	}
	
	@Override
	public CardRowLinear row() {return row;}
	
	public void enable() {
		StateGame.get().addElement(this);
		StateGame.get().cardPrewiew=false;
		this.setVisible(true);
	}
	
	public void disable() {
		this.row.clear();
		StateGame.get().removeElement(this);
		StateGame.get().cardPrewiew=true;
		this.setVisible(false);
	}
	

	@Override
	public boolean active() {
		return this.alpha>0F;
	}
	
	public static class MulliganCallback extends CardCallbackBase {
		
		private int count;
		private PerspectiveLocal persp;
		
		public MulliganCallback(SequenceHandler seqctx, PerspectiveLocal persp, int count) {
			super(seqctx); this.count=count; this.persp=persp;
		}
		
		public MulliganCallback init() {
			persp.chooser().setText("You may redraw up to "+count+" cards.");
			seqctx.makeKeepAlive(true);
			return this;
		}

		@Override
		public void result(Card card) {
			count--;
			persp.chooser().setText(count>0 ? "You may redraw up to "+count+" of your cards." : "");
			new MulliganTask(seqctx, -1, card, persp.chooser(), persp.deck(), 16, count<=0).tryStart();
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
		
	}
	
	public static class Draw1ofXCallback extends CardCallbackBase {
		
		private PerspectiveLocal persp;
		
		public Draw1ofXCallback(SequenceHandler seqctx, PerspectiveLocal persp) {
			super(seqctx); this.persp=persp;
		}
		
		public Draw1ofXCallback init() {
			persp.chooser().setText("Choose a card.");
			seqctx.makeKeepAlive(true);
			return this;
		}

		@Override
		public void result(Card card) {
			persp.chooser().row().setCardData(card, -1F); persp.chooser().row().setSelected(-1);
			end(true);
			new CardMoveToHand(seqctx, -2, persp.hand(), card, 20, 0.1F, 2).tryStart();
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
				new CardChooserCloseTask(seqctx, -1, persp.chooser(), 0.05F);
			} else {
				new CardChooserToStack(seqctx, -1, persp.deck(), persp.chooser(), immedialtely ? 0 : 20).tryStart();
			}
			seqctx.makeKeepAlive(false);
		}
		
	}
	
	public static class ResurrectCallback extends CardCallbackBase {
		
		private PerspectiveLocal persp;
		
		public ResurrectCallback(SequenceHandler seqctx, PerspectiveLocal persp) {
			super(seqctx); this.persp=persp;
		}
		
		public ResurrectCallback init() {
			persp.chooser().setText("Choose a card to resurrect.");
			seqctx.makeKeepAlive(true);
			return this;
		}

		@Override
		public void result(Card card) {
			persp.chooser().row().setCardData(card, -1F); persp.chooser().row().setSelected(-1);
			end(true);
			new CardMoveToPending(seqctx, -2, persp, card, 20, 2).tryStart();
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
				new CardChooserCloseTask(seqctx, -1, persp.chooser(), 0.05F);
			} else {
				new CardChooserToStack(seqctx, -1, persp.graveyard(), persp.chooser(), immedialtely ? 0 : 20).tryStart();
			}
			seqctx.makeKeepAlive(false);
		}
		
	}
	
	public static class ViewCallback extends CardCallbackBase {
		
		private PerspectiveLocal persp;
		private CardStack stack;
		
		public ViewCallback(SequenceHandler seqctx, PerspectiveLocal persp, CardStack stack) {
			super(seqctx); this.persp=persp; this.stack=stack;
		}
		
		public ViewCallback init() {
			persp.chooser().setText(stack.getiBase().base.name());
			seqctx.makeKeepAlive(true);
			return this;
		}

		@Override
		public void result(Card card) {
			//NOP
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
				new CardChooserCloseTask(seqctx, -1, persp.chooser(), 0.05F);
			} else {
				new CardChooserToStack(seqctx, -1, stack, persp.chooser(), immedialtely ? 0 : 20).tryStart();
			}
			seqctx.makeKeepAlive(false);
		}
		
	}

}

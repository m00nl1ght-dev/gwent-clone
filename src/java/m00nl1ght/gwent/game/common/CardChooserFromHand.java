package m00nl1ght.gwent.game.common;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardRow.CardData;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class CardChooserFromHand extends CardMultiMoveTask {
	
	protected ICardChooserUI chooser;
	private Perspective<?> player;
	protected float a = 0F;
	protected CardFilter[] filters;

	public CardChooserFromHand(SequenceHandler container, int priority, Perspective<?> player, ICardChooserUI chooser, CardFilter... filters) {
		super(container, priority, 20, 0); this.chooser=chooser; this.player=player; this.filters=filters;
	}
	
	@Override
	public void render(Graphics g) {
		if (!chooser.shouldDrawAni()) {return;}
		for (int i=0; i<list.size(); i++) {
			if (step[i]>=0) {
				Color.setBlendFactor(1F-a);
				list.get(i).draw(g, 1, -1);
				Color.setBlendFactor(a);
				list.get(i).drawFull(g, 1, -1);
				Color.resetBlendFactor();
			}
		}
	}
	
	@Override
	public void update() {
		a = ((float)step[list.size()-1] / frames);
		chooser.setAlpha(a);
		if (updateCardMove()) {this.completed();}
	}
	
	@Override
	public boolean start() {
		if (player.hand().isPushing()) {return false;}
		ArrayList<Card> temp = new ArrayList<Card>();
		for (CardData data : player.hand().getDataList()) {
			if (!CardFilter.filter(data.card, filters)) {continue;}
			temp.add(data.card); data.data=-1F;
		}
		if (temp.size()<=0) {this.unregister(); return false;}
		this.init(temp);
		float[] vec = chooser.row().calcVec(list.size(), -1);
		float scroll = chooser.row().scroll.setPropertiesExt(vec[list.size()-1]+chooser.row().cardW, chooser.row().getPos().w(), 200F);
		chooser.enable(); chooser.setAlpha(a);
		chooser.row().scroll.lock(false);
		chooser.row().initFillOperation();
		StateGame.get().setLocalHighlighted(null, 0, false);
		for (int i=0; i<list.size(); i++) {
			initCardMove(i, chooser.row().getPos().x()+vec[i]+scroll, chooser.row().getPos().y(), chooser.row().cardW, chooser.row().cardH);
		}
		return true;
	}

	@Override
	public void completed() {
		chooser.setAlpha(1F);
		player.hand().cleanupFlagged();
		chooser.row().fillWith(list, false);
		this.unregister();
	}
	
	public ICardChooserUI getChooser() {
		return chooser;
	}
	
	public Perspective<?> getPlayer() {
		return player;
	}
	
	public String toString() {
		return "ccFromHand {a:"+a+"}";
	}
	
}

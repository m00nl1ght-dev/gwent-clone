package m00nl1ght.gwent.game.common;

import java.util.ArrayList;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardRow.CardData;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class CardChooserToHand extends CardMultiMoveTask {

	protected ICardChooserUI chooser;
	private Perspective<?> player;
	protected float a = 0F;
	protected int wait;

	public CardChooserToHand(SequenceHandler container, int priority, Perspective<?> player, ICardChooserUI chooser, int wait) {
		super(container, priority, 20, 0); this.chooser=chooser; this.player=player; this.wait=wait;
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
		a = 1F - ((float)step[list.size()-1] / frames);
		chooser.setAlpha(a);
		if (updateCardMove()) {this.completed();}
	}
	
	@Override
	public boolean start() {
		if (player.hand().isPushing() || wait>0) {wait--; return false;}
		StateGame.get().setLocalHighlighted(null, 0, false);
		ArrayList<Card> temp = new ArrayList<Card>();
		float[] vecadd = new float[chooser.row().getSize()];
		for (int i=0; i<chooser.row().getSize(); i++) {
			CardData data = chooser.row().getDataList().get(i);
			if (data.data>=0F) {temp.add(data.card); vecadd[temp.size()-1]=player.hand().getStableVec()[i];}
		}
		this.init(temp);
		if (list.size()<=0) {throw new IllegalStateException("cannot close cardchooser when cardlist is empty!");}
		player.hand().initFillOperation();
		player.hand().fillWith(list, true);
		player.hand().sort();
		chooser.row().clear(); a=1F;
		for (int i=0; i<list.size(); i++) {
			initCardMove(i, player.hand().getPos().x()+vecadd[i], player.hand().getPos().y(), player.hand().cardW, player.hand().cardH);
		}
		return true;
	}

	@Override
	public void completed() {
		chooser.disable();
		player.hand().unflagAll();
		this.unregister();
	}
	
	public ICardChooserUI getChooser() {
		return chooser;
	}
	
	public Perspective<?> getPlayer() {
		return player;
	}
	
	public String toString() {
		return "ccToHand {a:"+a+"}";
	}

}

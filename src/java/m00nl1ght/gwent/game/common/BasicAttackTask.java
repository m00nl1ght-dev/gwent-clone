package m00nl1ght.gwent.game.common;

import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.voidUI.sequence.MoveHelper;

import org.newdawn.slick.Graphics;

public class BasicAttackTask extends SequenceTask implements CardCallback {
	
	protected MoveHelper mvh = new MoveHelper();
	protected boolean wait = true, ignoreArmor = false;
	protected Card target, card;
	protected int damage;
	protected int state = 0, var = 2;

	public BasicAttackTask(SequenceHandler container, int priority, Card card, int damage, boolean ignoreArmor, int variant) {
		super(container, priority); this.card=card; this.damage=damage; this.ignoreArmor=ignoreArmor; this.var=variant;
	}
	
	public void render(Graphics g) {
		switch (state) {
		case 0:
			Loader.iAttackSprite[var].setRotation(mvh.angle);
			Loader.iAttackSprite[var].draw(mvh.x, mvh.y, mvh.w, mvh.h); break;
		case 1:
			Loader.iAttackHit[var].getSprite((int)mvh.step).draw(target.x+target.w/2-64+50, target.y+target.h/2-64, 128, 128); break;
		}
	}

	public void update() {
		switch (state) {
		case 0:
			if (mvh.updateMove()) completed(); break;
		case 1:
			mvh.step++; if (mvh.step>=4) {completed();} break;
		}
	}
	
	public boolean start() {
		if (wait) {return false;}
		mvh.x=card.x+card.w/2-64F/2; mvh.y=card.y+card.h/2-128F/2; mvh.w=64F; mvh.h=128F;
		mvh.initMove(target.x+target.w/2-64F/2, target.y+target.h/2-128F/2, 64F, 128F, 20F);
		return true;
	}
	
	public void completed() {
		switch (state) {
		case 0:
			target.damage(handler, card, damage, ignoreArmor);
			mvh.step=0; state++; break;
		case 1:
			this.unregister(); break;
		}
	}
	
	@Override
	public void result(Card card) {
		this.target=card;
		wait=false;
	}

	@Override
	public void cancel() {
		
	}

	@Override
	public void exception(int i) {
		
	}
	
	public String toString() {
		return "BasicAttack {s:"+state+" v:"+var+" t:"+target+"}";
	}

}

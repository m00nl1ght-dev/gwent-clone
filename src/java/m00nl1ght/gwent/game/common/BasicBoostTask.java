package m00nl1ght.gwent.game.common;

import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.voidUI.sequence.MoveHelper;

import org.newdawn.slick.Graphics;

public class BasicBoostTask extends SequenceTask implements CardCallback {
	
	protected MoveHelper mvh = new MoveHelper();
	protected boolean wait = true;
	protected Card target, card;
	protected int amount;
	protected int state = 0, var = 0;

	public BasicBoostTask(SequenceHandler container, int priority, Card card, int amount, int variant) {
		super(container, priority); this.card=card; this.amount=amount; this.var=variant;
	}
	
	public void render(Graphics g) {
		switch (state) {
		case 0:
			Loader.iSparkSprite[var].getSprite(mvh.step % 8).setRotation(mvh.angle);
			Loader.iSparkSprite[var].getSprite(mvh.step % 8).draw(mvh.x, mvh.y, mvh.w, mvh.h);
			break;
		case 1:
			Loader.iBoostSprite[var].getSprite(mvh.step).draw(target.x+target.w/2-64F, target.y+target.h/2-85, 128, 170); break;
		}
	}

	public void update() {
		switch (state) {
		case 0:
			if (mvh.updateMove()) completed(); break;
		case 1:
			mvh.step++; if (mvh.step>=12) {completed();} break;
		}
	}
	
	public boolean start() {
		if (wait) {return false;}
		mvh.x=card.x+card.w/2-64F; mvh.y=card.y+card.h/2-64F; mvh.w=128F; mvh.h=128F;
		mvh.initMove(target.x+target.w/2-64F, target.y+target.h/2-64F, 128F, 128F, 10F);
		return true;
	}
	
	public void completed() {
		switch (state) {
		case 0:
			target.boost(handler, card, amount);
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
		return "BasicBoost {s:"+state+" v:"+var+" t:"+target+"}";
	}

}

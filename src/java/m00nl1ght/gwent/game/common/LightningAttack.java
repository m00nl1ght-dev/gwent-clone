package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Graphics;

public class LightningAttack extends SequenceTask implements CardCallback {
	
	protected boolean wait = true, ignoreArmor = false;
	protected Card target, card;
	protected int damage;
	protected int step = 0, var = 0;

	public LightningAttack(SequenceHandler container, int priority, Card card, int damage, boolean ignoreArmor, int variant) {
		super(container, priority); this.card=card; this.damage=damage; this.ignoreArmor=ignoreArmor; this.var=variant;
	}

	public void render(Graphics g) {
		Toolkit.drawBetween(Loader.iLighningSprite[var].getSprite(step % 8), card.x+card.w/2, card.y+card.h/2, target.x+target.w/2, target.y+target.h/2, 32F, 1F);
	}

	public void update() {
		step++; if (step>=40) {completed();}
	}
	
	public boolean start() {
		if (wait) {return false;}
		step=0;
		return true;
	}
	
	public void completed() {
		target.damage(handler, card, damage, ignoreArmor);
		this.unregister();
	}

	@Override
	public void result(Card card) {
		this.target=card;
		wait=false;
	}

	@Override
	public void cancel() {
		this.unregister();
	}

	@Override
	public void exception(int i) {
		
	}
	
	public String toString() {
		return "BasicAttack {p:"+step+" v:"+var+" t:"+target+"}";
	}


}

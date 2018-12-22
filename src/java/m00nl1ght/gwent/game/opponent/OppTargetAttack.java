package m00nl1ght.gwent.game.opponent;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceTask;

import org.newdawn.slick.Graphics;

public class OppTargetAttack extends SequenceTask {

	protected Perspective<?> p;
	protected float r1, r2, z1 = 0.9F, z2 = 1.5F;
	protected boolean z2m = false;
	protected Card sel;
	protected final Card source;
	protected final CardFilter[] filter;
	protected final CardCallback callback;

	public OppTargetAttack(SequenceHandler container, int priority, Card source, CardCallback callback, Perspective<?> p, CardFilter... filter) {
		super(container, priority); this.p=p; this.filter=filter; this.callback=callback; this.source=source;
	}
	
	public void render(Graphics g) {
		if (sel!=null) {
			float x=sel.x+sel.w/2, y=sel.y+sel.h/2;
			Loader.iTargetR0.setRotation(r1); Loader.iTargetR1.setRotation(r2);
			Loader.iTargetR1.draw(x-32*z2, y-32*z2, 64*z2, 64*z2);
			Loader.iTargetR0.draw(x-32*z1, y-32*z1, 64*z1, 64*z1);
			Toolkit.drawBetween(Loader.iTargetLineR, source.x+source.w/2, source.y+source.h/2, x, y, 50F, -100F);
		}
	}

	public void update() {
		sel = source.getLocation().persp.getHighlighted();
		if (sel!=null && !CardFilter.filter(sel, filter)) {sel=null;}
		if (sel!=null) {
			r1+=0.5F; if (r1>360F) {r1=0F;}
			r2-=1F; if (r2<-360F) {r2=0F;}
			if (z2m) {
				z2+=0.005F; if (z2>1.6F) {z2m=false;}
			} else {
				z2-=0.005F; if (z2<1.3F) {z2m=true;}
			}
		}
	}
	
	public String toString() {
		return "OppTargetAttack {s:"+sel+" c:"+source+"}";
	}
	
	public boolean proc() {
		sel = source.getLocation().persp.getHighlighted();
		if (sel!=null && !CardFilter.filter(sel, filter)) {sel=null;}
		if (sel!=null) {
			callback.result(sel);
			this.completed();
			return true;
		}
		return false;
	}
	
	public boolean start() {
		return true;
	}
	
	public void completed() {
		this.unregister();
	}

}

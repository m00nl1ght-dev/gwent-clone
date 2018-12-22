package m00nl1ght.gwent.game.local;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.Main;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.gwent.card.CardFilter;
import m00nl1ght.gwent.game.LocalPlayer;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceTask;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

public class TargetAttack extends SequenceTask implements MouseListener {

	float x = -1; float y = -1;
	protected Perspective<LocalPlayer> p;
	protected float r1, r2, z1 = 0.9F, z2 = 1.5F;
	protected boolean z2m = false;
	protected Card sel;
	protected final Card source;
	protected final CardFilter[] filter;
	protected final CardCallback callback;

	public TargetAttack(SequenceHandler container, int priority, Card source, CardCallback callback, Perspective<LocalPlayer> p, CardFilter... filter) {
		super(container, priority); this.p=p; this.filter=filter; this.callback=callback; this.source=source;
	}
	
	public void render(Graphics g) {
		if (x<0 || y<0) {return;}
		if (sel!=null) {
			x=sel.x+sel.w/2; y=sel.y+sel.h/2;
			Loader.iTargetB0.setRotation(r1); Loader.iTargetB1.setRotation(r2);
			Loader.iTargetB1.draw(x-32*z2, y-32*z2, 64*z2, 64*z2);
			Loader.iTargetB0.draw(x-32*z1, y-32*z1, 64*z1, 64*z1);
		}
		Toolkit.drawBetween(Loader.iTargetLineB, source.x+source.w/2, source.y+source.h/2, x, y, 50F, -100F);
	}

	public void update() {
		sel = StateGame.get().playerL().getHighlighted();
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
		return "TargetAttack {s:"+sel+" c:"+source+"}";
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		this.x=x; this.y=y;
		sel = source.getLocation().persp.getHighlighted();
		if (sel!=null && !CardFilter.filter(sel, filter)) {sel=null;}
		if (sel!=null) {
			callback.result(sel);
			this.completed();
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {this.x=newx; this.y=newy;}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {this.x=newx; this.y=newy;}
	
	public boolean start() {
		Main.appgc().getInput().addMouseListener(this);
		return true;
	}
	
	public void completed() {
		Main.appgc().getInput().removeMouseListener(this);
		this.unregister();
	}

	@Override
	public void setInput(Input input) {}
	
	@Override
	public boolean isAcceptingInput() {return true;}

	@Override
	public void inputEnded() {}

	@Override
	public void inputStarted() {}

	@Override
	public void mouseWheelMoved(int change) {}

	@Override
	public void mousePressed(int button, int x, int y) {}

	@Override
	public void mouseReleased(int button, int x, int y) {}

}

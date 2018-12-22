package m00nl1ght.gwent.game.local;

import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class CardPrewiewTask extends SequenceTask {
	
	private int state;
	protected Card card; 
	protected float prg = 1F;
	protected boolean gray;
	protected int highlight;
	float xBase = Loader.screenW + 200F, yBase = 170F, wBase = 300F, hBase=420F, xOff = -70F - 200F, yPersp = 150F;

	public CardPrewiewTask(SequenceHandler container, int priority, Card card, int highlight, boolean gray) {
		super(container, priority); this.card=card; this.gray=gray; this.highlight=highlight;
		if (card==null) throw new IllegalArgumentException("card is null!");
	}
	
	@Override
	public void render(Graphics g) {
		card.drawFullToBuffer(518F, 724F, 5F, Card.bufferOffsetF, highlight, gray);
		float shift = 1F-Math.abs(prg);
		card.drawQuad(xBase+(xOff-wBase)*shift, yBase-yPersp*prg, xBase+(xOff-wBase)*shift, yBase+hBase+yPersp*prg, xBase+(xOff)*shift, yBase+hBase, xBase+(xOff)*shift, yBase, 518F, 724F, Card.bufferOffsetF);
		Color.setBlendFactor(1F-Math.abs(prg));
		card.drawInfo(g, xBase+xOff-wBase, 615F, 300F, gray ? Card.shadow : Card.alpha100);
		Color.resetBlendFactor();
	}

	@Override
	public boolean start() {
		state=1;
		return true;
	}
	
	@Override
	public void update() {
		switch (state) {
		case 1:
			prg-=0.06F;
			if (prg<=0F) {state=2; prg=0F;}
			break;
		case 3:
			prg-=0.05F;
			if (prg<=-1F) {completed();}
			break;
		}
	}
	
	public void dismiss() {
		if (this.isQueued()) {this.unregister();}
		state=3;
	}
	
	@Override
	public void completed() {
		this.unregister();
	}
	
	public boolean canNextStart() { // not in use yet
		if (state>1) {return true;}
		if (prg>0.5F) {return true;}
		return false;
	}
	
	@Override
	public String toString() {
		return "CardPreview {s:"+state+" p:"+prg+" c:"+card+"}";
	}

}

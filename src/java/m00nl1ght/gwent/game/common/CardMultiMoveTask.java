package m00nl1ght.gwent.game.common;

import java.util.ArrayList;

import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Graphics;

public abstract class CardMultiMoveTask extends SequenceTask {

	protected ArrayList<Card> list;
	protected float stepx[], stepy[], stepw[], steph[];
	protected int step[], frames = 0, fDelta = 0;

	public CardMultiMoveTask(SequenceHandler container, int priority, int frames, int fDelta) {
		super(container, priority); this.frames=frames; this.fDelta=fDelta;
	}
	
	@Override
	public void render(Graphics g) {
		for (int i=0; i<list.size(); i++) {
			if (step[i]>=1) {list.get(i).draw(g, 1, -1);}
		}
	}
	
	public void init(ArrayList<Card> list) {
		if (list==null) {throw new IllegalArgumentException("cardlist is null!");}
		this.list=list;
		this.step=new int[list.size()];
		this.stepx=new float[list.size()]; this.stepy=new float[list.size()];
		this.stepw=new float[list.size()]; this.steph=new float[list.size()];
	}
	
	public void initCardMove(int i, float tx, float ty, float tw, float th) {
		Card card = list.get(i);
		stepw[i] = (tw-card.w)/frames;
		steph[i] = (th-card.h)/frames;
		stepx[i] = (tx-card.x)/(frames-fDelta*i);
		stepy[i] = (ty-card.y)/(frames-fDelta*i);
		step[i]=-fDelta*i;
	}
	
	public void initCardMove(int i, float tx, float ty) {
		Card card = list.get(i);
		stepw[i] = 0F;
		steph[i] = 0F;
		stepx[i] = (tx-card.x)/(frames-fDelta*i);
		stepy[i] = (ty-card.y)/(frames-fDelta*i);
		step[i]=-fDelta*i;
	}
	
	public boolean updateCardMove() {
		for (int i=0; i<list.size(); i++) {
			if (step[i]>0 && step[i]<=frames-fDelta*i) {list.get(i).x+=stepx[i]; list.get(i).y+=stepy[i]; list.get(i).w+=stepw[i]; list.get(i).h+=steph[i];}
			step[i]++;
		}
		if (list.isEmpty() || step[list.size()-1]>=frames-fDelta*(list.size()-1)) {return true;}
		return false;
	}

}

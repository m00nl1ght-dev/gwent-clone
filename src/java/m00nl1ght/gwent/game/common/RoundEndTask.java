package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.game.Perspective;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.sequence.SequenceQueue;
import m00nl1ght.voidUI.sequence.SequenceState;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RoundEndTask extends SequenceState {

	int state = 0, ecL = 0, ecO = 0;
	float a1 = -1F, a2 = -2F;
	final Perspective<?> winner;
	final int round, pointsL, pointsO, crownL, crownO;

	public RoundEndTask(SequenceQueue base, Perspective<?> winner, int round, int pointsL, int pointsO, int crownL, int crownO) {
		super(base, false); this.winner=winner; this.round=round; this.pointsL=pointsL; this.pointsO=pointsO; this.crownL=crownL; this.crownO=crownO;
		if (winner==null) {ecL=1; ecO=1;} else if (winner.isLocal()) {ecL=1;} else {ecO=1;}
	}
	
	@Override
	public void render(Graphics g) {
		Color.setBlendFactor(a2);
		if (ecL>=1) {
			Loader.iCrown[crownL>0?5:4].draw(122, 672, 115, 115);
		}
		if (ecO>=1) {
			Loader.iCrown[crownO>0?5:4].draw(122, 292, 115, 115);
		}
		Color.setBlendFactor(a1, 0.7F);
		g.setColor(Color.black);
		g.fillRect(0, 0, Loader.screenW, Loader.screenH);
		Color.setBlendFactor(a1);
		if (winner==null) {
			Loader.IRoundEnd.draw(Loader.screenW/2-467, Loader.screenH/2-93, Loader.screenW/2+468, Loader.screenH/2+93, 0, 400, 935, 586);
			Toolkit.drawBaseCentered("round ends in a tie", Loader.screenW/2, Loader.screenH/2-45, Loader.fontUM);
		} else if (winner.isLocal()) {
			Loader.IRoundEnd.draw(Loader.screenW/2-467, Loader.screenH/2-93, Loader.screenW/2+468, Loader.screenH/2+93, 0, 0, 935, 186);
			Toolkit.drawBaseCentered("you won the round", Loader.screenW/2, Loader.screenH/2-45, Loader.fontUM);
		} else {
			Loader.IRoundEnd.draw(Loader.screenW/2-467, Loader.screenH/2-93, Loader.screenW/2+468, Loader.screenH/2+93, 0, 200, 935, 386);
			Toolkit.drawBaseCentered("you lost the round", Loader.screenW/2, Loader.screenH/2-45, Loader.fontUM);
		}
		Toolkit.drawBaseCentered(""+pointsL, Loader.screenW/2-52, Loader.screenH/2+45, Loader.fontUM);
		Toolkit.drawBaseCentered(""+pointsO, Loader.screenW/2+52, Loader.screenH/2+45, Loader.fontUM);
		if (StateGame.get().playerL().getCrown()>0) Loader.iCrown[0].draw(Loader.screenW/2-467+293, Loader.screenH/2-93+100, 76, 76);
		if (StateGame.get().playerL().getCrown()>1) Loader.iCrown[1].draw(Loader.screenW/2-467+293, Loader.screenH/2-93+100, 76, 76);
		if (StateGame.get().playerO().getCrown()>0) Loader.iCrown[2].draw(Loader.screenW/2-467+563, Loader.screenH/2-93+100, 76, 76);
		if (StateGame.get().playerO().getCrown()>1) Loader.iCrown[3].draw(Loader.screenW/2-467+563, Loader.screenH/2-93+100, 76, 76);
		Color.setBlendFactor(a2);
		if (ecL>=1) {
			Loader.iCrown[crownL>0?5:4].draw(Loader.screenW/2-467+293, Loader.screenH/2-93+100, 76, 76);
		}
		if (ecO>=1) {
			Loader.iCrown[crownO>0?5:4].draw(Loader.screenW/2-467+563, Loader.screenH/2-93+100, 76, 76);
		}
		Color.resetBlendFactor();
		super.render(g);
	}

	@Override
	public void update() {
		switch (state) {
		case 0:
			a1+=0.05F;
			if (a1>=1F) {a1=1F; state++;}
			return;
		case 1:
			a2+=0.02F;
			if (a2>=1F) {
				a2=1F; state++;
				this.applyEvents();
			}
			return;
		case 2:
			a2-=0.05F;
			if (a2<=0F) {
				if (StateGame.get().playerL().getCrown()>=2 || StateGame.get().playerO().getCrown()>=2) {
					this.completed();
					StateGame.get().gameEnd();
				}
				a2=0F; a1=2F; state++;
			}
			return;
		case 3:
			a1-=0.02F;
			StateGame.get().playerL().setOverlayAlpha(a1);
			StateGame.get().playerO().setOverlayAlpha(a1);
			if (a1<=0F) {a1=0F; state++; StateGame.get().playerL().setOverlayAlpha(0F); StateGame.get().playerO().setOverlayAlpha(a1); this.setShouldEndWhenEmpty(true);}
			return;
		case 4:
			super.update();
			return;
		}
	}
	
	private void applyEvents() {
		if (winner==null) { //tie
			StateGame.get().playerL().roundEnd(this, round, true);
			StateGame.get().playerO().roundEnd(this, round, true);
		} else {
			winner.roundEnd(this, round, true);
			winner.opp().roundEnd(this, round, false);
		}
		StateGame.get().roundEnd(winner, this);
	}
	
	@Override
	public boolean start() {
		return super.start();
	}
	
	@Override
	public void completed() {
		super.completed();
	}

	@Override
	public String toString() {
		return "RoundEndState";
	}

}

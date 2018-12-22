package m00nl1ght.gwent.game.common;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.game.Player;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.sequence.SequenceHandler;
import m00nl1ght.voidUI.sequence.SequenceTask;

public class GameIntro extends SequenceTask {
	
	float a1 = 1F, a2 = 0F, a3 = 0F, a4 = 0F, aVS = 0F;
	int state = 0, step = 0;
	Player p1, p2;

	public GameIntro(SequenceHandler handler, int priority, Player p1, Player p2) {
		super(handler, priority); this.p1=p1; this.p2=p2;
	}
	
	public void render(Graphics g) {
		Color.setBlendFactor(a1);
		g.setColor(Color.black);
		g.fillRect(0, 0, Loader.screenW, Loader.screenH);
		Color.setBlendFactor(a1*a2);
		Loader.iSil[1].draw(0, 0, Loader.screenH/2, Loader.screenH/2);
		Toolkit.drawImageCentered(Loader.IRoundEnd, Loader.screenW/4+180, Loader.screenH/4+30, 140, 620, 543, 655);
		Toolkit.drawImageCentered(Loader.iChar[p1.getCharID()], Loader.screenW/4+180-200, Loader.screenH/4+30, 60, 60);
		Toolkit.drawImageCentered(Loader.iCframe[p1.getCframeID()], Loader.screenW/4+180-200, Loader.screenH/4+30, 150, 150);
		Toolkit.drawBaseCentered(p1.getName().toLowerCase(), Loader.screenW/4+180, Loader.screenH/4, Loader.fontORD);
		Color.setBlendFactor(a1*a3);
		Loader.iSil[0].draw(Loader.screenW-Loader.screenH/2, Loader.screenH/2, Loader.screenH/2, Loader.screenH/2);
		Toolkit.drawImageCentered(Loader.IRoundEnd, Loader.screenW/4*3-180, Loader.screenH/4*3-30, 140, 620, 543, 655);
		Toolkit.drawImageCentered(Loader.iChar[p2.getCharID()], Loader.screenW/4*3-180-200, Loader.screenH/4*3-30, 60, 60);
		Toolkit.drawImageCentered(Loader.iCframe[p2.getCframeID()], Loader.screenW/4*3-180-200, Loader.screenH/4*3-30, 150, 150);
		Toolkit.drawBaseCentered(p2.getName().toLowerCase(), Loader.screenW/4*3-180, Loader.screenH/4*3-60, Loader.fontORD);
		Color.setBlendFactor(a1);
		g.rotate(-400+1500*a2+150, 70+210, 90F-90F*a2);
		p1.getDeck().getLeader().drawFull(g, -400+1500*a2+a4*1500, 70, 300, 420, 1, 0);
		g.rotate(-400+1500*a2+150, 70+210, -(90F-90F*a2));
		g.rotate(Loader.screenW+100-1500*a3+150, Loader.screenH-470+210, -90F+90F*a3);
		p2.getDeck().getLeader().drawFull(g, Loader.screenW+100-1500*a3-a4*1500, Loader.screenH-470, 300, 420, 1, 0);
		g.rotate(Loader.screenW+100-1500*a3+150, Loader.screenH-470+210, 90F-90F*a3);
		Color.setBlendFactor(a1*aVS);
		Loader.IRoundEnd.draw(0, Loader.screenH/2-7, 1000, Loader.screenH/2+8, 0, 600, 1000, 615);
		Loader.IRoundEnd.draw(Loader.screenW-1000, Loader.screenH/2-7, Loader.screenW, Loader.screenH/2+8, 0, 600, 1000, 615);
		Loader.IRoundEnd.draw(Loader.screenW/2-66, Loader.screenH/2-40, Loader.screenW/2+66, Loader.screenH/2+40, 10, 620, 142, 700);
		Color.resetBlendFactor();
	}

	public void update() {
		switch (state) {
		case 0:
			a2+=0.02F;
			if (a2>=1F) {a2=1F; state++;}
			break;
		case 1:
			aVS+=0.05F;
			if (aVS>=1F) {aVS=1F; state++;}
			break;
		case 2:
			a3+=0.02F;
			if (a3>=1F) {a3=1F; state++;}
			break;
		case 3:
			step++;
			if (step>=150) {step=150; state++;}
			break;
		case 4:
			a1-=0.02F;
			a4=1F-a1;
			if (a1<=0F) {a1=0F; this.completed();}
			break;
		}
	}
	
	public boolean start() {
		return true;
	}

	@Override
	public String toString() {
		return "GameIntro";
	}


}

package m00nl1ght.gwent.card;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.card.Cards.CardBase;
import m00nl1ght.gwent.card.Cards.Loyality;
import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.common.CardDeployTask;
import m00nl1ght.gwent.game.common.CardLocation;
import m00nl1ght.gwent.game.common.CardMoveRowToStack;
import m00nl1ght.gwent.game.common.CardRow;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.sequence.SequenceHandler;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Card implements Comparable<Card> {
	
	public float x, y, w, h;
	public static final float oBX = 125F, oBY = 90F;
	public static final float[][] fOff = {{6, 32}, {8, 33}, {6, 32}, {6, 32}, {2, 30}, {6, 27}}; 
	public static final float[][] sOff = {{6, 24}, {4, 24}, {6, 32}, {6, 20}, {2, 20}, {7, 27}}; 
	public static final float[][] tOff = {{60, 13}, {55, 10}, {60, 9}, {55, 12}, {50, 12}, {60, 12}}; 
	private float rotate = 0F;
	public final CardBase base;
	protected CardLocation location;
	public final static Color shadow = new Color(0.4F, 0.4F, 0.4F);
	public final static Color phantom = new Color(0.68F, 0.94F, 1F, 0.85F);
	public final static Color alpha25 = new Color(0F, 0F, 0F, 0.25F);
	public final static Color alpha75 = new Color(0F, 0F, 0F, 0.75F);
	public final static Color alpha100 = Color.white;
	public final static Image buffer = Toolkit.createBufferImage(1024, 1024, Image.FILTER_LINEAR);
	public final static Graphics bufferGC = Toolkit.getBufferGC(buffer);
	public final static float bufferOffsetF = 140F, bufferOffsetN = 5F, hlOff = 60F;

	public int health;
	protected int armor;
	protected Loyality loyality;
	public int factionID;
	
	public Card(CardBase base) {
		this.base=base;
		loyality=base.loyality;
		health=base.health;
		factionID=base.factionID;
	}
	
	 // 85 * 105 board, 100 * 123 hand, 122 * 150 dragged, 300 * 420 prewiew (b=border)
	
	public void drawInFull(Graphics g, float x, float y, float w, float h, float b, int highlight, boolean gray) {
		float wscale = w / 518F; float hscale = h / 724F, wscale2 = w / 320F, hscale2 = h / 400F;
		if (base.tier==null) {b=0F;}
		if (gray) {Loader.fontNum1.setColor(shadow); Loader.symbol1.setColor(shadow);}
		switch (highlight) {
		case 1:
			g.drawImage(Loader.iHighlightB, x-hlOff*wscale, y-hlOff*hscale, x+w+hlOff*wscale, y+h+hlOff*hscale, 0F, 0F, 632F, 838F); break;
		case 2:
			g.drawImage(Loader.iHighlightR, x-hlOff*wscale, y-hlOff*hscale, x+w+hlOff*wscale, y+h+hlOff*hscale, 0F, 0F, 632F, 838F); break;
		}
		if (getRotation()<0.5F) {
			g.drawImage(base.texture(), x+b, y+b, x+w-b*2, y+h-b*2, b, b, 518-b*2, 724-b*2, gray ? shadow : alpha100);
			if (base.tier!=null) {
				g.drawImage(Loader.iFrame[base.tier.getID()], x, y, x+w, y+h, 0, 0, 518, 724, gray ? shadow : alpha100);
				if (base.tier.banner()>0) {
					g.drawImage(Loader.iCback[factionID], x-oBX*wscale, y-oBY*hscale, x+(300F-oBX)*wscale, y+(724F-oBY)*hscale, 930, 0, 930+300, 724, gray ? shadow : alpha100);
				} else {
					g.drawImage(Loader.iCback[factionID], x-oBX*wscale, y-oBY*hscale, x+(300F-oBX)*wscale, y+(724F-oBY)*hscale, 625, 0, 625+300, 724, gray ? shadow : alpha100);
				}
				if (base.health>0)
					Loader.fontNum1.renderBuildCentered(g, String.valueOf(health), x+fOff[factionID][0]*wscale2, y+fOff[ factionID][1]*hscale2, 0.3F*wscale2, 0, 70);
				if (base.row>0)
					Loader.symbol1.renderBuildCenteredX(g, String.valueOf(base.row-1), x+sOff[factionID][0]*wscale2, y+(sOff[ factionID][1]+50F)*hscale2, 0.4F*wscale2, 0);
			}
		} else {
			g.drawImage(Loader.iCback[factionID], x, y, x+w, y+h, 0, 0, 518, 724, gray ? shadow : alpha100);
		}
		if (gray) {Loader.fontNum1.setColor(null); Loader.symbol1.setColor(null);}
	}
	
	public void drawFull(Graphics g, float x, float y, float w, float h, float b, int highlight, boolean gray) {
		drawInFull(g, x, y, w, h, b, highlight, gray);
		this.x=x; this.y=y; this.w=w; this.h=h; 
	}
	
	public void drawFull(Graphics g, float x, float y, float w, float h, float b, int highlight) {
		drawFull(g, x, y, w, h, b, highlight, false);
	}
	
	public void drawFull(Graphics g, float b, int highlight) {
		drawInFull(g, x, y, w, h, b, highlight, false);
	}
	
	public void drawFullToBuffer(float bw, float bh, float bb, float bOff, int highlight, boolean gray) {
		Card.bufferGC.clear();
		this.drawInFull(Card.bufferGC, bOff, bOff, bw, bh, bb, highlight, gray);
		Card.bufferGC.flush();
	}

	public void drawIn(Graphics g, float x, float y, float w, float h, float b, int highlight, Color col) {
		float wscale = w / 518F, hscale = h / 724F, wscale2 = w / 100F, hscale2 = h / 123F;
		if (base.tier==null) {b=0F;}
		if (col!=Color.white) {Loader.fontNum2.setColor(col); Loader.symbol2.setColor(col);}
		switch (highlight) {
		case 1:
			Loader.iHighlightB.draw(x-hlOff*wscale, y-hlOff*hscale, w+hlOff*wscale*2, h+hlOff*hscale*2); break;
		case 2:
			Loader.iHighlightR.draw(x-hlOff*wscale, y-hlOff*hscale, w+hlOff*wscale*2, h+hlOff*hscale*2); break;
		}
		if (getRotation()<0.5F) {
			g.setColor(Color.black);
			if (highlight==-1) {g.fillRect(x-1F, y-1F, w+2F, h+2F);}
			base.texture().draw(x+b, y+b, x+w-b*2, y+h-b*2, 518+b ,b , 518+base.sdW-b*2, base.sdH-b*2, col);
			if (base.tier!=null) {
				int o = -1;
				if (base.health>0) {o++;} if (base.row>0) {o++;}
				Loader.iFrame[base.tier.getID()].draw(x, y, x+w, y+h, 518, 0, 518+100, 123, col);
				g.drawImage(Loader.iCback[factionID], x, y, x+24F*wscale2, y+(96F-tOff[factionID][0]+(o>=0?tOff[factionID][1]+25*o:0))*hscale2, 520, 140+tOff[factionID][0]-(o>=0?tOff[factionID][1]+25*o:0), 520+24, 140+96, col);
				g.setColor(alpha25);
				g.fillRect(x, y, 24F*wscale2, 1F);
				if (base.health>0) {
					Loader.fontNum2.renderBuildCentered(g, String.valueOf(health), x+11.5F*wscale2, y+14F*hscale2, 0.13F*wscale2, 0, 18*wscale2);
				}
				if (base.row>0) {
					Loader.symbol2.renderBuildCenteredX(g, String.valueOf(base.row-1), x+11.5F*wscale2, y+30F*hscale2, 0.16F*wscale2, 0);
				}
			}
		} else {
			Loader.iCback[factionID].draw(x, y, x+w, y+h, 518 ,0 , 518+108, 132, col);
		}
		if (col!=Color.white) {Loader.fontNum2.setColor(null); Loader.symbol2.setColor(null);}
	}
	
	public void drawIn(Graphics g, float x, float y, float w, float h, float b, int highlight, boolean gray) {
		drawIn(g, x, y, w, h, b, highlight, gray ? shadow : alpha100);
	}
	
	public void draw(Graphics g, float x, float y, float w, float h, float b, int highlight, boolean gray) {
		drawIn(g, x, y, w, h, b, highlight, gray);
		this.x=x; this.y=y; this.w=w; this.h=h; 
	}
	
	public void draw(Graphics g, float x, float y, float w, float h, float b, int highlight) {
		draw(g, x, y, w, h, b, highlight, false);
	}
	
	public void draw(Graphics g, float b, int highlight) {
		drawIn(g, x, y, w, h, b, highlight, false);
	}
	
	public void drawPhantom(Graphics g, float x, float y, float w, float h) {
		drawIn(g, x, y, w, h, 1, 1, phantom);
	}
	
	public void drawToBuffer(float bw, float bh, float bb, float bOff, int highlight, boolean gray) {
		Card.bufferGC.clear();
		this.drawIn(Card.bufferGC, bOff, bOff, bw, bh, bb, highlight, gray);
		Card.bufferGC.flush();
	}
	
	public void drawQuad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float bw, float bh, float bOff) {
		float x14 = bOff*(x4-x1)/bw, y12 = bOff*(y2-y1)/bh;
		float x23 = bOff*(x3-x2)/bw, y43 = bOff*(y3-y4)/bh;
		Toolkit.drawQuadTri(buffer, x1-x14, y1-y12, x2-x23, y2+y12, x3+x23, y3+y43, x4+x14, y4-y43, 0F, 0F, bw+bOff*2, bh+bOff*2, alpha100);
	}
	
	public void drawWithRotation(float x, float y, float w, float h, float b, int highlight, boolean gray) {
		this.drawToBuffer(w, h, b, bufferOffsetN, highlight, gray);
		float rx = 0.5F-Math.abs(0.5F-Math.abs(rotate));
		float ry = (rotate<0.5F?-rx:rx) * 0.2F; // <-- adjust?
		//StateGame.debug="ry: "+ry;
		this.drawQuad(x+rx*w, y-ry*h, x+rx*w, y+h+ry*h, x+w-rx*w, y+h-ry*h, x+w-rx*w, y+ry*h, w, h, bufferOffsetN);
	}
	
	public void drawInfo(Graphics g, float x, float y, float w, Color col) {
		g.setColor(Color.black);
		if (base.tier!=null && getRotation()>=0.5F) {return;}
		g.fillRect(x+5, y+2, w, 64F);
		Loader.iDiv2Frame.draw(x+4, y+60F, 304, 10, col);
		Loader.iDiv0Frame[base.tier!=null ? base.tier.ID : 0].draw(x-8, y-10, 24, 80, col);
		Loader.fontU.drawString(x+20, y-15, base.name.toLowerCase(), col);
		Loader.fontU0.drawString(x+20, y+25, base.getTags().toLowerCase(), col);
		if (base.getAbbility()!=null) {
			g.fillRect(x+5, y+2+75F, w, 124F);
			Loader.iDiv2Frame.draw(x+4, y+120F+75F, 304, 10, col);
			Loader.iDiv1Frame[base.tier!=null ? base.tier.ID : 0].draw(x-8, y-9+75F, 24, 139, col);
			Loader.fontU0.drawString(x+20, y-5+75, base.getAbbility().toLowerCase(), col);
		}
	}

	@Override
	public int compareTo(Card card) {
		int tier = this.base.tier.compareTo(card.base.tier);
		if (tier!=0) {return tier;}
		return Integer.compare(this.base.health, card.base.health);
	}

	public Loyality getLoyality() {
		return loyality;
	}
	
	public CardLocation updateLocation(CardLocation location) {
		CardLocation pre = this.location;
		this.location=location;
		return pre;
	}

	public CardLocation getLocation() {return location;}

	public void deploy(SequenceHandler seqctx) {
		SequenceHandler deploy = new CardDeployTask(this);
		seqctx.addTaskNext(deploy);
		base.deploy(deploy, this);
	}
	
	public void damage(SequenceHandler seqctx, Card source, int damage, boolean ignoreArmor) {
		float dmg = damage;
		dmg-=armor; armor-=damage;
		if (armor<0) {armor=0;}
		if (dmg<0) {return;}
		health-=dmg;
		notifyContainer();
		if (health <= 0) {health=0; destroy(seqctx);}
	}

	public void boost(SequenceHandler seqctx, Card source, int amount) {
		health+=amount;
		notifyContainer();
	}
	
	public void destroy(SequenceHandler seqctx) {
		this.armor=0; this.health=0;
		notifyContainer();
		if (location.base instanceof CardRow) {
			new CardMoveRowToStack(seqctx, -1, this, (CardRow)location.base, location.persp.graveyard(), 20, 25, true);
		}
	}
	

	public void roundEnd(SequenceHandler seqctx) {
		new CardMoveRowToStack(seqctx, -2, this, (CardRow)location.base, location.persp.graveyard(), 20, 10, false);
	}
	
	public void reset() {
		this.health=base.health;
		this.armor=0;
		this.rotate=0F;
		notifyContainer();
	}
	
	public void notifyContainer() {
		if (this.location==null || this.location.base==null) {return;}
		location.base.pointsChanged();
	}
	
	@Override
	public String toString() {
		return base.name;
	}

	public float getRotation() {
		return rotate;
	}

	public void setRotation(float rotate) {
		this.rotate = rotate;
	}

}

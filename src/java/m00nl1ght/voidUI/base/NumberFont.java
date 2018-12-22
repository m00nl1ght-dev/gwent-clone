package m00nl1ght.voidUI.base;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class NumberFont {
	
	protected Image sheet;
	protected int[][] vec;
	protected float tscale = 1F;
	protected Color color = Color.white;
	
	public NumberFont(int[][] vec) {
		this.vec=vec;
	}
	
	public NumberFont(int[][] vec, float scale) {
		this.vec=vec; this.tscale=scale;
	}
	
	public void bindTexture(Image sheet) {
		this.sheet=sheet;
	}
	
	public void setColor(Color color) {
		if (color==null) {color=Color.white;}
		this.color=color;
	}
	
	public int renderBuildCentered(Graphics g, String number, float x, float y, float scale, int space, float maxW) {
		int w = -space, h = 0;
		for (char c : number.toCharArray()) {
			int n = Character.getNumericValue(c);
			if (n>=0) {w+=vec[n][3]+space; if (vec[n][4]>h) {h=vec[n][4];}}
		}
		if (w*scale>maxW) {scale*=maxW/(w*scale);}
		return renderBuild(g, number, x-w*scale/2, y-h*scale/2, scale, space);
	}
	
	public int renderBuildCenteredX(Graphics g, String number, float x, float y, float scale, int space) {
		int w = -space;
		for (char c : number.toCharArray()) {
			int n = Character.getNumericValue(c);
			if (n>=0) {w+=vec[n][3]+space;}
		}
		return renderBuild(g, number, x-w*scale/2, y, scale, space);
	}
	
	public int renderBuild(Graphics g, String number, float x, float y, float scale, int space) {
		int width = 0;
		for (char c : number.toCharArray()) {
			int n = Character.getNumericValue(c);
			if (n>=0) {
				g.drawImage(sheet, x+width*scale, y, x+(width+vec[n][3])*scale, y+vec[n][4]*scale, vec[n][1]*tscale, vec[n][2]*tscale, (vec[n][1]+vec[n][3])*tscale, (vec[n][2]+vec[n][4])*tscale, color);
				width+=vec[n][3]+space;
			}
		}
		return width-space;
	}
	
}

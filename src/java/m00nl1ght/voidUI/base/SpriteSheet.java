package m00nl1ght.voidUI.base;

import org.newdawn.slick.Image;

public class SpriteSheet {
	
	public int tilesx, tilesy;
	public float tw, th;
	public Image[] sprites;
	public Image img;
	public boolean yprio = false;
	
	public SpriteSheet(int tilesx, int tilesy, boolean yprio) {
		this.tilesx=tilesx; this.tilesy=tilesy; this.yprio=yprio;
	}
	
	public SpriteSheet load(Image image) {
		this.img=image;
		this.tw=image.getWidth()/tilesx;
		this.th=image.getHeight()/tilesy;
		sprites = new Image[tilesx*tilesy]; int i = 0;
		if (yprio) {
			for (int x = 0; x < tilesx; x++) {
				for (int y = 0; y < tilesy; y++) {
					sprites[i]=image.getSubImage((int)(tw*x), (int)(th*y), (int)tw, (int)th); i++;
				}
			}
		} else {
			for (int y = 0; y < tilesy; y++) {
				for (int x = 0; x < tilesx; x++) {
					sprites[i]=image.getSubImage((int)(tw*x), (int)(th*y), (int)tw, (int)th); i++;
				}
			}
		}
		return this;
	}
	
	public Image getSprite(int idx) {
		return sprites[idx];
	}

}

package m00nl1ght.voidUI.base;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.renderer.SGL;

public class Toolkit {
	
	final public static float base = 0.48F;
	final public static float baseC = 0.6F;
	
	public static void drawBase(String str, float x, float y, UnicodeFont font) {
		font.drawString(x, y - font.getLineHeight()*base, str);
	}
	
	public static void drawBaseCenteredW(String str, float x, float y, UnicodeFont font) {
		font.drawString(x-font.getWidth(str)/2, y - font.getLineHeight()*base, str);
	}
	
	public static void drawBaseCenteredH(String str, float x, float y, UnicodeFont font) {
		drawBaseCenteredH(str, x, y, font, Color.white);
	}
	
	public static void drawBaseCenteredH(String str, float x, float y, UnicodeFont font, Color col) {
		font.drawString(x, y - font.getLineHeight()*baseC, str, col);
	}
	
	public static void drawBaseCentered(String str, float x, float y, UnicodeFont font) {
		drawBaseCentered(str, x, y, font, Color.white);
	}
	
	public static void drawBaseCentered(String str, float x, float y, UnicodeFont font, Color col) {
		font.drawString(x-font.getWidth(str)/2, y - font.getLineHeight()*baseC, str, col);
	}
	
	public static void drawImageCentered(Image image, float cx, float cy, float texX1, float texY1, float texX2, float texY2) {
		drawImageCentered(image, cx, cy, texX1, texY1, texX2, texY2, 1F);
	}
	
	public static void drawImageCentered(Image image, float cx, float cy, float texX1, float texY1, float texX2, float texY2, float scale) {
		float w = (texX2 - texX1)*scale, h = (texY2 - texY1)*scale;
		image.draw(cx-w/2, cy-h/2, cx+w/2, cy+h/2, texX1, texY1, texX2, texY2);
	}
	
	public static void drawImageCentered(Image image, float cx, float cy, float w, float h) {
		drawImageCentered(image, cx, cy, w, h, 1F);
	}
	
	public static void drawImageCentered(Image image, float cx, float cy, float w, float h, float scale) {
		image.draw(cx-w*scale/2, cy-h*scale/2, w*scale, h*scale);
	}
	
	public static void drawQuad(Image image, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float srcx, float srcy, float srcw, float srch, Color col) {
		
		col.bind();
		image.getTexture().bind();

		float width = image.getWidth();
		float height = image.getHeight();
		float textureWidth = image.getTextureWidth();
		float textureHeight = image.getTextureHeight();

		float newTextureOffsetX = (srcx / width) * textureWidth + image.getTextureOffsetX();
		float newTextureOffsetY = (srcy / height) * textureHeight + image.getTextureOffsetY();
		float newTextureWidth = (srcw / width) * textureWidth;
		float newTextureHeight = (srch / height) * textureHeight;

		GL11.glBegin(SGL.GL_QUADS); 
		GL11.glTexCoord2f(newTextureOffsetX, newTextureOffsetY);
		GL11.glVertex3f(x1,y1, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX, newTextureOffsetY + newTextureHeight);
		GL11.glVertex3f(x2,y2, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY + newTextureHeight);
		GL11.glVertex3f(x3,y3, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY);
		GL11.glVertex3f(x4,y4, 0.0f);
		GL11.glEnd(); 

	}
	
	public static void drawQuadTri(Image image, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float srcx, float srcy, float srcw, float srch, Color col) {
		
		col.bind();
		image.getTexture().bind();

		float width = image.getWidth();
		float height = image.getHeight();
		float textureWidth = image.getTextureWidth();
		float textureHeight = image.getTextureHeight();

		float newTextureOffsetX = (srcx / width) * textureWidth + image.getTextureOffsetX();
		float newTextureOffsetY = (srcy / height) * textureHeight + image.getTextureOffsetY();
		float newTextureWidth = (srcw / width) * textureWidth;
		float newTextureHeight = (srch / height) * textureHeight;
		
		float xm = 0.25F*(x1+x2+x3+x4);
		float ym = 0.25F*(y1+y2+y3+y4);

		GL11.glBegin(SGL.GL_TRIANGLES); 
		GL11.glTexCoord2f(newTextureOffsetX, newTextureOffsetY); //0
		GL11.glVertex3f(x1,y1, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX, newTextureOffsetY + newTextureHeight); //1
		GL11.glVertex3f(x2,y2, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth/2, newTextureOffsetY + newTextureHeight/2); //2
		GL11.glVertex3f(xm,ym, 0.0f);
		
		GL11.glTexCoord2f(newTextureOffsetX, newTextureOffsetY + newTextureHeight); //3
		GL11.glVertex3f(x2,y2, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth/2, newTextureOffsetY + newTextureHeight/2); //4
		GL11.glVertex3f(xm,ym, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY + newTextureHeight); //5
		GL11.glVertex3f(x3,y3, 0.0f);
		
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY + newTextureHeight); //6
		GL11.glVertex3f(x3,y3, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth/2, newTextureOffsetY + newTextureHeight/2); //7
		GL11.glVertex3f(xm,ym, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY); //8
		GL11.glVertex3f(x4,y4, 0.0f);
		
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY); //9
		GL11.glVertex3f(x4,y4, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX + newTextureWidth/2, newTextureOffsetY + newTextureHeight/2); //10
		GL11.glVertex3f(xm,ym, 0.0f);
		GL11.glTexCoord2f(newTextureOffsetX, newTextureOffsetY); //11
		GL11.glVertex3f(x1,y1, 0.0f);
		
		GL11.glEnd(); 

	}
	
	public static void drawBetween(Image target, float sx, float sy, float tx, float ty, float w, float alpha) {
		
		float tex[] = {target.getTextureOffsetX(), target.getTextureOffsetX() + target.getTextureWidth(), target.getTextureOffsetY(), target.getTextureOffsetY() + target.getTextureHeight()};
		float cx = sx+(tx-sx)/2;
		float cy = sy+(ty-sy)/2;
		float l = (float)Math.sqrt(Math.pow((tx-sx), 2) + Math.pow(ty-sy, 2))/2;
		float a = (float)Math.toDegrees(Math.atan2((tx-sx)/2,(sy-ty)/2));
		
		if (l<=0) {return;}
		// negative alpha -> distance for 1F, blended [1F->0F] if distance between points is lower
		if (alpha<0F) {alpha=l>-alpha?1F:l/-alpha;} 
		GL11.glColor4f(1F, 1F, 1F, alpha);
		target.bind();
		
		GL11.glTranslatef(cx, cy, 0F);
		GL11.glRotatef(a, 0F, 0F, 1F);
		GL11.glTranslatef(-cx, -cy, 0F);
		GL11.glBegin(SGL.GL_QUADS); 
		
		GL11.glTexCoord2f(tex[0], tex[2]); //TOP LEFT
		GL11.glVertex3f(cx-w, cy-l, 0.0f);
		
		GL11.glTexCoord2f(tex[0], tex[3]); //BOTTOM LEFT
		GL11.glVertex3f(cx-w, cy+l, 0.0f);
		
		GL11.glTexCoord2f(tex[1], tex[3]); //BOTTOM RIGHT
		GL11.glVertex3f(cx+w, cy+l, 0.0f);
		
		GL11.glTexCoord2f(tex[1], tex[2]); //TOP RIGHT
		GL11.glVertex3f(cx+w, cy-l, 0.0f);
		
		GL11.glEnd();
		GL11.glTranslatef(cx, cy, 0F);
		GL11.glRotatef(-a, 0F, 0F, 1F);
		GL11.glTranslatef(-cx, -cy, 0F);
		
	}
	
	public static Image createBufferImage(int w, int h, int filter) {
		try {
			return new Image(w, h, filter);
		} catch (SlickException e) {
			e.printStackTrace(); return null;
		}
	}
	
	public static Graphics getBufferGC(Image image) {
		try {
			return image.getGraphics();
		} catch (SlickException e) {
			e.printStackTrace(); return null;
		}
	}

}

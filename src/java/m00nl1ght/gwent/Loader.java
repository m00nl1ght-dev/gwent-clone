package m00nl1ght.gwent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import m00nl1ght.voidUI.base.NumberFont;
import m00nl1ght.voidUI.base.ResourceLoader.LoadResource;
import m00nl1ght.voidUI.base.ResourceLoader.PreLoad;
import m00nl1ght.voidUI.base.SpriteSheet;

import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.GradientEffect;
import org.newdawn.slick.font.effects.OutlineEffect;

public class Loader { //contains all assets and resources
	
	public static int[][] nu1pos = {{0,403,208,98,190},{1,8,9,50,195},{2,64,11,99,188},{3,169,11,98,188},{4,276,6,107,205},{5,384,11,109,189},{6,10,208,98,191},{7,107,208,98,190},{8,203,208,98,190},{9,301,207,98,191}};
	public static int[][] nu2pos = {{0,412,224,99,173},{1,4,27,58,173},{2,64,27,99,173},{3,170,27,96,173},{4,278,27,102,173},{5,385,27,109,173},{6,10,224,98,173},{7,103,224,110,173},{8,219,224,98,173},{9,318,224,98,173}};
	public static int[][] sympos = {{0,0,0,128,128},{1,128,0,128,128},{2,256,0,128,128},{3,384,0,128,128},{4,0,128,128,128},{5,128,128,128,128},{6,256,128,128,128},{7,384,128,128,128}};

	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	public static float screenW = (float) screen.getWidth(), screenH = (float) screen.getHeight();
	public static Random rand = new Random();
	
	@PreLoad @LoadResource // black font
	public static UnicodeFont fontB = createFont("Neverwinter", 50, Color.black);
	@PreLoad @LoadResource // white font
	public static UnicodeFont fontW = createFont("Neverwinter", 50, Color.white);
	@LoadResource // outlined normal font
	public static UnicodeFont fontU = createFontOutlinedGradient("Neverwinter", 50, new Color(0.9F, 0.9F, 0.9F), new Color(0.5F, 0.5F, 0.5F), 0.3F, new Color(0.2F, 0.2F, 0.2F, 0.5F), 3, 3, 0);
	@LoadResource // outlined large font
	public static UnicodeFont fontUM = createFontOutlinedGradient("Neverwinter", 70, new Color(0.9F, 0.9F, 0.9F), new Color(0.5F, 0.5F, 0.5F), 0.3F, new Color(0F, 0F, 0F, 0.5F), 3, 3, 0);
	@LoadResource // outlined large font
	public static UnicodeFont fontUX = createFontOutlinedGradient("Neverwinter", 100, new Color(0.9F, 0.9F, 0.9F), new Color(0.5F, 0.5F, 0.5F), 0.3F, new Color(0.2F, 0.2F, 0.2F, 0.5F), 3, 3, 0);
	@LoadResource // outlined small font
	public static UnicodeFont fontU0 = createFontOutlinedGradient("Neverwinter", 30, new Color(0.9F, 0.9F, 0.9F), new Color(0.5F, 0.5F, 0.5F), 0.3F, new Color(0.2F, 0.2F, 0.2F, 0.5F), 3, 2, 15);
	@LoadResource // outlined blue font
	public static UnicodeFont fontBL = createFontOutlinedGradient("Neverwinter", 40, new Color(16, 117, 156), color(16, 117, 156, 255, 4F), 0.8F, new Color(0F, 0F, 0F, 0.6F), 3, 2, 0);
	@LoadResource // outlined red font
	public static UnicodeFont fontRD = createFontOutlinedGradient("Neverwinter", 40, new Color(156, 40, 8), color(156, 40, 8, 255, 4F), 0.8F, new Color(0F, 0F, 0F, 0.6F), 3, 2, 0);
	@LoadResource // outlined alpha font
	public static UnicodeFont fontN = createFontOutlined("Neverwinter", 30, new Color(0F, 0F, 0F, 1F), new Color(1F, 1F, 1F, 0.1F), 3, 2, 0);
	@LoadResource // outlined bright blue font
	public static UnicodeFont fontOBL = createFontOutlinedGradient("Neverwinter", 50, new Color(16, 117, 156), color(16, 117, 156, 255, 2F), 0.4F, new Color(0F, 0F, 0F, 0.6F), 3, 2, 0);
	@LoadResource // outlined bright red font
	public static UnicodeFont fontORD = createFontOutlinedGradient("Neverwinter", 50, new Color(156, 40, 8), color(156, 40, 8, 255, 2F), 0.3F, new Color(0F, 0F, 0F, 0.6F), 3, 2, 0);
	@LoadResource // outlined darker red font
	public static UnicodeFont fontODC = createFontOutlinedGradient("Neverwinter", 50, color(156, 40, 8, 255, 2F), color(156, 40, 8, 255, 1.5F), 0.5F, new Color(0F, 0F, 0F, 0.6F), 3, 2, 0);
	
	@LoadResource("cardmisc/graveyard.png")
	public static Image iGraveyard;
	@LoadResource("cards/%s.png")
	public static Image iCard[] = new Image[4];
	@LoadResource("cardmisc/cardback%s.png")
	public static Image iCback[] = new Image[6];
	@LoadResource("cardmisc/frame%s.png")
	public static Image iFrame[] = new Image[4];
	@LoadResource("cardmisc/highlight_blue.png")
	public static Image iHighlightB;  
	@LoadResource("cardmisc/highlight_red.png")
	public static Image iHighlightR; 
	@LoadResource("cardmisc/ash.png")
	public static Image iAsh;  
	
	@PreLoad @LoadResource("UI/bg.png")
	public static Image iBackground;
	@PreLoad @LoadResource("UI/genBG.png")
	public static Image iGenBG;
	@PreLoad @LoadResource("UI/woodframe.png")
	public static Image iWoodframe;
	@PreLoad @LoadResource("UI/loader.png")
	public static Image iLoader;
	@PreLoad @LoadResource("UI/loader_bg.png")
	public static Image iLoader_bg;
	@PreLoad @LoadResource("UI/loader_f.png")
	public static Image iLoader_f;
	
	@LoadResource("UI/numbers.png")
	public static NumberFont fontNum1 = new NumberFont(nu2pos);
	@LoadResource("UI/numbers1.png")
	public static NumberFont fontNum2 = new NumberFont(nu2pos, 0.125F);
	@LoadResource("UI/symbol.png")
	public static NumberFont symbol1 = new NumberFont(sympos);
	@LoadResource("UI/symbol1.png")
	public static NumberFont symbol2 = new NumberFont(sympos, 0.25F);
	@LoadResource("UI/game.png")
	public static Image iGameBG0;
	@LoadResource("UI/game2.png")
	public static Image iGameBG;
	@LoadResource("UI/red_ribbon.png")
	public static Image iRibbonR; 
	@LoadResource("UI/blue_ribbon.png")
	public static Image iRibbonB; 
	@LoadResource("char/n%s.png")
	public static Image iCframe[] = new Image[25]; 
	@LoadResource("UI/sil%s.png")
	public static Image iSil[] = new Image[2];
	@LoadResource("char/c%s.png")
	public static Image iChar[] = new Image[4];
	@LoadResource("scene/m%s.png")
	public static Image iAttackSprite[] = new Image[5];
	@LoadResource("scene/hit%s.png")
	public static SpriteSheet iAttackHit[] = {new SpriteSheet(2, 2, true), new SpriteSheet(2, 2, true), new SpriteSheet(2, 2, true), new SpriteSheet(2, 2, true), new SpriteSheet(2, 2, true)};
	@LoadResource("scene/boost%s.png")
	public static SpriteSheet iBoostSprite[] = {new SpriteSheet(4, 3, false), new SpriteSheet(4, 3, false), new SpriteSheet(4, 3, false), new SpriteSheet(4, 3, false), new SpriteSheet(4, 3, false)};
	@LoadResource("scene/spark%s.png")
	public static SpriteSheet iSparkSprite[] = {new SpriteSheet(4, 4, false), new SpriteSheet(4, 4, false), new SpriteSheet(4, 4, false), new SpriteSheet(4, 4, false), new SpriteSheet(4, 4, false)};
	@LoadResource("scene/lighning%s.png")
	public static SpriteSheet iLighningSprite[] = {new SpriteSheet(8, 1, false), new SpriteSheet(8, 1, false), new SpriteSheet(8, 1, false), new SpriteSheet(8, 1, false), new SpriteSheet(8, 1, false)};
	
	
	@LoadResource("UI/roundend.png")
	public static Image IRoundEnd;
	@LoadResource("UI/crown%s.png")
	public static Image iCrown[] = new Image[6];
	@LoadResource("UI/arrowglow.png")
	public static Image iArrowGlow;
	@LoadResource("UI/lineB.png")
	public static Image iTargetLineB;
	@LoadResource("UI/lineR.png")
	public static Image iTargetLineR;
	@LoadResource("UI/targetB0.png")
	public static Image iTargetB0;
	@LoadResource("UI/targetB1.png")
	public static Image iTargetB1;
	@LoadResource("UI/targetR0.png")
	public static Image iTargetR0;
	@LoadResource("UI/targetR1.png")
	public static Image iTargetR1;
	@LoadResource("UI/dv%s.png")
	public static Image iDiv0Frame[] = new Image[3];
	@LoadResource("UI/dw%s.png")
	public static Image iDiv1Frame[] = new Image[3];
	@LoadResource("UI/df.png")
	public static Image iDiv2Frame;
	@LoadResource("UI/div1.png")
	public static Image iDivider1;
	@LoadResource("UI/div2.png")
	public static Image iDivider2;
	@LoadResource("UI/iconDeck.png")
	public static Image iIconDeck;
	@LoadResource("UI/iconHand.png")
	public static Image iIconHand;
	@LoadResource("UI/iconGrave.png")
	public static Image iIconGrave;
	@LoadResource("UI/overlay.png")
	public static Image iOverlay;  
	@LoadResource("UI/woodflat.png")
	public static Image iWoodflat;  
	@LoadResource("UI/btn1a.png")
	public static Image iBtn1A; 
	@LoadResource("UI/btn1s.png")
	public static Image iBtn1S; 
	@LoadResource("UI/btn1d.png")
	public static Image iBtn1D; 
	@LoadResource("UI/input.png")
	public static Image iInputF;
	@LoadResource("UI/inputS.png")
	public static Image iInputS; 
	@LoadResource("UI/paperlist.png")
	public static Image iList1B; 
	@LoadResource("UI/papercover.png")
	public static Image iList1C; 
	@LoadResource("UI/arrow1.png")
	public static Image iArrowU; 
	@LoadResource("UI/arrow2.png")
	public static Image iArrowD; 
	@LoadResource("UI/closeB.png")
	public static Image iCloseB; 
	@LoadResource("UI/closeS.png")
	public static Image iCloseS; 
	@LoadResource("UI/closeD.png")
	public static Image iCloseD;
	@LoadResource("UI/fTile%s.png")
	public static Image iFtile[] = new Image[6]; 
	
	private static UnicodeFont createFont(String font, int size, Color col) {
		UnicodeFont f = new UnicodeFont(new Font(font, Font.PLAIN, size));
		//implement load from file?
		//try {f = new UnicodeFont(font, size, false, false);} catch (SlickException e) {e.printStackTrace();}
		f.getEffects().add(new ColorEffect(col));
		f.addAsciiGlyphs();
		return f;
	}
	
	private static UnicodeFont createFontOutlined(String font, int size, Color col, Color outline, int outln, int paX, int paY) {
		UnicodeFont f = new UnicodeFont(new Font(font, Font.PLAIN, size));
		f.setPaddingBottom(outln); f.setPaddingTop(outln);
		f.setPaddingLeft(outln); f.setPaddingRight(outln);
		f.setPaddingAdvanceX(-outln-paX); f.setPaddingAdvanceY(-outln-paY);
		f.getEffects().add(new OutlineEffect(outln, outline));
		f.getEffects().add(new ColorEffect(col));
		f.addAsciiGlyphs();
		return f;
	}
	
	private static UnicodeFont createFontOutlinedGradient(String font, int size, Color top, Color bottom, float scale, Color outline, int outln, int paX, int paY) {
		UnicodeFont f = new UnicodeFont(new Font(font, Font.PLAIN, size));
		f.setPaddingBottom(outln); f.setPaddingTop(outln);
		f.setPaddingLeft(outln); f.setPaddingRight(outln);
		f.setPaddingAdvanceX(-outln-paX); f.setPaddingAdvanceY(-outln-paY);
		f.getEffects().add(new OutlineEffect(outln, outline));
		f.getEffects().add(new GradientEffect(top, bottom, scale));
		f.addAsciiGlyphs();
		return f;
	}
	
	private static Color color(int r, int g, int b, int a, float m) {
		return new Color((int)(r/m), (int)(g/m), (int)(b/m), a);
	}

}

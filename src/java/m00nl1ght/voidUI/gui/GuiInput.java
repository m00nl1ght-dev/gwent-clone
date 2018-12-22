package m00nl1ght.voidUI.gui;

import m00nl1ght.gwent.Loader;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.Toolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

public class GuiInput extends GuiElement {
	
	private Image iActive, iSelected, iDisabled;
	protected String string = "";
	protected String hint = "";
	private boolean carret = true;
	private int carretTimer = 0, carretTime = 30;
	protected UnicodeFont fontB = Loader.fontB;
	protected Color colB = Color.white;
	protected UnicodeFont fontD = Loader.fontB;
	protected Color colD = new Color(1F, 1F, 1F, 0.4F);
	private float xo=50F, yo=0F;
	protected int maxLenght=0;

	public GuiInput(GuiContainer<? extends GuiElement> parent, ElementPos position) {
		this(parent, position, "", Loader.iBtn1A, Loader.iBtn1S, Loader.iBtn1D);
	}
	
	public GuiInput(GuiContainer<? extends GuiElement> parent, ElementPos position, String str) {
		this(parent, position, str, Loader.iBtn1A, Loader.iBtn1S, Loader.iBtn1D);
	}

	public GuiInput(GuiContainer<? extends GuiElement> parent, ElementPos position, Image iActive, Image iSelected, Image iDisabled) {
		this(parent, position, "", iActive, iSelected, iDisabled);
	}
	
	public GuiInput(GuiContainer<? extends GuiElement> parent, ElementPos position, String str, Image iActive, Image iSelected, Image iDisabled) {
		super(parent, position);
		this.iActive=iActive; this.iSelected=iSelected; this.iDisabled=iDisabled; this.string =str;
	}
	
	@Override
	public void mouseClicked(int button, int x2, int y2, int clicks) {
		if (button==0) {
			base.elementCallback(this, 0, button); 
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (key==Input.KEY_BACK) {
			if (string.length()>0) {
				dataChangeRequested(string.substring(0, string.length()-1));
				base.elementCallback(this, 3, 0);
			}
			return;
		}
		if (key==Input.KEY_RETURN) {
			base.elementCallback(this, 1, 0);
			return;
		}
		if (fontB.getWidth(""+c)>0 && fontB.getFont().canDisplay(c)) {
			if (maxLenght>0 && string.length()>=maxLenght) {return;}
			dataChangeRequested(string+c);
			base.elementCallback(this, 2, 0);
		}
	}
	
	public void dataChangeRequested(String newData) {
		string=newData;
	}
	
	@Override
	public void render(GameContainer container, GuiBasedGame game, Graphics g) throws SlickException {
		if (enabled) {
			boolean focus=this.hasKeyboardFocus();
			if (base.getSelectedElement()==this) {
				iSelected.draw(pos.x(), pos.y(), pos.w(), pos.h());
			} else {
				iActive.draw(pos.x(), pos.y(), pos.w(), pos.h());
			}
			if (string.length()==0 && !focus) {
				Toolkit.drawBaseCenteredH(hint, pos.x()+xo, pos.y()+pos.h()/2+yo, fontD, colD);
			} else {
				Toolkit.drawBaseCenteredH(string, pos.x()+xo, pos.y()+pos.h()/2, fontB, colB);
				if (focus && carret) {Toolkit.drawBaseCenteredH("_", pos.x()+xo+fontB.getWidth(string)+3, pos.y()+pos.h()/2+yo, fontD, colD);}
				if (focus && carretTime>0) {carretTimer++; if (carretTimer>=carretTime) {carretTimer=0; carret=!carret;}}
			}
		} else {
			iDisabled.draw(pos.x(), pos.y(), pos.w(), pos.h());
			Toolkit.drawBaseCenteredH(string, pos.x()+xo, pos.y()+pos.h()/2+yo, fontD, colD);
		}
	}
	
	public void setString(String str) {this.string=str;}
	public void setHint(String str) {this.hint=str;}
	public String getString() {return string;}
	
	public void setFont(UnicodeFont font, Color col) {this.fontB=font; this.colB=col;}
	public void setHintFont(UnicodeFont font, Color col) {this.fontD=font; this.colD=col;}
	public void setOffset(float xo, float yo, int maxLenght) {this.xo=xo; this.yo=yo; this.maxLenght=maxLenght;}

}

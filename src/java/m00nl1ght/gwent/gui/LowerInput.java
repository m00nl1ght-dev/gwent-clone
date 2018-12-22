package m00nl1ght.gwent.gui;

import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.GuiContainer;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiInput;

import org.newdawn.slick.Image;

public class LowerInput extends GuiInput {

	public LowerInput(GuiContainer<? extends GuiElement> parent, ElementPos position) {
		super(parent, position);
	}
	
	public LowerInput(PopupNewProfile parent, ElementPos position, Image iAcive, Image iSelected, Image iDisabled) {
		super(parent, position, iAcive, iSelected, iDisabled);
	}

	@Override
	public void dataChangeRequested(String newData) {
		if (newData.endsWith("ß")) {string+="ss"; return;}
		if (newData.endsWith("°")) {return;}
		string=newData.toLowerCase();
	}

}

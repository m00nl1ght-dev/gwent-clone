package m00nl1ght.gwent;

import m00nl1ght.gwent.gui.AnimationLoading;
import m00nl1ght.gwent.gui.ListEntryProfile;
import m00nl1ght.gwent.gui.PopupNewProfile;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.NumberFont;
import m00nl1ght.voidUI.base.ResourceLoader;
import m00nl1ght.voidUI.base.SpriteSheet;
import m00nl1ght.voidUI.base.Toolkit;
import m00nl1ght.voidUI.gui.ElementPos;
import m00nl1ght.voidUI.gui.ElementPos.AlignType;
import m00nl1ght.voidUI.gui.GuiButton;
import m00nl1ght.voidUI.gui.GuiElement;
import m00nl1ght.voidUI.gui.GuiList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

public class StateSplash extends ResourceLoader {

	AnimationLoading a_loader;
	int state=0;
	StepReflectionLoad<Image> stepI;
	StepReflectionLoad<SpriteSheet> stepSS;
	StepReflectionLoad<UnicodeFont> stepF;
	StepReflectionLoad<NumberFont> stepN;
	GuiButton btnContinue; GuiList profileList; PopupNewProfile popupNewP;
	
	protected StateSplash(GameContainer gc, GuiBasedGame g) {
		super(gc, g); step.wait=100; step.string="Loading..."; //init step in superclass
		this.addStep(stepI=new StepReflectionLoad<Image>(new ImageLoader(), Image.class));
		this.addStep(stepSS=new StepReflectionLoad<SpriteSheet>(new SpriteSheetLoader(), SpriteSheet.class));
		this.addStep(stepF=new StepReflectionLoad<UnicodeFont>(new FontLoader(), UnicodeFont.class));
		this.addStep(stepN=new StepReflectionLoad<NumberFont>(new NumberFontLoader(stepI.getLoader()), NumberFont.class));
		stepI.string="Loading assets..."; stepSS.string="Loading assets..."; stepF.string="Loading assets..."; stepN.string="Loading assets...";
		this.register(Loader.class);
	}
	
	@Override
	public void init(GameContainer arg0, GuiBasedGame arg1) throws SlickException {
		a_loader = new AnimationLoading(null, 3F);
		Profile.getList().add(new Profile("player", 0));
	}
	
	@Override
	public void initGUI(GameContainer arg0, GuiBasedGame arg1) throws SlickException {
		addElement(profileList=new GuiList(this, new ElementPos(this, Loader.screenW/2-232, Loader.screenH/2-420, 464, 768, 1), 47, 47, 0.9F, 0.05F, Loader.iList1B, Loader.iList1C)).setVisible(false);
		profileList.setClipping(50, 50, 50, 100); profileList.setVerticalSpacing(-22);
		addElement(btnContinue=new GuiButton(this, new ElementPos(this, Loader.screenW/2-220, Loader.screenH/2+350, 441, 98, 2), "test game")); // DEBUG -> "sign in"
		addElement(popupNewP=new PopupNewProfile(this, new ElementPos(this, 0, 0, 810, 404, 10, AlignType.CENTEREDXY), 0.7F)).setVisible(false);
		updateProfileList();
	}
	
	public void updateProfileList() {
		profileList.clearElements();
		for (Profile p : Profile.getList()) {
			profileList.addElement(new ListEntryProfile(profileList, p, 384, 96, 0, 120).setShiftOnHover(10F));
		}
	}

	@Override
	public void render(GameContainer arg0, GuiBasedGame arg1, Graphics g) throws SlickException {
		Loader.iBackground.draw(0, 0, Loader.screenW, Loader.screenH);
		Loader.iWoodframe.setRotation(90F);
		Loader.iWoodframe.draw(Loader.screenW/2-512F, Loader.screenH/2-351.5F, 1024, 703);
		switch (state) {
		case 0:
			a_loader.render(g);
			Toolkit.drawBaseCenteredW(this.getString(), Loader.screenW/2, Loader.screenH/2+200, Loader.fontW);
			Toolkit.drawBaseCenteredW(this.getProgress(), Loader.screenW/2, Loader.screenH/2-10, Loader.fontW);
			break;
		case 1:
			a_loader.render(g);
			Toolkit.drawBaseCenteredW("ready", Loader.screenW/2, Loader.screenH/2-10, Loader.fontW);
			break;
		case 2:
			Toolkit.drawBaseCenteredW("choose your profile", Loader.screenW/2, Loader.screenH/2-450, Loader.fontW);
			break;
		}
		super.render(arg0, arg1, g);
	}

	@Override
	public void update(GameContainer arg0, GuiBasedGame arg1, int arg2) throws SlickException {
		switch (state) {
		case 0:
			if (!this.isReady()) {
				a_loader.update();
				try {this.loadNext();} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
			} else {
				//Profile.load(Profile.getList().get(0));
				//Main.instance.enterState(Main.instance.stateMap.getID());
				//DEBUG ^^ for faster testing
				a_loader.setState(-1F); state=1; 
			} break;
		}
		super.update(arg0, arg1, arg2);
	}
	
	@Override
	public void elementCallback(GuiElement element, int eventID, int data) {
		if (element==btnContinue) {
			switch (state) {
			case 1:
				state=2; profileList.setVisible(true); btnContinue.setString("add new profile"); return;
			case 2:
				popupNewP.reset();
				popupNewP.setVisible(true); 
			} return;
		}
		if (element instanceof ListEntryProfile) {
			switch (state) {
			case 2:
				Profile.load(((ListEntryProfile)element).profile);
				Main.instance.enterState(Main.instance.stateMap.getID());
			} return;
		}
	}
	
	protected class StepLoadProfileList extends Step { // todo: later when profile loading/saving is implemented :)
		
		protected void loadNextAsset() throws SlickException, IllegalArgumentException, IllegalAccessException {}
		public int getAssetCount() {return 0;}
		
	}

}

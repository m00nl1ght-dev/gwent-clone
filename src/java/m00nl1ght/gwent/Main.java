package m00nl1ght.gwent;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import m00nl1ght.gwent.game.StateGame;
import m00nl1ght.gwent.game.end.StateGameEnd;
import m00nl1ght.voidUI.base.GuiBasedGame;
import m00nl1ght.voidUI.base.GuiState;
import m00nl1ght.voidUI.base.ResourceLoader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Main extends GuiBasedGame {
	
	protected static AppGameContainer appgc;
	protected static Main instance;
	public static Random rand = new Random();
	protected StateMap stateMap;
	protected StateGame stateGame; 
	protected StateGameEnd stateGameEnd; 
	protected ResourceLoader stateSplash;

	public Main(String gamename) {
		super(gamename);
		this.setResourceLocation("src/resources/");
	}
	
	@Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.setLoader(stateSplash = new StateSplash(gc, this));
        this.addState(stateMap = new StateMap(1, gc));
        this.addState(stateGame = new StateGame(2, gc));
        this.addState(stateGameEnd = new StateGameEnd(3, gc));
    }

	public static void main(String[] args) throws Exception {
		System.out.println("This source is for educational purposes only.\nAll assets like textures and fonts have been removed because I do not have copyright for them.\nObviously the game will throw an error if you try to run it.");
//		try { // original startup code
//			instance = new Main("Gwent Clone");
//			appgc = new AppGameContainer(instance);
//			appgc.setDisplayMode(1920, 1080, true);
//			appgc.setShowFPS(false);
//			appgc.setTargetFrameRate(60);
//			appgc.setMinimumLogicUpdateInterval(1);
//			appgc.setVSync(true);
//			appgc.start();
//		} catch (SlickException ex) {
//			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//		}
	}
	
	public static Main instance() {return instance;}
	public static AppGameContainer appgc() {return appgc;}
	public static GuiState getGuiState(int id) {return instance.getState(id);}
	public static int[] getMousePos() {
		int[] pos = new int[2];
		pos[0] = appgc.getInput().getAbsoluteMouseX();
		pos[1] = appgc.getInput().getAbsoluteMouseY();
		return pos;
	}
	
}
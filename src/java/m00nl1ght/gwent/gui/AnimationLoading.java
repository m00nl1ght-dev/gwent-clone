package m00nl1ght.gwent.gui;

import org.newdawn.slick.Graphics;

import m00nl1ght.voidUI.sequence.SequenceTask;
import m00nl1ght.gwent.Loader;
import m00nl1ght.voidUI.sequence.SequenceHandler;

public class AnimationLoading extends SequenceTask {
	
	private float state;
	private float speed;

	public AnimationLoading(SequenceHandler container, float speed) {
		super(container, 0);
		this.speed=speed;
		state=0F;
	}
	
	@Override
	public void render(Graphics g) {
		Loader.iLoader_bg.draw(Loader.screenW/2-210, Loader.screenH/2-210, 420, 420);
		if (state==-1F) {
			Loader.iLoader_f.draw(Loader.screenW/2-210, Loader.screenH/2-210, 420, 420);
		} else {
			Loader.iLoader.draw(Loader.screenW/2-210, Loader.screenH/2-210, 420, 420);
		}
	}
	
	@Override
	public void update() {
		if (state<0F) {return;}
		state+=speed; if (state>=360F) {state-=360F;}
		Loader.iLoader.setRotation(state);
	}
	
	public void setState(float state) {
		this.state=state;
	}
	
	public String toString() {
		return "AniLoading";
	}

}

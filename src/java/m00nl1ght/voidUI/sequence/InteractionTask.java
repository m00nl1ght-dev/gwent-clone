package m00nl1ght.voidUI.sequence;

import org.newdawn.slick.Graphics;


public abstract class InteractionTask<I extends Interactable> extends SequenceTask {
	
	protected final I target; 
	protected final String info;

	public InteractionTask(I target, String info) {
		super(target.getHandler(), target.getPriority());
		this.target=target; this.info=info;
	}
	
	public InteractionTask(I target) {
		this(target, "");
	}
	
	@Override
	public final void render(Graphics g) {}
	
	@Override
	public final boolean start() {
		return true;
	}
	
	@Override
	public final void completed() {
		this.unregister();
	}
	
	@Override
	public abstract void update();
	
	@Override
	public String toString() {
		return "InteractionTask {"+target+"}";
	}

}

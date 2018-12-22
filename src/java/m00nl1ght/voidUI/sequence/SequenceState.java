package m00nl1ght.voidUI.sequence;

import m00nl1ght.gwent.game.Perspective;


public class SequenceState extends AdvancedSeqHandler {
	
	protected SequenceQueue base;

	public SequenceState(SequenceQueue base, boolean shouldEndWhenEmpty) {
		super(); this.base=base; this.endWhenEmpty=shouldEndWhenEmpty;
	}
	
	public String toString() {return "DefaultState";}
	
	public void enter() {
		
	}
	
	public void leave() {
		
	}
	
	@Override
	public void completed() {
		base.remove(this);
	}

	public boolean canInteract(Perspective<?> persp) {
		return false;
	}

}

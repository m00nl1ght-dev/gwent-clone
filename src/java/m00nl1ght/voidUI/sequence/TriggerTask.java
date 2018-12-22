package m00nl1ght.voidUI.sequence;


public class TriggerTask extends SequenceTask {
	
	protected Triggerable target;

	public TriggerTask(SequenceHandler handler, int priority, Triggerable target) {
		super(handler, priority); this.target=target;
	}
	
	@Override
	public boolean start() {
		target.trigger();
		completed();
		return true;
	}
	
	public interface Triggerable {
		public void trigger();
	}

	@Override
	public String toString() {
		return "TriggerTask {"+target+"}";
	}

}

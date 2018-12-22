package m00nl1ght.voidUI.sequence;

import java.util.ArrayList;

public class SequenceQueue {
	
	protected ArrayList<SequenceState> queue = new ArrayList<SequenceState>();
	
	public SequenceState get() {
		if (queue.isEmpty()) {return null;}
		return queue.get(0);
	}
	
	public void addToQueue(SequenceState state) {
		queue.add(state);
		if (queue.size()==0) {state.enter();}
	}
	
	public void discardCurrent() {
		queue.remove(0).leave();
		if (!queue.isEmpty()) {get().enter();}
	}
	
	public int getSize() {
		return queue.size();
	}

	public void remove(SequenceState sequenceState) {
		if (get()==sequenceState) {discardCurrent();} else {
			queue.remove(sequenceState);
		}
	}

	@Override
	public String toString() {
		return "SequenceQueue";
	}

	public String debug() {
		if (queue.isEmpty()) {return this.toString() + " []";}
		String s = this.toString()+ " [\n";
		for (SequenceTask task : this.queue) {
			s+=task.debug()+",\n";
		}
		s=s.substring(0, s.length()-2)+"\n]";
		return s;
	}

}

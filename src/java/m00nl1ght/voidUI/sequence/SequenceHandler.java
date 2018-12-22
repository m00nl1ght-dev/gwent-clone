package m00nl1ght.voidUI.sequence;

import java.util.ArrayList;
import java.util.Comparator;

import org.newdawn.slick.Graphics;

public class SequenceHandler extends SequenceTask {
	
	protected ArrayList<SequenceTask> tasks = new ArrayList<SequenceTask>();
	public final Comparator<SequenceTask> COMPARATOR = new SeqComparator();
	protected int topPriority = 0;
	protected boolean endWhenEmpty = false, keepAlive = false, debug = false;
	
	public SequenceHandler() {
		super(null, 0);
	}
	
	public SequenceHandler(SequenceHandler handler, int priority) {
		super(handler, priority);
	}
	
	public SequenceTask addTask(SequenceTask task) {
		tasks.add(task);
		if (task.getPriority()>topPriority) {topPriority=task.getPriority();}
		task.handler=this;
		this.updatePriorities();
		debug("Task has been added with priority "+task.getPriority()+" : "+task.toString());
		return task;
	}
	
	public SequenceTask addTask(SequenceTask task, int priority) {
		task.setPriority(priority);
		return addTask(task);
	}
	
	public SequenceTask addTaskEqual(SequenceTask task) {
		return addTask(task, topPriority);
	}
	
	public SequenceTask addTaskNext(SequenceTask task) {
		return addTask(task, topPriority+1);
	}
	
	public boolean tryStart(SequenceTask task) {
		task.setQueued(!task.start());
		if (!task.isQueued()) {debug("Started task using #tryStart(): "+task.toString()); return true;} else {return false;}
	}
	
	public void removeTask(SequenceTask task) {
		tasks.remove(task);
		task.handler=null;
		debug("Task has been removed: "+task.toString());
		this.updatePriorities();
		this.updateSelf();
	}
	
	public void reset() {
		debug("Handler has been cleared.");
		tasks.clear();
		this.updateSelf();
	}
	
	@Override
	public void update() {
		for (SequenceTask task : tasks.toArray(new SequenceTask[tasks.size()])) {
			if (task.handler!=this) {continue;} // if task was removed during update -> do not update it
			if (!task.isQueued()) {
				task.update();
			} else {
				task.setQueued(!task.start());
				if (!task.isQueued()) {debug("Started task: "+task.toString());}
			}
		}
		updateSelf();
	}
	
	protected void updateSelf() {
		if (!keepAlive && endWhenEmpty && tasks.isEmpty()) {completed(); debug("Handler ended because no tasks left.");}
	}
	
	@Override
	public void render(Graphics g) {
		for (SequenceTask task : tasks) {
			if (!task.isQueued()) {
				task.render(g);
			}
		}
	}
	
	public void updatePriorities() {
		tasks.sort(COMPARATOR);
	}
	
	public int getTopPriority() {
		return topPriority;
	}
	
	public void setTopPriority(int topPriority) {
		this.topPriority=topPriority;
	}
	
	public SequenceHandler setShouldEndWhenEmpty(boolean flag) {
		this.endWhenEmpty=flag; return this;
	}
	
	public void makeKeepAlive(boolean keepAlive) {
		this.keepAlive=keepAlive; // if true, ignores all end conditions (like whenEmpty)
	}
	
	private static class SeqComparator implements Comparator<SequenceTask> {

		@Override
		public int compare(SequenceTask arg0, SequenceTask arg1) {
			return Integer.compare(arg0.getPriority(), arg1.getPriority());
		}
		
	}
	
	protected void debug(String str) {
		if (!debug) {return;}
		System.out.println("["+this.toString()+"] "+str);
	}
	
	@Override
	public String toString() {
		return "SequenceHandler";
	}

	public String debug() {
		if (tasks.isEmpty()) {return this.toString() + " []";}
		String s = this.toString()+ " <"+(this.endWhenEmpty?"N":"X")+"> [\n";
		for (SequenceTask task : this.tasks) {
			s+=task.debug()+",\n";
		}
		s=s.substring(0, s.length()-2)+"\n]";
		return s;
	}
	
	public boolean isIdle() {
		return tasks.isEmpty();
	}
	
}

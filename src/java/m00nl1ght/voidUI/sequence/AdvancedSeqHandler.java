package m00nl1ght.voidUI.sequence;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Graphics;

public class AdvancedSeqHandler extends SequenceHandler {
	
	// extra queue for priority logic
	protected ArrayList<SequenceTask> queued = new ArrayList<SequenceTask>();
	protected int minTaskPrio = Integer.MAX_VALUE;
	{debug=true;} //debug
	
	public AdvancedSeqHandler(SequenceHandler handler, int priority) {
		super(handler, priority);
	}
	
	public AdvancedSeqHandler() {
		super();
	}

	@Override
	public SequenceTask addTask(SequenceTask task) {
		queued.add(task);
		if (task.getPriority()>topPriority) {topPriority=task.getPriority();}
		task.handler=this;
		debug("Task has been added with priority "+task.getPriority()+" : "+task.toString());
		this.updateQueueList();
		this.updateQueue();
		return task;
	}
	
	@Override
	public boolean tryStart(SequenceTask task) {
		if (!tasks.contains(task)) {debug("Failed #tryStart(): "+task); return false;}
		task.setQueued(!task.start());
		if (!task.isQueued()) {debug("Started task using #tryStart(): "+task.toString()); return true;} else {return false;}
	}
	
	@Override
	public void removeTask(SequenceTask task) {
		if (queued.remove(task)) {this.updateQueueList();}
		if (tasks.remove(task)) {this.updateTaskList();}
		debug("Task has been removed: "+task.toString());
		task.handler=null;
		this.updateQueue();
		this.updateSelf();
	}
	
	@Override
	public void reset() {
		tasks.clear();
		queued.clear();
		debug("Handler has been cleared.");
		minTaskPrio = Integer.MAX_VALUE;
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
	
	@Override
	protected void updateSelf() {
		if (!keepAlive && endWhenEmpty && tasks.isEmpty() && queued.isEmpty()) {completed(); debug("Handler ended, no tasks left.");}
	}
	
	@Override
	public void render(Graphics g) {
		for (SequenceTask task : tasks) {
			if (!task.isQueued()) {
				task.render(g);
			}
		}
	}
	
	@Override
	public void updatePriorities() {
		this.updateTaskList();
		this.updateQueueList();
		this.updateQueue();
	}
	
	private void updateQueue() {
		boolean dirty = false;
		Iterator<SequenceTask> it = queued.iterator();
		while (it.hasNext()) {
			SequenceTask task = it.next();
			if (task.getPriority()>minTaskPrio) {break;}
			if (task.getPriority()<minTaskPrio) {minTaskPrio=task.getPriority();}
			tasks.add(task);
			//debug("Task has been dequeued (p:"+task.getPriority()+", m:"+minTaskPrio+"): "+task.toString());
			it.remove();
			dirty=true;
		}
		if (dirty) {
			updateTaskList();
			updateQueueList();
		}
	}
	
	private void updateTaskList() {
		tasks.sort(COMPARATOR);
		minTaskPrio=Integer.MAX_VALUE;
		for (SequenceTask task : tasks) {
			if (task.getPriority()<minTaskPrio) {minTaskPrio=task.getPriority();}
		}
	}
	
	private void updateQueueList() {
		queued.sort(COMPARATOR);
	}
	
	public String toString() {
		return "AdvSeqHandler";
	}

	public String debug() {
		if (tasks.isEmpty() && queued.isEmpty()) {return this.toString() + " []";}
		String s = this.toString() + " <"+minTaskPrio+(this.endWhenEmpty?"N":"X")+"> [\n";
		for (SequenceTask task : this.tasks) {
			s+=task.debug()+",\n";
		}
		for (SequenceTask task : this.queued) {
			s+="~"+task.debug()+",\n";
		}
		s=s.substring(0, s.length()-2)+"\n]";
		return s;
	}
	
	@Override
	public boolean isIdle() {
		return tasks.isEmpty() && queued.isEmpty();
	}

}

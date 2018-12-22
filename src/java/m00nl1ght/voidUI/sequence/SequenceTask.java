package m00nl1ght.voidUI.sequence;

import org.newdawn.slick.Graphics;


public abstract class SequenceTask implements Interactable {
	
	private boolean queued = true;
	protected SequenceHandler handler;
	private int priority;
	
	public SequenceTask(SequenceHandler handler, int priority) {
		this.handler=handler; this.priority=priority;
		if (handler!=null) {this.register();} else {
			if (priority<0) {throw new IllegalArgumentException("Cannot assign placeholder priority to a task without assigning a sequence handler to it first!");}
		}
	}

	public void render(Graphics g) {
		
	}

	public void update() {
		
	}
	
	public boolean start() {
		return true;
	}
	
	public void completed() {
		this.unregister();
	}
	
	public final SequenceHandler getHandler() {return this.handler;}
	
	public final void unregister() {
		if (handler!=null) {handler.removeTask(this);}
	}

	private final void register() {
		switch (priority) {
		case -1: handler.addTaskNext(this); break;
		case -2: handler.addTaskEqual(this); break;
		default: handler.addTask(this); break;
		}
	}
	
	public boolean tryStart() {
		if (handler==null) {return false;}
		return handler.tryStart(this);
	}
	
	public final SequenceTask register(SequenceHandler container) {
		if (this.handler!=null) {throw new IllegalStateException("Task is already registered!");}
		this.handler=container;
		register(); return this;
	}
	
	public final SequenceTask register(SequenceHandler container, int priority) {
		if (this.handler!=null) {throw new IllegalStateException("Task is already registered!");}
		this.handler=container; this.priority=priority;
		register(); return this;
	}
	
	public final boolean isQueued() {
		return queued;
	}
	
	public final void setQueued(boolean queued) {
		this.queued=queued;
	}
	
	public final int getPriority() {
		return this.priority;
	}
	
	public final void setPriority(int priority) {
		this.priority=priority;
		if (handler!=null) {this.handler.updatePriorities();}
	}

	public String debug() {
		return toString();
	}
	
	@Override
	public abstract String toString();

}

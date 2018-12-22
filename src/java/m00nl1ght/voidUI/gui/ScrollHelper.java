package m00nl1ght.voidUI.gui;

import m00nl1ght.gwent.Main;

public class ScrollHelper {
	
	protected float val = 0F, min = 0F, max = 0F, mntm = 0F, mntmdec = 0.95F, thres=0.1F;
	protected boolean locked = false;
	
	public ScrollHelper(float deceleration, float momentumThreshold) {
		this.mntmdec=deceleration; this.thres=momentumThreshold;
	}
	
	public float setPropertiesExt(float w, float wt, float border) {
		if (w<wt-border*2) {
			val = wt/2-w/2;
			min=val;
			max=val;
		} else {
			min=-w+wt-border;
			max=border;
			val=max;
		}
		return val;
	}
	
	public void setProperties(float min, float max, float val) {
		this.val = val;
		this.min = min;
		this.max = max;
	}
	
	public boolean updateOnRender() {
		if (locked) {mntm=0F; return false;}
		if (!Main.appgc().getInput().isMouseButtonDown(0) && mntm!=0F) {
			dragIn(0F, mntm);
			mntm*=mntmdec;
			if (Math.abs(mntm)<thres) {mntm=0F;}
			return true;
		} return false;
	}
	
	protected void dragIn(float oldV, float newV) {
		val+=newV-oldV;
		if (val<min) val=min;
		if (val>max) val=max;
	}
	
	public void drag(float oldV, float newV) {
		if (locked) {return;}
		dragIn(oldV, newV);
		mntm=newV-oldV;
	}
	
	public void applyMomentum(float momentum) {
		if (locked) {return;}
		mntm+=momentum;
	}
	
	public void lock(boolean lock) {
		this.locked=lock;
	}
	
	public boolean isLocked() {return locked;}
	public float get() {return val;}

}

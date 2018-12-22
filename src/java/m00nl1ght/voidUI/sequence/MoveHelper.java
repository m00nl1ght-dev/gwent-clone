package m00nl1ght.voidUI.sequence;

public class MoveHelper {

	public float angle, stepx, stepy, stepw, steph, x, y, w, h;
	protected float l;
	public int step = 0, frames;
	
	public float initMove(float tx, float ty, float tw, float th, float speed) {
		this.l = (float)Math.sqrt(Math.pow((tx-x), 2) + Math.pow(ty-y, 2))/2;
		initMove(tx, ty, tw, th, (int)(l/speed));
		return l;
	}
	
	public void initMove(float tx, float ty, float tw, float th, int frames) {
		this.angle = (float)Math.toDegrees(Math.atan2((tx-x)/2,(y-ty)/2));
		this.frames = frames;
		stepw = (tw-w)/frames;
		steph = (th-h)/frames;
		stepx = (tx-x)/frames;
		stepy = (ty-y)/frames;
		step=1;
	}
	
	public boolean updateMove() {
		x+=stepx; y+=stepy; w+=stepw; h+=steph;
		if (step>=frames) {return true;}
		step++; return false;
	}

}

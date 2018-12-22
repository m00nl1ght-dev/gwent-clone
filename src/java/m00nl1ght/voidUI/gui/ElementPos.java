package m00nl1ght.voidUI.gui;

public class ElementPos {
	
	protected float x, y, w, h, actSX, actSY; //cached, calculated actual screen pos & effective element scale
	protected float posX, posY, posW, posH, depth, scaleX = 1F, scaleY = 1F; //original src pos
	protected AlignType align = AlignType.DEFAULT;
	protected final ElementPos parent;
	
	public ElementPos(ElementPos parent, float x, float y, float w, float h, float depth, float scaleX, float scaleY, AlignType align) {
		if (parent==null) throw new IllegalArgumentException("ElementPos parent cannot be null.");
		if (align==null) throw new IllegalArgumentException("ElementPos align cannot be null.");
		this.posX=x; this.posY=y; this.posW=w; this.posH=h; this.depth=depth; this.scaleX=scaleX; this.scaleY=scaleY; this.parent=parent; this.align=align; update();
	}
	
	protected ElementPos(float x, float y, float w, float h, float depth) {
		this.parent=null; this.posX=x; this.posY=y; this.posW=w; this.posH=h; this.depth=depth; this.update();
	}
	
	public ElementPos(ElementPos parent, float x, float y, float w, float h, float depth, AlignType align) {
		this(parent, x, y, w, h, depth, 1F, 1F, align);
	}
	
	public ElementPos(ElementPos parent, float x, float y, float w, float h, float depth) {
		this(parent, x, y, w, h, depth, AlignType.DEFAULT);
	}
	
	public ElementPos(GuiElement parent, float x, float y, float w, float h, float depth) {
		this(parent.getPos(), x, y, w, h, depth, AlignType.DEFAULT);
	}
	
	public ElementPos(GuiElement parent, float x, float y, float w, float h, float depth, AlignType align) {
		this(parent.getPos(), x, y, w, h, depth, 1F, 1F, align);
	}
	
	public void update() {
		//TODO parent.update() here? hmm... 
		actSX=parent.actSX*scaleX;
		actSY=parent.actSY*scaleY;
		w=posW*actSX;
		h=posH*actSY;
		switch (align) {
		case DEFAULT:
			x=parent.x+posX*actSX;
			y=parent.y+posY*actSY;
			break;
		case TOPRIGHT:
			x=parent.x+parent.w-(posX+posW)*actSX;
			y=parent.y+posY*actSY;
			break;
		case BOTTOMLEFT:
			x=parent.x+posX*actSX;
			y=parent.y+parent.h-(posY+posH)*actSY;
			break;
		case BOTTOMRIGHT:
			x=parent.x+parent.w-(posX+posW)*actSX;
			y=parent.y+parent.h-(posY+posH)*actSY;
			break;
		case CENTEREDX:
			x=parent.x+parent.w/2-w/2+posX*actSX;
			y=parent.y+posY*actSY;
			break;
		case CENTEREDY:
			x=parent.x+posX*actSX;
			y=parent.y+parent.h/2-h/2+posY*actSY;
			break;
		case CENTEREDXY:
			x=parent.x+parent.w/2-w/2+posX*actSX;
			y=parent.y+parent.h/2-h/2+posY*actSY;
			break;
		}
	}
	
	public float x() {return x;}
	public float y() {return y;}
	public float w() {return w;}
	public float h() {return h;}
	public float depth() {return depth;}

	public float cX() {return x+w/2;}
	public float cY() {return y+h/2;}
	
	public float srcX() {return posX;}
	public float srcY() {return posY;}
	public float srcW() {return posW;}
	public float srcH() {return posH;}
	public float scaleX() {return scaleX;}
	public float scaleY() {return scaleY;}
	
	public void src(float x, float y, float w, float h) {this.posX=x; this.posY=y; this.posW=w; this.posH=h; update();}
	
	public void add(float x, float y) {posX+=x; posY+=y; update();}
	public void add(float x, float y, float w, float h) {posX+=x; posY+=y; posW+=w; posH+=h; update();}
	
	public boolean within(int mx, int my) {
		if (mx < x || my < y || mx > x+w || my > y+h) {return false;}
		return true;
	}
	
	public enum AlignType {
		
		DEFAULT, 
		TOPRIGHT, 
		BOTTOMLEFT, 
		BOTTOMRIGHT,
		CENTEREDX,
		CENTEREDY,
		CENTEREDXY;
		
	}

	public ElementPos copy() {
		return new ElementPos(parent, posX, posY, posW, posH, depth, scaleX, scaleY, align);
	}
	
	public ElementPos sub(float depth) {
		return new ElementPos(this, 0, 0, posW, posH, depth, 1F, 1F, AlignType.DEFAULT);
	}
	
	public static class ParentElementPos extends ElementPos {

		public ParentElementPos(float x, float y, float w, float h) {
			super(x, y, w, h, 0);
		}
		
		@Override
		public void update() {
			actSX=1F; actSY=1F; w=posW; h=posH; x=posX; y=posY;
		}
		
	}
	
	public static class ScrollableElementPos extends ElementPos {
		
		public final ScrollHelper scroll;

		public ScrollableElementPos(ElementPos parent, float x, float y, float depth, float decelaration, float momentumThreshold) {
			super(parent, x, y, depth, 0, 0); this.scroll = new ScrollHelper(decelaration, momentumThreshold);
		}
		
		public void init(float min, float max, float val) {
			scroll.setProperties(min, max, val);
		}

		@Override
		public void update() {
			if (scroll==null) {return;}
			actSX=parent.actSX*scaleX;
			actSY=parent.actSY*scaleY;
			w=posW*actSX;
			h=posH*actSY;
			switch (align) {
			case DEFAULT:
				x=parent.x+posX*actSX;
				y=parent.y+(posY+scroll.get())*actSY;
				break;
			case TOPRIGHT:
				x=parent.x+parent.w-posX*actSX;
				y=parent.y+(posY+scroll.get())*actSY;
				break;
			case BOTTOMLEFT:
				x=parent.x+posX*actSX;
				y=parent.y+parent.h-(posY+scroll.get())*actSY; //not shure
				break;
			case BOTTOMRIGHT:
				x=parent.x+parent.w-posX*actSX;
				y=parent.y+parent.h-(posY+scroll.get())*actSY; //not shure
				break;
			case CENTEREDX:
				x=parent.x+parent.w/2-w/2+posX*actSX;
				y=parent.y+(posY+scroll.get())*actSY;
				break;
			case CENTEREDY:
				x=parent.x+posX*actSX;
				y=parent.y+parent.h/2-h/2+(posY+scroll.get())*actSY;
				break;
			case CENTEREDXY:
				x=parent.x+parent.w/2-w/2+posX*actSX;
				y=parent.y+parent.h/2-h/2+(posY+scroll.get())*actSY;
				break;
			}
		}
		
	}

}

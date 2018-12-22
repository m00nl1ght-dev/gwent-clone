package m00nl1ght.voidUI.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Vector;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.renderer.SGL;

import m00nl1ght.gwent.Loader;
import m00nl1ght.gwent.Main;
import m00nl1ght.voidUI.gui.GuiScreen;

public abstract class ResourceLoader extends GuiScreen {
	
	protected GuiBasedGame game;
	protected GameContainer container;
	
	protected int stepID = 0;
	protected Step step;
	private boolean ready = false;
	private int assetCount = 0, loadedCount = 0;
	private Vector<Step> steps = new Vector<Step>();
	private Vector<Class<?>> classes = new Vector<Class<?>>();
	protected String sourcepath = "";
	protected static Image base = loadBaseImg();
	
	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
	public @interface LoadResource {String value() default "";}
	@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
	public @interface PreLoad {}
	
	protected ResourceLoader(GameContainer gc, GuiBasedGame g, String sourcePath) {
		super(-1, gc); this.game=g; this.container=gc; this.sourcepath=sourcePath;
		this.addStep(new StepInitLoader(classes));
		step=steps.get(stepID);
	}

	protected ResourceLoader(GameContainer gc, GuiBasedGame g) {
		this(gc, g, g.getResourceLocation());
	}
	
	final void preload() throws SlickException, IllegalArgumentException, IllegalAccessException {
		for (Step step : steps) {step.preLoadAssets();}
	}
	
	final private void loaderFinished() throws SlickException {
		ready=true;
		game.loaderFinished();
		this.initGUI(container, game);
	}
	
	final protected void loadNext() throws SlickException, IllegalArgumentException, IllegalAccessException {
		if (ready) {throw new IllegalStateException("loadNext() called after loader was already finished!");}
		if (step.isFinished()) {
			stepID++; loadedCount+=step.getAssetCount();
			if (stepID>=steps.size()) {loaderFinished(); step=null; return;}
			step=steps.get(stepID); return;
		}
		step.loadNextAsset();
	}
	
	final protected void addStep(Step step) {
		steps.add(step);
	}
	
	final protected void register(Class<?> c) {
		classes.add(c);
	}
	
	public final boolean isReady() {return ready;}
	public String getString() {return step==null ? "" : step.string;}
	public String getProgress() {return (loadedCount+(step==null ? 0 : step.assetIdx))+"/"+assetCount;}
	public int getWait() {return step==null ? 0 : step.wait;}
	public final int getAssetCount() {return assetCount;}
	public void init(GameContainer container, GuiBasedGame game) throws SlickException {}
	public void initGUI(GameContainer container, GuiBasedGame game) throws SlickException {};
	
	protected interface TypeLoader<T> {
		public T load(T asset, LoadResource src, int i) throws SlickException;
	}
	
	protected class ImageLoader implements TypeLoader<Image> {
		public ImageLoader() {}
		public Image load(Image asset, LoadResource src, int i) throws SlickException {
			if (i<0) return new Image(sourcepath+src.value());
			return new Image(sourcepath+src.value().replaceAll("%s", ""+i));
		}
	}
	
//	protected class STBImageLoader implements TypeLoader<Image> {
//		public STBImageLoader() {}
//		public Image load(Image asset, LoadResource src, int i) throws SlickException {
//			String p = i<0?sourcepath+src.value():sourcepath+src.value().replaceAll("%s", ""+i);
//			int[] w = new int[1], h = new int[1], c = new int[1];
//			ByteBuffer data = STBImage.stbi_load(p, w, h, c, 4);
//			if (data==null) {throw new SlickException("Failed to load image: "+STBImage.stbi_failure_reason());}
//			if (c[0]!=4) {throw new SlickException("Failed to load image: Only 4-channel decoding supported, but image has "+c[0]+" channels.");}
//			Texture tex = Texture.generate(w[0], h[0]);
//			tex.bind();
//			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w[0], h[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
//			stbi_image_free(data);
//			System.out.println("Texture loaded: w="+w[0]+" h:"+h[0]);
//			Texture.bindNone();
//			return tex;
//		}
//	} //for testing
	
	protected class SpriteSheetLoader implements TypeLoader<SpriteSheet> {
		public SpriteSheetLoader() {}
		public SpriteSheet load(SpriteSheet asset, LoadResource src, int i) throws SlickException {
			if (i<0) return asset.load(new Image(sourcepath+src.value()));
			return asset.load(new Image(sourcepath+src.value().replaceAll("%s", ""+i)));
		}
	}
	
	protected class FontLoader implements TypeLoader<UnicodeFont> {
		public FontLoader() {}
		public UnicodeFont load(UnicodeFont asset, LoadResource src, int i) throws SlickException {
			asset.loadGlyphs();
			return asset;
		}
	}
	
	protected class NumberFontLoader implements TypeLoader<NumberFont> {
		private TypeLoader<Image> imgL;
		public NumberFontLoader(TypeLoader<Image> imgL) {this.imgL=imgL;}
		public NumberFont load(NumberFont asset, LoadResource src, int i) throws SlickException {
			asset.bindTexture(imgL.load(null, src, i));
			return asset;
		}
	}
	
	protected class Step {
		
		protected int assetIdx = 0;
		public String string = "";
		public int wait = 0;
		
		protected boolean register(Field field, boolean pre) {return false;}
		protected void loadNextAsset() throws SlickException, IllegalArgumentException, IllegalAccessException {}
		protected void preLoadAssets() throws SlickException, IllegalArgumentException, IllegalAccessException {}
		public int getAssetCount() {return 0;}
		public boolean isFinished() {return assetIdx>=getAssetCount();};
		
	}
	
	protected class StepInitLoader extends Step {
		
		private Vector<Class<?>> classes;
		protected StepInitLoader(Vector<Class<?>> c) {this.classes=c;}
		
		protected void preLoadAssets() throws SlickException, IllegalArgumentException, IllegalAccessException {
			for (Class<?> c : classes) {
				for (Field f : c.getFields()) {
					if (f.isAnnotationPresent(LoadResource.class)) {
						if (f.isAnnotationPresent(PreLoad.class)) {
							for (Step s : steps) {if (s.register(f, true)) break;}
						} else {
							for (Step s : steps) {if (s.register(f, false)) break;}
							assetCount+=1;
						}
					}
				}
			}
		}
		public boolean isFinished() {return true;};
		
	}
	
	protected class StepReflectionLoad<T> extends Step {
		
		protected Vector<Field> assets = new Vector<Field>();
		protected Vector<Field> assetsPre = new Vector<Field>();
		protected Field asset; private LoadResource src;
		protected TypeLoader<T> loader;
		protected Class<T> clazz;
		protected T[] partial;
		protected int partialIdx = 0;
		
		public StepReflectionLoad(TypeLoader<T> loader, Class<T> clazz) {this.loader=loader; this.clazz=clazz;}

		protected boolean register(Field field, boolean pre) {
			if (field.getType()==clazz || (field.getType().isArray() && field.getType().getComponentType()==clazz)) {
				if (pre) {assetsPre.add(field);} else {assets.add(field);} return true;
			} else {return false;}
		}
		
		@SuppressWarnings("unchecked")
		protected void preLoadAssets() throws SlickException, IllegalArgumentException, IllegalAccessException {
			for (Field asset : assetsPre) {
				src = asset.getAnnotation(LoadResource.class);
				if (asset.getType().isArray()) { // arrays untested
					partial=(T[]) asset.get(this);
					for (int i = 0; i < partial.length; i++) {
						partial[i]=loader.load(partial[i], src, i);
					}
				} else {
					asset.set(this, loader.load((T)asset.get(this), src, -1));
				}
			} partial=null; src=null;
		}
		
		@SuppressWarnings("unchecked")
		protected void loadNextAsset() throws SlickException, IllegalArgumentException, IllegalAccessException {
			if (partial==null) {
				if (assetIdx>=assets.size()) {throw new IllegalStateException("ResourceLoader cannot load idx "+assetIdx+" when assets array has only "+assets.size()+" elements!");}
				asset = assets.get(assetIdx);
				src = asset.getAnnotation(LoadResource.class);
				if (asset.getType().isArray()) {
					partial=(T[])asset.get(this); partialIdx=0;
				}
			}
			if (partial!=null) {
				partial[partialIdx]=loader.load(partial[partialIdx], src, partialIdx);
				if (partialIdx>=partial.length-1) {
					partial=null; partialIdx=0; assetIdx++;
				} else {partialIdx++;} return;
			}
			asset.set(this, loader.load((T)asset.get(this), src, -1));
			assetIdx++;
		}
		
		public int getAssetCount() {return assets.size();}
		public TypeLoader<T> getLoader() {return loader;}
		
	}

	public void renderPre() {
		GL11.glClear(SGL.GL_COLOR_BUFFER_BIT | SGL.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		Main.appgc().getGraphics().resetTransform();
		Main.appgc().getGraphics().resetFont();
		Main.appgc().getGraphics().resetLineWidth();
		Main.appgc().getGraphics().setAntiAlias(false);
		base.draw(Loader.screenW/2-300F, Loader.screenH/2-200F, 600F, 400F);
		Display.update();
	}
	
	
	private static Image loadBaseImg() {
		try {
			return new Image("src/resources/voidUI/base.png");
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

package m00nl1ght.gwent.game;




public abstract class Player {
	
	protected String name = "Opponent";
	protected String title = "Cardsmith";
	protected int charID = 0;
	protected int frameID = 0;
	protected Deck deckBase = new Deck();
	
	public String getName() {return name;}
	public String getTitle() {return title;}
	public int getCharID(){return charID;}
	public int getCframeID(){return frameID;}
	public Deck getDeck(){return deckBase;}

}

package m00nl1ght.gwent;

import java.util.Vector;

import m00nl1ght.gwent.game.Deck;
import m00nl1ght.gwent.game.LocalPlayer;

public class Profile {
	
	private static Vector<Profile> list = new Vector<Profile>();
	public static Profile user = null; 
	
	private String name = "Unnamed";
	private String title = "Cardsmith";
	private int charID = 0;
	private int frameID = 0;
	private Deck deck = new Deck();
	
	public static void load(Profile profile) {
		user=profile;
	}
	
	public Profile(String name, int charID) {
		this.name=name; this.charID=charID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static Vector<Profile> getList() {
		return list;
	}
	
	public static Profile get(int id) {
		return list.get(id);
	}
	
	public int getCharID() {
		return charID;
	}

	public int getCframeID() {
		return frameID;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public LocalPlayer derivePlayer() {
		return new LocalPlayer(this);
	}

}

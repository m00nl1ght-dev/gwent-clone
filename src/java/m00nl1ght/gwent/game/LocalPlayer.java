package m00nl1ght.gwent.game;

import m00nl1ght.gwent.Profile;

public class LocalPlayer extends Player {
	
	protected Profile profile;
	protected PerspectiveLocal persp; // current game perspective
	
	public LocalPlayer(Profile profile) {
		this.profile=profile;
		this.charID=profile.getCharID();
		this.deckBase=profile.getDeck();
		this.frameID=profile.getCframeID();
		this.name=profile.getName();
		this.title=profile.getTitle();
	}
	
	public void newGame(PerspectiveLocal persp) {
		if (persp==null) {throw new IllegalStateException("persp cannot be null!");}
		if (this.persp!=null) {throw new IllegalStateException("This player is already in a game!");}
		this.persp=persp;
	}
	
	public void gameFinished() {
		validate();
		this.persp=null;
	}
	
	public void onTurnStart() {
		if (persp.hand.getSize()<=0 && persp.leader.getSize()<=0) {
			persp.pass();
		}
	}
	
	private void validate() {
		if (this.persp==null) {throw new IllegalStateException("This player is not in a game!");}
	}

}

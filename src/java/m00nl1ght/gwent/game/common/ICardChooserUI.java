package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.game.common.CardMoveTask.CardCallback;

public interface ICardChooserUI {
	
	public void setCallback(CardCallback callback);
	
	public void setText(String text);
	
	public CardRowLinear row();
	
	public void setAlpha(float alpha);
	
	public void enable();
	public void disable();

	public boolean shouldDrawAni();

	public boolean active();

}

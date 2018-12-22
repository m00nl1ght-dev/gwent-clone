package m00nl1ght.gwent.game.common;

import m00nl1ght.gwent.card.Card;
import m00nl1ght.voidUI.sequence.AdvancedSeqHandler;

public class CardDeployTask extends AdvancedSeqHandler {
	
	protected Card card;

	public CardDeployTask(Card card) {
		super();
		this.card=card;
		this.endWhenEmpty=true;
	}
	
	@Override
	public String toString() {
		return "DeployTask {c:"+card+"}";
	}

}
